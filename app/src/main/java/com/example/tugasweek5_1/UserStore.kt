package com.example.tugasweek5_1

import android.content.Context
import java.security.MessageDigest

/**
 * Penyimpanan akun sederhana berbasis SharedPreferences (hanya untuk keperluan tugas).
 * Password disimpan dalam bentuk hash SHA-256, bukan teks asli.
 */
class UserStore(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("user_store", Context.MODE_PRIVATE)

    private fun normalize(email: String) = email.trim().lowercase()
    private fun nameKey(email: String) = "name_${normalize(email)}"
    private fun passKey(email: String) = "pass_${normalize(email)}"

    private fun hash(password: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
            .joinToString("") { "%02x".format(it) }

    fun emailExists(email: String) = prefs.contains(passKey(email))

    /** @return false jika email sudah terdaftar. */
    fun register(name: String, email: String, password: String): Boolean {
        if (emailExists(email)) return false
        prefs.edit()
            .putString(nameKey(email), name.trim())
            .putString(passKey(email), hash(password))
            .apply()
        return true
    }

    fun login(email: String, password: String): Boolean {
        val saved = prefs.getString(passKey(email), null) ?: return false
        if (saved != hash(password)) return false
        prefs.edit().putString(KEY_SESSION, normalize(email)).apply()
        return true
    }

    fun resetPassword(email: String, newPassword: String) {
        if (!emailExists(email)) return
        prefs.edit().putString(passKey(email), hash(newPassword)).apply()
    }

    fun getName(email: String): String = prefs.getString(nameKey(email), "") ?: ""

    fun currentEmail(): String? = prefs.getString(KEY_SESSION, null)

    fun logout() {
        prefs.edit().remove(KEY_SESSION).apply()
    }

    private companion object {
        const val KEY_SESSION = "session_email"
    }
}
