package com.example.ui.screens.register.components

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.utils.checkLoginTitle


@Composable
fun RegisterWebView(
    modifier: Modifier = Modifier,
    urlToLoad: String,
    onLoadingStateChanged: (isLoading: Boolean) -> Unit,
    onRegistrationComplete: () -> Unit,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        onLoadingStateChanged(true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onLoadingStateChanged(false)

                        view?.let {
                            checkLoginTitle(it, onFound = onRegistrationComplete)
                        }
                    }
                }


                loadUrl(urlToLoad)
            }
        }
    )
}