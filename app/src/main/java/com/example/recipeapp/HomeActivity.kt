package com.example.recipeapp


import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapp.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var rvAdapter: PopularAdapter
    private lateinit var dataList: ArrayList<Recipe>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpRecyclerView()
        binding.search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.salad.setOnClickListener {
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTLE", "Salad")
            myIntent.putExtra("CATEGORY", "Salad")
            startActivity(myIntent)
        }
        binding.maindish.setOnClickListener {
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTLE", "Main Dish")
            myIntent.putExtra("CATEGORY", "Dish")
            startActivity(myIntent)
        }
        binding.drinks.setOnClickListener {
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTLE", "Drinks")
            myIntent.putExtra("CATEGORY", "Drinks")
            startActivity(myIntent)
        }
      /*  binding.more.setOnClickListener {
            var dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.activity_bottom_sheet)
            dialog.show()
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setGravity(Gravity.BOTTOM)
        }*/
        binding.desserts.setOnClickListener {
            var myIntent = Intent(this@HomeActivity, CategoryActivity::class.java)
            myIntent.putExtra("TITTLE", "Desserts")
            myIntent.putExtra("CATEGORY", "Desserts")
            startActivity(myIntent)
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

    }

    private fun setUpRecyclerView() {
        dataList = ArrayList()
        binding.rvPopular.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        var db = Room.databaseBuilder(this@HomeActivity, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()
        var daoObject = db.getDao()
        var recipes = daoObject.getAll()
        for (i in recipes!!.indices) {
            if (recipes[i]!!.category.contains("Popular")) {
                dataList.add(recipes[i]!!)
            }
            rvAdapter = PopularAdapter(dataList, this)
            binding.rvPopular.adapter = rvAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
                R.id.sharebutton -> {
                    val appLink = "https://play.google.com/store/apps/details?id=package com.example.recipeapp"
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my awesome app: $appLink")

// Optionally, specify the package name of the Facebook app to share directly
                    shareIntent.setPackage("com.facebook")
                    startActivity(Intent.createChooser(shareIntent, "Share via"))

                    val instagramAppIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/premium coder"))
                    instagramAppIntent.setPackage("com.instagram.android")

// If Instagram app is not installed, open Instagram in a web browser
                    try {
                        startActivity(instagramAppIntent)
                    } catch (e: ActivityNotFoundException) {
                        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/preminum_coder"))
                        startActivity(webIntent)
                    }
                }
            R.id.supportus -> {
                startActivity(Intent(this,AddRecipeActivity::class.java))
                finish()

            }
            R.id.sign_out->{
                val auth= FirebaseAuth.getInstance()
                auth.signOut()
                startActivity(Intent(this,LoginActivity::class.java))
                finish()
            }
            R.id.about_dev->
            {
                startActivity(Intent(this,AboutUsActivity::class.java))
                finish()

            }
            R.id.privacy_policy->
            {
                startActivity(Intent(this,PrivacyPolicyActivity::class.java))
                finish()

            }
            R.id.report_drawn->
            {
                startActivity(Intent(this,ReportActivity::class.java))
                finish()
            }



                else -> {

                    Toast.makeText(this,"please try again!",Toast.LENGTH_SHORT).show()
                }
            }

        return true
    }
}

