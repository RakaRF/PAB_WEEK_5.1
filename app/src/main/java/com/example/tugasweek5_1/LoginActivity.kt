package com.example.tugasweek5_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var store: UserStore
    private lateinit var emailLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout

    // Menerima hasil dari halaman registrasi (email baru) untuk diisi otomatis.
    private val registerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                emailLayout.editText?.setText(result.data?.getStringExtra(EXTRA_EMAIL).orEmpty())
                passwordLayout.editText?.text = null
                passwordLayout.requestFocus()
                Toast.makeText(this, R.string.login_register_success, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = UserStore(this)

        // Jika sudah login sebelumnya, langsung ke beranda.
        if (store.currentEmail() != null) {
            goToHome()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        applySystemBarInsets(findViewById(R.id.main))

        emailLayout = findViewById(R.id.etEmailLayout)
        passwordLayout = findViewById(R.id.etPasswordLayout)

        findViewById<MaterialButton>(R.id.btnLogin).setOnClickListener { attemptLogin() }

        // Login -> Registrasi
        findViewById<MaterialButton>(R.id.btnGoRegister).setOnClickListener {
            registerLauncher.launch(Intent(this, RegisterActivity::class.java))
        }

        // Login -> Lupa password
        findViewById<MaterialButton>(R.id.btnForgot).setOnClickListener {
            startActivity(
                Intent(this, ForgotPasswordActivity::class.java)
                    .putExtra(EXTRA_EMAIL, emailLayout.value.trim())
            )
        }
    }

    // Dipanggil saat kembali dari reset password (FLAG_ACTIVITY_CLEAR_TOP | SINGLE_TOP).
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (::emailLayout.isInitialized) {
            emailLayout.editText?.setText(intent.getStringExtra(EXTRA_EMAIL).orEmpty())
            passwordLayout.editText?.text = null
            passwordLayout.error = null
        }
    }

    private fun attemptLogin() {
        val emailOk = emailLayout.validateEmail(this)
        val passOk = passwordLayout.validatePassword(this)
        if (!emailOk || !passOk) return

        if (store.login(emailLayout.value, passwordLayout.value)) {
            // Login -> Beranda
            goToHome()
        } else {
            passwordLayout.error = getString(R.string.login_failed)
        }
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish() // supaya tombol Back dari beranda tidak kembali ke login
    }

    companion object {
        const val EXTRA_EMAIL = "extra_email"
    }
}
