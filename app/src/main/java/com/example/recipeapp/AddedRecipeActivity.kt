package com.example.recipeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class AddedRecipeActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_added_recipe)
        db.collection("recipes")
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                querySnapshot.documents.forEach { document ->
                    val recipeTitle = document["title"] as String
                    val recipeImage = document["image"] as String // Retrieve the recipe image
                    println("Recipe title: $recipeTitle, Recipe image: $recipeImage")
                }
            }
            .addOnFailureListener { e ->
                println("Error getting documents: $e")
            }
    }
}