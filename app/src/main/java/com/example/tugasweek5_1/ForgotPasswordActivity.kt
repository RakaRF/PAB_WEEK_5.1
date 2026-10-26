package com.example.tugasweek5_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var store: UserStore
    private lateinit var emailLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        applySystemBarInsets(findViewById(R.id.main))

        store = UserStore(this)
        emailLayout = findViewById(R.id.etEmailLayout)

        // Isi otomatis dari email yang sudah diketik di halaman login
        emailLayout.editText?.setText(intent.getStringExtra(LoginActivity.EXTRA_EMAIL).orEmpty())

        findViewById<MaterialButton>(R.id.btnContinue).setOnClickListener { attemptContinue() }

        // Lupa password -> kembali ke Login
        findViewById<MaterialButton>(R.id.btnBackToLogin).setOnClickListener { finish() }
    }

    private fun attemptContinue() {
        if (!emailLayout.validateEmail(this)) return

        if (!store.emailExists(emailLayout.value)) {
            emailLayout.error = getString(R.string.forgot_email_not_found)
            return
        }

        // Lupa password -> Reset password
        // (Aplikasi nyata biasanya mengirim kode/tautan ke email dulu; di sini disederhanakan.)
        startActivity(
            Intent(this, ResetPasswordActivity::class.java)
                .putExtra(LoginActivity.EXTRA_EMAIL, emailLayout.value.trim())
        )
    }
}
