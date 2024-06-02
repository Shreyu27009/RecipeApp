
package com.example.recipeapp
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.databinding.ActivityAddRecipeBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class AddRecipeActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var binding: ActivityAddRecipeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Get recipe data from UI


    /*    Log.d("AddRecipeActivity", "Link: $link")
        Log.d("AddRecipeActivity", "Recipe name: $recipeName")
        Log.d("AddRecipeActivity", "Recipe ingredients: $recipeIng")
        Log.d("AddRecipeActivity", "Recipe steps: $recipeSteps")
        Log.d("AddRecipeActivity", "Category: $cat")*/


        // Create a new recipe document in Firestore


        // Add recipe data to the document
        binding.addRecipe.setOnClickListener {
            val link = binding.recipeLink.text.toString()
            val recipeName = binding.recipename.text.toString()
            val recipeIng = binding.recipeIng1.text.toString()
            val recipeSteps = binding.recipeDes1.text.toString()
            val cat = binding.recipeCat1.text.toString()
            insertRecipes(link,recipeName,recipeIng,recipeSteps,cat)
            }

    }

    private fun insertRecipes(link:String,recipeName:String,recipeIng:String,recipeSteps:String,cat:String) {
        if(link.isEmpty() || recipeName.isEmpty() || recipeIng.isEmpty() || recipeSteps.isEmpty() || cat.isEmpty()) {
            Toast.makeText(this@AddRecipeActivity, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }
        val db=FirebaseFirestore.getInstance()
        val recipe:MutableMap<String,Any> = HashMap()
        recipe["link"]=link
        recipe["recipeName"]=recipeName
        recipe["recipeIng"]=recipeIng
        recipe["recipeSteps"]=recipeSteps
        recipe["cat"]=cat

        val recipeCollection = db.collection("recipe")
        
        db.collection("recipe")
            .add(recipe)
            .addOnSuccessListener {
                Toast.makeText(this@AddRecipeActivity, "Recipe added successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@AddRecipeActivity, HomeActivity::class.java))
            }
            .addOnFailureListener {
                Toast.makeText(this@AddRecipeActivity, "Error adding recipe", Toast.LENGTH_SHORT).show()
            }

    }
    fun readRecipe()
    {
        val db=FirebaseFirestore.getInstance()
        db.collection("recipe")
            .get()
            .addOnCanceledListener{
                val result:StringBuffer= StringBuffer()
                fun readRecipe(): String {
                    val db = FirebaseFirestore.getInstance()
                    var result = ""

                    db.collection("recipe")
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val querySnapshot = task.result
                                for (document in querySnapshot!!) {
                                    result += document.data.getValue("link").toString() + "\n\n"
                                    result += document.data.getValue("recipeName").toString() + "\n\n"
                                    result += document.data.getValue("recipeIng").toString() + "\n\n"
                                    result += document.data.getValue("recipeSteps").toString() + "\n\n"
                                    result += document.data.getValue("cat").toString() + "\n\n"
                                }
                            } else {
                                Log.d("ReadRecipe", "Error getting documents: ", task.exception)
                            }
                        }
                    return result
                }


                }
            }
    }

