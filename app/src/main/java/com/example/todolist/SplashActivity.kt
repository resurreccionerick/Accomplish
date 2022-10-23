package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide() //hide actionbar

        val splashLogo = findViewById<ImageView>(R.id.splash_logo)

        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate)

        splashLogo.animation = anim

        Thread {
            try {
                Thread.sleep(3000) //sleep 3 second to load data and open login activity
                startActivity(
                    Intent(
                        this@SplashActivity,
                        LoginActivity::class.java
                    )
                ) //start the loginActivity after fetching subjects data
                finish()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }
}