package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = Firebase.auth

        val toolbar = findViewById<Toolbar>(R.id.toolbar_forgotPass)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

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
    }

    override fun onBackPressed() {
        finish();
        startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { //this is for back button
        if (item.itemId == android.R.id.home) {
            finish();
            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}