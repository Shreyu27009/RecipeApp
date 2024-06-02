package com.example.recipeapp

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.example.recipeapp.databinding.ActivityAboutUsBinding
import com.example.recipeapp.databinding.ActivityAddRecipeBinding

class AboutUsActivity : AppCompatActivity() {
    private val binding: ActivityAboutUsBinding by lazy{
        ActivityAboutUsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.more1.setOnClickListener{
            Log.d("PrivacyPolicyActivity", "more1 clicked")
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }

    }
}