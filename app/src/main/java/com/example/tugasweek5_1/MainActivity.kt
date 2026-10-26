package com.example.tugasweek5_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

/** Halaman beranda, ditampilkan setelah login berhasil. */
class MainActivity : AppCompatActivity() {

    private lateinit var store: UserStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = UserStore(this)

        val email = store.currentEmail()
        if (email == null) { // belum login -> kembali ke login
            goToLogin()
            return
        }

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        applySystemBarInsets(findViewById(R.id.main))

        val name = store.getName(email).ifBlank { email }
        findViewById<TextView>(R.id.tvGreeting).text = getString(R.string.home_greeting, name)
        findViewById<TextView>(R.id.tvEmail).text = email

        // Beranda -> Login (logout)
        findViewById<MaterialButton>(R.id.btnLogout).setOnClickListener {
            store.logout()
            goToLogin()
        }
    }

    private fun goToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }
}
