package com.amsterdam.repository.security

interface CryptoManager {
    fun encrypt(bytes: ByteArray): ByteArray

    fun decrypt(bytes: ByteArray): ByteArray?
}
