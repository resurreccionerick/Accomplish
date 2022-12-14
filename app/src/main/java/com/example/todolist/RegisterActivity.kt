package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide() //hide actionbar

        auth = Firebase.auth

        val email = findViewById<EditText>(R.id.txtRegisterEmail)
        val pass = findViewById<EditText>(R.id.txtRegisterPass)
        val btnRegister = findViewById<Button>(R.id.btnSignup)
        val btnRegisterBack = findViewById<MaterialButton>(R.id.btnRegisterBack)

        btnRegister.setOnClickListener {
            if (txtRegisterEmail.text.toString().isEmpty() || txtRegisterPass.text.toString()
                    .isEmpty()
            ) {
                Toast.makeText(baseContext, "Please enter all fields.", Toast.LENGTH_SHORT)
                    .show()
            } else if (txtRegisterPass.text!!.length < 6) {
                Toast.makeText(baseContext, "Please enter 6 digit password.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.createUserWithEmailAndPassword(email.text.toString(), pass.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                            Toast.makeText(
                                baseContext,
                                "Registration Successful.",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
                            Toast.makeText(baseContext, "Registration failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }
        }

        btnRegisterBack.setOnClickListener {
            finish();
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    override fun onBackPressed() {
        finish();
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
    }

}