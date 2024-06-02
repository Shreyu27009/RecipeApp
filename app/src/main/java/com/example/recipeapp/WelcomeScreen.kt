package com.example.recipeapp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.example.recipeapp.databinding.ActivityWelcomeScreenBinding

class WelcomeScreen : AppCompatActivity() {
    private val binding:ActivityWelcomeScreenBinding by lazy{
        ActivityWelcomeScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            },5000
        )
        val welcomeText="Welcome!"
        val spannableString=SpannableString(welcomeText)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FF0000")),0,5,0)
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#312222")),5,welcomeText.length,0)
        binding.welcomeTxt.text=spannableString
    }
}