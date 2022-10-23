package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportActionBar?.hide() //hide actionbar

        auth = Firebase.auth

        val btnForgotPassBack = findViewById<MaterialButton>(R.id.btnForgotPassBack)
        val btnForgotPass = findViewById<Button>(R.id.btnForgotPass)
        val txtForgotPassEmail = findViewById<TextInputEditText>(R.id.txtForgotPassEmail)

        btnForgotPass.setOnClickListener {
            if (txtForgotPassEmail.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter all fields.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.sendPasswordResetEmail(txtForgotPassEmail.text.toString())
                Toast.makeText(this, "Check your email to change your password.", Toast.LENGTH_LONG)
                    .show()
                txtForgotPassEmail.text?.clear()
            }
        }

        btnForgotPassBack.setOnClickListener{
            finish();
            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
        }
    }

    override fun onBackPressed() {
        finish()
        startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
    }
}