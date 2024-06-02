package com.example.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.recipeapp.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private val binding:ActivitySignUpBinding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private lateinit var  auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth= FirebaseAuth.getInstance()

        binding.signInBtn.setOnClickListener{
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }


        binding.registerbtn.setOnClickListener{
            val email=binding.email.text.toString()
            val username=binding.username.text.toString()
            val password=binding.password.text.toString()
            val confirmpass=binding.confirmpassword.text.toString()

            //check if any field is empty
            if(email.isEmpty()||username.isEmpty()||password.isEmpty()||confirmpass.isEmpty())
            {
                Toast.makeText(this,"please fill all the details",Toast.LENGTH_SHORT).show()
            }
            else if (password!=confirmpass)
            {
                Toast.makeText(this, "password must be equal", Toast.LENGTH_SHORT).show()
            }
            else
            {
                auth.createUserWithEmailAndPassword(email,password)

                    .addOnCompleteListener(this) {task->
                        if(task.isSuccessful){
                            Toast.makeText(this,"Registration Successful",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,LoginActivity::class.java))
                            finish()
                        }
                        else
                        {
                            Toast.makeText(this,"please try again!:${task.exception?.message}",Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
        }
    }
