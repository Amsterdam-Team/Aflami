package com.example.imageviewer.util

import android.graphics.Bitmap
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.opengl.GLES20
import android.opengl.GLUtils
import androidx.core.graphics.createBitmap
import java.nio.ByteBuffer
import java.nio.ByteOrder


internal class OpenGLBlurProcessor {

    companion object {
        fun blurBitmap(inputBitmap: Bitmap, radius: Float): Bitmap? {
            val processor = OpenGLBlurProcessor()
            return processor.processBlur(inputBitmap, radius)
        }
    }

    private fun processBlur(inputBitmap: Bitmap, radius: Float): Bitmap? {
        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            return null
        }

        val version = IntArray(2)
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            return null
        }

        val configs = arrayOfNulls<EGLConfig>(1)
        val numConfigs = IntArray(1)
        val configAttribs = intArrayOf(
            EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_NONE
        )

        if (!EGL14.eglChooseConfig(
                eglDisplay,
                configAttribs,
                0,
                configs,
                0,
                configs.size,
                numConfigs,
                0
            )
        ) {
            EGL14.eglTerminate(eglDisplay)
            return null
        }

        val config = configs[0]
        val contextAttribs = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )

        val eglContext =
            EGL14.eglCreateContext(eglDisplay, config, EGL14.EGL_NO_CONTEXT, contextAttribs, 0)
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            EGL14.eglTerminate(eglDisplay)
            return null
        }

        val surfaceAttribs = intArrayOf(
            EGL14.EGL_WIDTH, inputBitmap.width,
            EGL14.EGL_HEIGHT, inputBitmap.height,
            EGL14.EGL_NONE
        )

        val eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, config, surfaceAttribs, 0)
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroyContext(eglDisplay, eglContext)
            EGL14.eglTerminate(eglDisplay)
            return null
        }

        if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            cleanup(eglDisplay, eglContext, eglSurface)
            return null
        }

        try {
            val result = performBlurOperation(inputBitmap, radius)

            cleanup(eglDisplay, eglContext, eglSurface)

            return result
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup(eglDisplay, eglContext, eglSurface)
            return null
        }
    }

    private fun cleanup(eglDisplay: EGLDisplay, eglContext: EGLContext, eglSurface: EGLSurface) {
        EGL14.eglMakeCurrent(
            eglDisplay,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_CONTEXT
        )
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglTerminate(eglDisplay)
    }

    private fun performBlurOperation(inputBitmap: Bitmap, radius: Float): Bitmap? {
        val width = inputBitmap.width
        val height = inputBitmap.height

        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)

        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        val program = GLES20.glCreateProgram()
        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            val log = GLES20.glGetProgramInfoLog(program)
            GLES20.glDeleteProgram(program)
            throw RuntimeException("Program linking failed: $log")
        }

        // Setup vertex data
        val quadCoords = floatArrayOf(
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f
        )

        val texCoords = floatArrayOf(
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
        )

        val vertexBuffer = ByteBuffer.allocateDirect(quadCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(quadCoords)
                position(0)
            }

        val texCoordBuffer = ByteBuffer.allocateDirect(texCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(texCoords)
                position(0)
            }

        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] == 0) {
            return null
        }

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, inputBitmap, 0)

        val positionHandle = GLES20.glGetAttribLocation(program, "vPosition")
        val texCoordHandle = GLES20.glGetAttribLocation(program, "vTexCoord")
        val textureUniformHandle = GLES20.glGetUniformLocation(program, "uTexture")
        val radiusHandle = GLES20.glGetUniformLocation(program, "uRadius")
        val textureSizeHandle = GLES20.glGetUniformLocation(program, "uTextureSize")

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUseProgram(program)

        GLES20.glEnableVertexAttribArray(positionHandle)
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        GLES20.glVertexAttribPointer(positionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, texCoordBuffer)

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        GLES20.glUniform1i(textureUniformHandle, 0)

        GLES20.glUniform1f(radiusHandle, radius)
        GLES20.glUniform2f(textureSizeHandle, width.toFloat(), height.toFloat())

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(positionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)

        val pixelBuffer = ByteBuffer.allocateDirect(width * height * 4)
        pixelBuffer.order(ByteOrder.nativeOrder())

        GLES20.glReadPixels(
            0,
            0,
            width,
            height,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            pixelBuffer
        )

        pixelBuffer.rewind()
        val tempBitmap = createBitmap(width, height)
        tempBitmap.copyPixelsFromBuffer(pixelBuffer)

        val matrix = android.graphics.Matrix()
        matrix.preScale(1.0f, -1.0f)
        val outputBitmap = Bitmap.createBitmap(
            tempBitmap, 0, 0, width, height, matrix, false
        )

        tempBitmap.recycle()

        GLES20.glDeleteTextures(1, textureHandle, 0)
        GLES20.glDeleteProgram(program)
        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(fragmentShader)

        return outputBitmap
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] == 0) {
            val log = GLES20.glGetShaderInfoLog(shader)
            GLES20.glDeleteShader(shader)
            throw RuntimeException("Shader compilation failed: $log")
        }

        return shader
    }

    private val vertexShaderCode = """
        attribute vec4 vPosition;
        attribute vec2 vTexCoord;
        varying vec2 fTexCoord;
        
        void main() {
            gl_Position = vPosition;
            fTexCoord = vTexCoord;
        }
    """.trimIndent()

    private val fragmentShaderCode = """
        precision mediump float;
        varying vec2 fTexCoord;
        uniform sampler2D uTexture;
        uniform float uRadius;
        uniform vec2 uTextureSize;
        
        void main() {
            vec2 texelSize = 1.0 / uTextureSize;
            vec4 color = vec4(0.0);
            float totalWeight = 0.0;
            
            // Convert radius to pixel units
            float pixelRadius = uRadius;
            
            // Early exit for no blur
            if (pixelRadius <= 0.5) {
                gl_FragColor = texture2D(uTexture, fTexCoord);
                return;
            }
            
            // Calculate sigma for Gaussian
            float sigma = pixelRadius / 2.0;
            int iRadius = int(ceil(pixelRadius));
            
            // Sample in a circular pattern
            for (int x = -iRadius; x <= iRadius; x++) {
                for (int y = -iRadius; y <= iRadius; y++) {
                    float dist = sqrt(float(x * x + y * y));
                    
                    // Only sample within the blur radius
                    if (dist <= pixelRadius) {
                        vec2 offset = vec2(float(x), float(y)) * texelSize;
                        vec2 sampleCoord = fTexCoord + offset;
                        
                        // Clamp to texture bounds
                        sampleCoord = clamp(sampleCoord, vec2(0.0), vec2(1.0));
                        
                        // Calculate Gaussian weight
                        float weight = exp(-(dist * dist) / (2.0 * sigma * sigma));
                        
                        color += texture2D(uTexture, sampleCoord) * weight;
                        totalWeight += weight;
                    }
                }
            }
            
            // Normalize
            if (totalWeight > 0.0) {
                color /= totalWeight;
            }
            
            gl_FragColor = color;
        }
    """.trimIndent()
}