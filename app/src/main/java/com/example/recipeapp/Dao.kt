package com.example.recipeapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
public interface Dao{
    @Query("SELECT * FROM recipe" )
    fun getAll():List<Recipe?>

    @Query("SELECT * FROM recipe WHERE ing LIKE:ing")
    fun findRecipe(ing:String):List<Recipe?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertRecipe(recipe: Recipe)

    @Update
    fun updateRecipe(recipe: Recipe)

    @Delete
    fun deleteRecipe(recipe: Recipe)

    @Query("SELECT * FROM recipe ORDER BY tittle")
    fun getAllRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE ing LIKE:searchQuery OR des LIKE:searchQuery OR category LIKE:searchQuery")
    fun searchDatabase(searchQuery:String):List<Recipe>

    @Query("SELECT * FROM recipe ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomRecipe(): Recipe?




    /*companion object {
        fun findRecipe(ing: String): List<Recipe?> {
            return com.example.recipeapp.Dao.findRecipe(ing)
        }
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertRecipe(recipe: Recipe) {

        }*/

    }



