package com.example.recipeapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room

class RecipeModel(application: Application) : AndroidViewModel(application) {
    private val daoObject: Dao
    val allRecipes: LiveData<List<Recipe>>

    init {
        val db = Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, "db_name"
        ).fallbackToDestructiveMigration().createFromAsset("recipe.db").build()
        daoObject = db.getDao()
        allRecipes = daoObject.getAllRecipes()
    }

    suspend fun insertRecipe(recipe: Recipe) {
        daoObject.insertRecipe(recipe)
    }
}
