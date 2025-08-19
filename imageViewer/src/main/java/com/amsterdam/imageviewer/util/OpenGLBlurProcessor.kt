package com.amsterdam.imageviewer.util

import android.graphics.Bitmap
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLContext
import android.opengl.EGLDisplay
import android.opengl.EGLSurface
import android.opengl.GLES20
import android.opengl.GLUtils
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import androidx.core.graphics.createBitmap

internal class OpenGLBlurProcessor {

    companion object {
        fun blurBitmap(inputBitmap: Bitmap, radius: Float): Bitmap? {
            val processor = OpenGLBlurProcessor()
            return processor.processBlur(inputBitmap, radius)
        }
    }



    private var eglDisplay: EGLDisplay? = null
    private var eglContext: EGLContext? = null
    private var eglSurface: EGLSurface? = null

    private var horizontalShaderProgram = 0
    private var verticalShaderProgram = 0

    private var framebuffer1 = 0
    private var framebuffer2 = 0
    private var fboTexture1 = 0
    private var fboTexture2 = 0

    private var quadVBO = 0
    private lateinit var vertexBuffer: FloatBuffer

    private var currentWidth = 0
    private var currentHeight = 0
    private var initialized = false





    private fun processBlur(inputBitmap: Bitmap, radius: Float): Bitmap? {
        return try {
            if (!setupEGL(inputBitmap.width, inputBitmap.height)) {
                return null
            }

            initialize()
            val result = performTwoPassBlur(inputBitmap, radius)
            cleanup()
            result
        } catch (e: Exception) {
            e.printStackTrace()
            cleanup()
            null
        }
    }

    private fun initialize() {
        if (initialized) return

        compileShaders()
        setupFullscreenQuad()
        setupFramebuffers()

        initialized = true
    }

    private fun setupEGL(width: Int, height: Int): Boolean {
        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        if (eglDisplay == EGL14.EGL_NO_DISPLAY) {
            return false
        }

        val version = IntArray(2)
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            return false
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

        if (!EGL14.eglChooseConfig(eglDisplay, configAttribs, 0, configs, 0, configs.size, numConfigs, 0)) {
            cleanup()
            return false
        }

        val config = configs[0] ?: return false
        val contextAttribs = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE
        )

