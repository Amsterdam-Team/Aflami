package com.amsterdam.ui.screens.register.components

import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.amsterdam.ui.utils.buildFormExistenceCheckScript
import com.amsterdam.ui.utils.createFormDetector


@Composable
fun RegisterWebView(
    modifier: Modifier = Modifier,
    urlToLoad: String,
    onLoadingStateChanged: (isLoading: Boolean) -> Unit,
    onRegistrationComplete: () -> Unit,
) {
    val registrationDetector = remember(onRegistrationComplete) {
        createFormDetector(onRegistrationComplete)
    }
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {

                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onLoadingStateChanged(false)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)

                        view ?: return
                        val script = buildFormExistenceCheckScript()
                        view.evaluateJavascript(script) { result ->
                            val isFormPresent = result?.toBoolean() ?: false
                            registrationDetector(isFormPresent)
                        }
                    }
                    override fun onReceivedError(
                        view: WebView?, request: WebResourceRequest?, error: WebResourceError?
                    ) {
                        super.onReceivedError(view, request, error)
                        onLoadingStateChanged(false)
                    }
                }

                onLoadingStateChanged(true)
                loadUrl(urlToLoad)
            }
        }
    )
}