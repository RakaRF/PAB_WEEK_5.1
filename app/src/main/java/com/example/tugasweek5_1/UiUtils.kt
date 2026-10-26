package com.example.tugasweek5_1

import android.app.Activity
import android.util.Patterns
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

/** Memberi padding sesuai system bar agar konten tidak tertutup (edge-to-edge). */
fun Activity.applySystemBarInsets(root: View) {
    ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
        insets
    }
}

/** Ambil teks dari TextInputLayout, kosong jika belum diisi. */
val TextInputLayout.value: String
    get() = editText?.text?.toString().orEmpty()

/** Validasi email. @return true jika valid; jika tidak, error ditampilkan di layout. */
fun TextInputLayout.validateEmail(activity: Activity): Boolean {
    val text = value.trim()
    error = when {
        text.isEmpty() -> activity.getString(R.string.error_email_empty)
        !Patterns.EMAIL_ADDRESS.matcher(text).matches() -> activity.getString(R.string.error_email_invalid)
        else -> null
    }
    return error == null
}

/** Validasi password (wajib diisi, minimal 6 karakter). */
fun TextInputLayout.validatePassword(activity: Activity, minLength: Int = 6): Boolean {
    val text = value
    error = when {
        text.isEmpty() -> activity.getString(R.string.error_password_empty)
        text.length < minLength -> activity.getString(R.string.error_password_short)
        else -> null
    }
    return error == null
}

/** Validasi konfirmasi password harus sama dengan [other]. */
fun TextInputLayout.validateMatches(activity: Activity, other: TextInputLayout): Boolean {
    error = if (value != other.value) activity.getString(R.string.error_password_mismatch) else null
    return error == null
}
