package com.example.covid.vaccination

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class SplashScreen : AppCompatActivity() {

    lateinit var splashIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        splashIV = findViewById(R.id.splash_imageView)
        splashIV.alpha = 0f
        splashIV.animate().setDuration(2000).alpha(1f).withEndAction{
            val i = Intent(this ,MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }


    }
}