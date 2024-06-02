package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.recipeapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private val binding:ActivityLoginBinding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var auth:FirebaseAuth
    override fun onStart() {
        super.onStart()
        //check if user already logged in
        val currentuser: FirebaseUser? =auth.currentUser
        if(currentuser!= null)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener{
            val username=binding.username.text.toString()
            val password=binding.password.text.toString()
            if(username.isEmpty()||password.isEmpty())
            {
                Toast.makeText(this,"Please enter all the details!",Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth.signInWithEmailAndPassword(username,password)

                    .addOnCompleteListener { task ->
                        if(task.isSuccessful)
                        {
                            Toast.makeText(this,"Sign-In Successful",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this,"Sign-In failed:${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
        binding.signupBtn.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }
}