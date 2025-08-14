package com.amsterdam.repository.utils

import android.util.Base64
import com.amsterdam.repository.security.CryptoManager

fun CryptoManager.encryptString(input: String): String {
    val encryptedBytes = encrypt(input.toByteArray(Charsets.UTF_8))
    return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
}

fun CryptoManager.decryptString(input: String): String? =
    try {
        val decodedBytes = Base64.decode(input, Base64.DEFAULT)
        val decryptedBytes = decrypt(decodedBytes)
        decryptedBytes?.toString(Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }