package com.example.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.RecipeAdapter.RecipeViewHolder
import com.example.recipeapp.databinding.ActivityAddedRecipeBinding

class RecipeAdapter(private val recipes:List<AllRecipes>):RecyclerView.Adapter<RecipeViewHolder>() {
    class RecipeViewHolder(private val binding:ActivityAddedRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: AllRecipes) {


        }
        // Declare and initialize your view elements here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
       val binding=ActivityAddedRecipeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe=recipes[position]
        holder.bind(recipe)
    }

}