        eglContext = EGL14.eglCreateContext(eglDisplay, config, EGL14.EGL_NO_CONTEXT, contextAttribs, 0)
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            cleanup()
            return false
        }

        val surfaceAttribs = intArrayOf(
            EGL14.EGL_WIDTH, width,
            EGL14.EGL_HEIGHT, height,
            EGL14.EGL_NONE
        )

        eglSurface = EGL14.eglCreatePbufferSurface(eglDisplay, config, surfaceAttribs, 0)
        if (eglSurface == EGL14.EGL_NO_SURFACE) {
            cleanup()
            return false
        }

        return EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)
    }

    private fun performTwoPassBlur(inputBitmap: Bitmap, radius: Float): Bitmap? {
        val width = inputBitmap.width
        val height = inputBitmap.height

        val inputTextureId = uploadBitmapAsTexture(inputBitmap)
        if (inputTextureId == 0) return null

        if (radius <= 0.5f) {
            return renderDirect(inputTextureId, width, height)
        }

        resizeFramebuffers(width, height)

        renderHorizontalPass(inputTextureId, width, height, radius)

        renderVerticalPass(width, height, radius)

        val result = readFBO(width, height)

        GLES20.glDeleteTextures(1, intArrayOf(inputTextureId), 0)

        return result
    }

    private fun uploadBitmapAsTexture(bitmap: Bitmap): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] == 0) return 0

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        return textureHandle[0]
    }

    private fun renderHorizontalPass(textureId: Int, width: Int, height: Int, radius: Float) {
        GLES20.glUseProgram(horizontalShaderProgram)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer1)
        GLES20.glViewport(0, 0, width, height)

        GLES20.glUniform1f(GLES20.glGetUniformLocation(horizontalShaderProgram, "uRadius"), radius)
        GLES20.glUniform2f(GLES20.glGetUniformLocation(horizontalShaderProgram, "uTextureSize"),
            width.toFloat(), height.toFloat())

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(GLES20.glGetUniformLocation(horizontalShaderProgram, "uTexture"), 0)

        drawQuad(horizontalShaderProgram)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    private fun renderVerticalPass(width: Int, height: Int, radius: Float) {
        GLES20.glUseProgram(verticalShaderProgram)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer2)
        GLES20.glViewport(0, 0, width, height)

        GLES20.glUniform1f(GLES20.glGetUniformLocation(verticalShaderProgram, "uRadius"), radius)
        GLES20.glUniform2f(GLES20.glGetUniformLocation(verticalShaderProgram, "uTextureSize"),
            width.toFloat(), height.toFloat())

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTexture1)
        GLES20.glUniform1i(GLES20.glGetUniformLocation(verticalShaderProgram, "uTexture"), 0)

        drawQuad(verticalShaderProgram)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    private fun renderDirect(textureId: Int, width: Int, height: Int): Bitmap {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer2)
        GLES20.glViewport(0, 0, width, height)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        GLES20.glUseProgram(verticalShaderProgram)
        GLES20.glUniform1f(GLES20.glGetUniformLocation(verticalShaderProgram, "uRadius"), 0.0f)
        GLES20.glUniform2f(GLES20.glGetUniformLocation(verticalShaderProgram, "uTextureSize"),
            width.toFloat(), height.toFloat())

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(GLES20.glGetUniformLocation(verticalShaderProgram, "uTexture"), 0)

        drawQuad(verticalShaderProgram)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

        return readFBO(width, height)
    }

    private fun drawQuad(shaderProgram: Int) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, quadVBO)

        val posLoc = GLES20.glGetAttribLocation(shaderProgram, "aPosition")
        val texLoc = GLES20.glGetAttribLocation(shaderProgram, "aTexCoord")

        GLES20.glEnableVertexAttribArray(posLoc)
        GLES20.glVertexAttribPointer(posLoc, 2, GLES20.GL_FLOAT, false, 4 * 4, 0)

        GLES20.glEnableVertexAttribArray(texLoc)
        GLES20.glVertexAttribPointer(texLoc, 2, GLES20.GL_FLOAT, false, 4 * 4, 2 * 4)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glDisableVertexAttribArray(posLoc)
        GLES20.glDisableVertexAttribArray(texLoc)
    }

    private fun readFBO(width: Int, height: Int): Bitmap {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer2)

        val pixelBuffer = ByteBuffer.allocateDirect(width * height * 4)
        pixelBuffer.order(ByteOrder.nativeOrder())

        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

        pixelBuffer.rewind()
        val tempBitmap = createBitmap(width, height)
        tempBitmap.copyPixelsFromBuffer(pixelBuffer)

        val outputBitmap = Bitmap.createBitmap(
            tempBitmap, 0, 0, width, height
        )

        tempBitmap.recycle()

        return outputBitmap
    }

    private fun setupFullscreenQuad() {
        val quadVertices = floatArrayOf(
            -1.0f,  1.0f,   0.0f, 1.0f,
            -1.0f, -1.0f,   0.0f, 0.0f,
            1.0f,  1.0f,   1.0f, 1.0f,
            1.0f, -1.0f,   1.0f, 0.0f
        )

        vertexBuffer = ByteBuffer.allocateDirect(quadVertices.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(quadVertices)
                position(0)
            }

        val vboArray = IntArray(1)
        GLES20.glGenBuffers(1, vboArray, 0)
        quadVBO = vboArray[0]

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, quadVBO)
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, quadVertices.size * 4, vertexBuffer, GLES20.GL_STATIC_DRAW)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
    }

    private fun setupFramebuffers() {
        val framebuffers = IntArray(2)
        GLES20.glGenFramebuffers(2, framebuffers, 0)
        framebuffer1 = framebuffers[0]
        framebuffer2 = framebuffers[1]

        val textures = IntArray(2)
        GLES20.glGenTextures(2, textures, 0)
        fboTexture1 = textures[0]
        fboTexture2 = textures[1]

        currentWidth = 0
        currentHeight = 0
    }

    private fun resizeFramebuffers(width: Int, height: Int) {
        if (currentWidth == width && currentHeight == height) {
            return
        }

        currentWidth = width
        currentHeight = height

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer1)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTexture1)
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
            GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, fboTexture1, 0)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, framebuffer2)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fboTexture2)
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0,
            GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
            GLES20.GL_TEXTURE_2D, fboTexture2, 0)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    private fun compileShaders() {
        val vertexShaderSrc = """
            attribute vec2 aPosition;
            attribute vec2 aTexCoord;
            varying vec2 vTexCoord;
            void main() {
                vTexCoord = aTexCoord;
                gl_Position = vec4(aPosition, 0.0, 1.0);
            }
        """.trimIndent()

        val horizontalFragmentShaderSrc = """
            precision mediump float;
            varying vec2 vTexCoord;
            uniform sampler2D uTexture;
            uniform float uRadius;
            uniform vec2 uTextureSize;

            void main() {
                vec2 texelSize = 1.0 / uTextureSize;
                vec4 color = vec4(0.0);

                if (uRadius <= 0.5) {
                    gl_FragColor = texture2D(uTexture, vTexCoord);
                    return;
                }

                float sigma = uRadius / 2.0;
                float twoSigmaSq = 2.0 * sigma * sigma;
                float totalWeight = 0.0;
                int iRadius = int(ceil(uRadius));

                // Sample along horizontal axis only
                for (int x = -iRadius; x <= iRadius; ++x) {
                    float distance = abs(float(x));
                    if (distance <= uRadius) {
                        vec2 sampleCoord = vTexCoord + vec2(float(x) * texelSize.x, 0.0);
                        sampleCoord = clamp(sampleCoord, vec2(0.0), vec2(1.0));

                        float weight = exp(-(distance * distance) / twoSigmaSq);
                        color += texture2D(uTexture, sampleCoord) * weight;
                        totalWeight += weight;
                    }
                }

                gl_FragColor = color / totalWeight;
            }
        """.trimIndent()

        val verticalFragmentShaderSrc = """
            precision mediump float;
            varying vec2 vTexCoord;
            uniform sampler2D uTexture;
            uniform float uRadius;
            uniform vec2 uTextureSize;

            void main() {
                vec2 texelSize = 1.0 / uTextureSize;
                vec4 color = vec4(0.0);

                if (uRadius <= 0.5) {
                    gl_FragColor = texture2D(uTexture, vTexCoord);
                    return;
                }

                float sigma = uRadius / 2.0;
                float twoSigmaSq = 2.0 * sigma * sigma;
                float totalWeight = 0.0;
                int iRadius = int(ceil(uRadius));

                // Sample along vertical axis only
                for (int y = -iRadius; y <= iRadius; ++y) {
                    float distance = abs(float(y));
                    if (distance <= uRadius) {
                        vec2 sampleCoord = vTexCoord + vec2(0.0, float(y) * texelSize.y);
                        sampleCoord = clamp(sampleCoord, vec2(0.0), vec2(1.0));

                        float weight = exp(-(distance * distance) / twoSigmaSq);
                        color += texture2D(uTexture, sampleCoord) * weight;
                        totalWeight += weight;
                    }
                }

                gl_FragColor = color / totalWeight;
            }
        """.trimIndent()

        val vertexShader = compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderSrc)
        val horizontalFragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, horizontalFragmentShaderSrc)
        val verticalFragmentShader = compileShader(GLES20.GL_FRAGMENT_SHADER, verticalFragmentShaderSrc)

        horizontalShaderProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(horizontalShaderProgram, vertexShader)
        GLES20.glAttachShader(horizontalShaderProgram, horizontalFragmentShader)
        GLES20.glLinkProgram(horizontalShaderProgram)

        verticalShaderProgram = GLES20.glCreateProgram()
        GLES20.glAttachShader(verticalShaderProgram, vertexShader)
        GLES20.glAttachShader(verticalShaderProgram, verticalFragmentShader)
        GLES20.glLinkProgram(verticalShaderProgram)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(horizontalShaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            val log = GLES20.glGetProgramInfoLog(horizontalShaderProgram)
            throw RuntimeException("Horizontal program linking failed: $log")
        }

        GLES20.glGetProgramiv(verticalShaderProgram, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            val log = GLES20.glGetProgramInfoLog(verticalShaderProgram)
            throw RuntimeException("Vertical program linking failed: $log")
        }

        GLES20.glDeleteShader(vertexShader)
        GLES20.glDeleteShader(horizontalFragmentShader)
        GLES20.glDeleteShader(verticalFragmentShader)
    }

    private fun compileShader(type: Int, source: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, source)
        GLES20.glCompileShader(shader)

        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)
        if (compileStatus[0] != GLES20.GL_TRUE) {
            val log = GLES20.glGetShaderInfoLog(shader)
            GLES20.glDeleteShader(shader)
            throw RuntimeException("Shader compilation failed: $log")
        }

        return shader
    }

    private fun cleanup() {
        if (quadVBO != 0) {
            GLES20.glDeleteBuffers(1, intArrayOf(quadVBO), 0)
            quadVBO = 0
        }

        if (horizontalShaderProgram != 0) {
            GLES20.glDeleteProgram(horizontalShaderProgram)
            horizontalShaderProgram = 0
        }

        if (verticalShaderProgram != 0) {
            GLES20.glDeleteProgram(verticalShaderProgram)
            verticalShaderProgram = 0
        }

        if (framebuffer1 != 0 || framebuffer2 != 0) {
            GLES20.glDeleteFramebuffers(2, intArrayOf(framebuffer1, framebuffer2), 0)
            framebuffer1 = 0
            framebuffer2 = 0
        }

        if (fboTexture1 != 0 || fboTexture2 != 0) {
            GLES20.glDeleteTextures(2, intArrayOf(fboTexture1, fboTexture2), 0)
            fboTexture1 = 0
            fboTexture2 = 0
        }

        eglDisplay?.let { display ->
            EGL14.eglMakeCurrent(display, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
            eglSurface?.let { EGL14.eglDestroySurface(display, it) }
            eglContext?.let { EGL14.eglDestroyContext(display, it) }
            EGL14.eglTerminate(display)
        }

        eglDisplay = null
        eglContext = null
        eglSurface = null
        initialized = false
    }


}