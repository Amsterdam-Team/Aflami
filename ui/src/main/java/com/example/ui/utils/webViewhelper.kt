package com.example.ui.utils

import android.webkit.WebView

private fun buildHeaderCheckScript(text: String): String {
    return """
        (function() {
            var headers = document.querySelectorAll('h2');
            for (var i = 0; i < headers.length; i++) {
                if (headers[i].innerText && headers[i].innerText.trim().includes('$text')) {
                    return true;
                }
            }
            return false;
        })();
    """.trimIndent()
}

fun checkLoginTitle(webView: WebView, onFound: () -> Unit) {
    val script = buildHeaderCheckScript("Login to your account")
    webView.evaluateJavascript(script) { result ->
        if (result == "true") {
            onFound()
        }
    }
}