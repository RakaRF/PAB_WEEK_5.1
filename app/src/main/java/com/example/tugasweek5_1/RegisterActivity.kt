package com.example.tugasweek5_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    private lateinit var store: UserStore
    private lateinit var nameLayout: TextInputLayout
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        applySystemBarInsets(findViewById(R.id.main))

        store = UserStore(this)
        nameLayout = findViewById(R.id.etNameLayout)
        emailLayout = findViewById(R.id.etEmailLayout)
        passwordLayout = findViewById(R.id.etPasswordLayout)
        confirmLayout = findViewById(R.id.etConfirmLayout)

        findViewById<MaterialButton>(R.id.btnRegister).setOnClickListener { attemptRegister() }

        // Registrasi -> kembali ke Login
        findViewById<MaterialButton>(R.id.btnGoLogin).setOnClickListener { finish() }
    }

    private fun attemptRegister() {
        val nameOk = validateName()
        val emailOk = emailLayout.validateEmail(this)
        val passOk = passwordLayout.validatePassword(this)
        val confirmOk = confirmLayout.validateMatches(this, passwordLayout)
        if (!nameOk || !emailOk || !passOk || !confirmOk) return

        val success = store.register(nameLayout.value, emailLayout.value, passwordLayout.value)
        if (!success) {
            emailLayout.error = getString(R.string.register_email_taken)
            return
        }

        // Registrasi -> Login (membawa email agar terisi otomatis)
        setResult(
            RESULT_OK,
            Intent().putExtra(LoginActivity.EXTRA_EMAIL, emailLayout.value.trim())
        )
        finish()
    }

    private fun validateName(): Boolean {
        nameLayout.error =
            if (nameLayout.value.isBlank()) getString(R.string.error_name_empty) else null
        return nameLayout.error == null
    }
}
