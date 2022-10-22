package com.example.todolist

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val splashLogo = findViewById<ImageView>(R.id.splash_logo)

        //Glide.with(this).asGif().load(R.raw.splash_logo).into(splashLogo);

        val anim = AnimationUtils.loadAnimation(this, R.anim.rotate)

        splashLogo.animation = anim


        Thread {
            try {
                Thread.sleep(1000) //sleep 3 second to load data and open login activity
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