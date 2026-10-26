package com.example.tugasweek5_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var store: UserStore
    private lateinit var email: String
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmLayout: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        email = intent.getStringExtra(LoginActivity.EXTRA_EMAIL).orEmpty()
        if (email.isEmpty()) { // dibuka tanpa email -> tidak valid
            finish()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_reset_password)
        applySystemBarInsets(findViewById(R.id.main))

        store = UserStore(this)
        passwordLayout = findViewById(R.id.etPasswordLayout)
        confirmLayout = findViewById(R.id.etConfirmLayout)

        findViewById<TextView>(R.id.tvResetSubtitle).text = getString(R.string.reset_subtitle, email)
        findViewById<MaterialButton>(R.id.btnSave).setOnClickListener { attemptSave() }
    }

    private fun attemptSave() {
        val passOk = passwordLayout.validatePassword(this)
        val confirmOk = confirmLayout.validateMatches(this, passwordLayout)
        if (!passOk || !confirmOk) return

        store.resetPassword(email, passwordLayout.value)
        Toast.makeText(this, R.string.reset_success, Toast.LENGTH_SHORT).show()

        // Reset password -> Login (tumpukan Forgot & Reset dibersihkan)
        startActivity(
            Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra(LoginActivity.EXTRA_EMAIL, email)
        )
        finish()
    }
}
