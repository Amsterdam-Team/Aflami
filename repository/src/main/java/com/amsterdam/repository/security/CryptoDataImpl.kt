package com.amsterdam.repository.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class CryptoDataImpl @Inject constructor() : CryptoData {
    private val cipher = Cipher.getInstance(TRANSFORMATION)
    private val keyStore =
        KeyStore.getInstance(KEYSTORE_KEY).apply {
            load(null)
        }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"

        private const val KEYSTORE_KEY = "AndroidKeyStore"
        private const val KEYSTORE_ALIAS_KEY = "secret"
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEYSTORE_ALIAS_KEY, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey =
        KeyGenerator
            .getInstance(ALGORITHM)
            .apply {
                init(
                    KeyGenParameterSpec
                        .Builder(
                            KEYSTORE_ALIAS_KEY,
                            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT,
                        ).setBlockModes(BLOCK_MODE)
                        .setEncryptionPaddings(PADDING)
                        .build(),
                )
            }.generateKey()

    override fun encrypt(bytes: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, getKey())
        val iv = cipher.iv
        val encrypted = cipher.doFinal(bytes)
        return iv + encrypted
    }

    override fun decrypt(bytes: ByteArray): ByteArray? {
        val iv = bytes.copyOfRange(0, cipher.blockSize)
        val data = bytes.copyOfRange(cipher.blockSize, bytes.size)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), IvParameterSpec(iv))
        return cipher.doFinal(data)
    }

    override fun encryptString(input: String): String {
        val encryptedBytes = encrypt(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    override fun decryptString(input: String): String? =
        try {
            val decodedBytes = Base64.decode(input, Base64.DEFAULT)
            val decryptedBytes = decrypt(decodedBytes)
            decryptedBytes?.toString(Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}
