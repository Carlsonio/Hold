package com.example.hold.di

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton

// Этот класс отвечает за создание, хранение и извлечение ключа от БД
@Singleton
class CryptoManager @Inject constructor(@ApplicationContext context: Context) {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
    private val prefs: SharedPreferences = context.getSharedPreferences("secure_prefs", Context.MODE_PRIVATE)

    private val KEY_ALIAS = "db_passphrase_key"
    private val PREFS_ENCRYPTED_PASSPHRASE = "encrypted_passphrase"
    private val PREFS_ENCRYPTION_IV = "encryption_iv"

    init {
        // Если ключ еще не создан, генерируем его
        if (!prefs.contains(PREFS_ENCRYPTED_PASSPHRASE)) {
            generateAndStoreEncryptedPassphrase()
        }
    }

    fun getDatabasePassphrase(): ByteArray {
        val encryptedPassphrase = android.util.Base64.decode(prefs.getString(PREFS_ENCRYPTED_PASSPHRASE, ""), android.util.Base64.DEFAULT)
        val iv = android.util.Base64.decode(prefs.getString(PREFS_ENCRYPTION_IV, ""), android.util.Base64.DEFAULT)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        return cipher.doFinal(encryptedPassphrase)
    }

    private fun generateAndStoreEncryptedPassphrase() {
        // 1. Генерируем случайный пароль для SQLCipher
        val passphrase = ByteArray(32).apply { SecureRandom().nextBytes(this) }

        // 2. Получаем или создаем ключ в Android Keystore
        val secretKey = getOrCreateSecretKey()

        // 3. Шифруем пароль с помощью ключа из Keystore
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedPassphrase = cipher.doFinal(passphrase)
        val iv = cipher.iv

        // 4. Сохраняем зашифрованную версию и вектор инициализации в SharedPreferences
        prefs.edit {
            putString(PREFS_ENCRYPTED_PASSPHRASE, android.util.Base64.encodeToString(encryptedPassphrase, android.util.Base64.DEFAULT))
            putString(PREFS_ENCRYPTION_IV, android.util.Base64.encodeToString(iv, android.util.Base64.DEFAULT))
        }

        // Очищаем оригинальный пароль из памяти
        passphrase.fill(0)
    }

    private fun getSecretKey(): SecretKey {
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    private fun getOrCreateSecretKey(): SecretKey {
        val existingKey = keyStore.getEntry(KEY_ALIAS, null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: generateSecretKey()
    }

    private fun generateSecretKey(): SecretKey {
        val spec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(spec)
        }.generateKey()
    }
}
