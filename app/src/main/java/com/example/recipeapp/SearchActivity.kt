package com.example.recipeapp

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.recipeapp.databinding.ActivitySearchBinding
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var rvAdapter: SearchAdapter
    private lateinit var dataList: ArrayList<Recipe>
    private lateinit var recipes: List<Recipe?>
    private val entries = mutableListOf<Recipe?>()
    private lateinit var recipeDao: Dao
    private lateinit var db: AppDatabase

    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search1.requestFocus()

         db = Room.databaseBuilder(this@SearchActivity, AppDatabase::class.java, "db_name")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()



        recipeDao = db.getDao()
        var daoObject = recipeDao
        recipes = daoObject.getAll()!!
        setupRecyclerView()
        binding.goBackHome.setOnClickListener {
            finish()
        }
        binding.search1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString() != "") {
                    filterData(s.toString())
                } else {
                    setupRecyclerView()
                }

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().endsWith("\n")) {
                    fetchDataAndGenerateReport()
                }
            }


        })
        binding.search1.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                fetchDataAndGenerateReport()
                true
            } else {
                false
            }
        }

    }

    private fun fetchDataAndGenerateReport() {
            CoroutineScope(Dispatchers.IO).launch {
                val searchQuery = binding.search1.text.toString()
                val entries = recipeDao.searchDatabase("%$searchQuery%")
                withContext(Dispatchers.Main) {
                    if (entries.isNotEmpty()) {
                        this@SearchActivity.entries.clear()
                        this@SearchActivity.entries.addAll(entries)
                        generateReport()
                    } else {
                        Log.e("ReportActivity", "No data to generate report")
                        Toast.makeText(this@SearchActivity, "No data found!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private fun generateReport() {
        if (entries.isEmpty()) {
            Log.e("ReportActivity", "No data to generate report")
            return
        }
        try {
            val downloadsFolder =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val pdfFile = File(downloadsFolder, "report5.pdf")
            val pdfDocument = PdfDocument(PdfWriter(pdfFile))
            val pageSize = PageSize.A4
            val document = Document(pdfDocument, pageSize)
            val tableWidth = pageSize.width - document.leftMargin - document.rightMargin
            val columnWidth = tableWidth * 0.95f / 2f

            val table = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                        0.3f,
                        0.3f
                    )
                )
            ).useAllAvailableWidth()

            //Add Header Cells
            table.addHeaderCell(
                Cell().add(
                    Paragraph("Title").setTextAlignment(
                        TextAlignment.CENTER
                    )
                )
            )

            table.addHeaderCell(
                Cell().add(
                    Paragraph("Category").setTextAlignment(
                        TextAlignment.CENTER
                    )
                )
            )

            for (entry in entries) {
                table.addCell(
                    Cell().add(
                        Paragraph(entry?.tittle).setTextAlignment(
                            TextAlignment.CENTER
                        )
                    ).setPadding(5f)
                )
                table.addCell(
                    Cell().add(
                        Paragraph(entry?.category).setTextAlignment(
                            TextAlignment.CENTER
                        )
                    ).setPadding(5f)
                )
            }

            document.add(table)
            document.close()
            db.close()

            Log.i("ReportActivity", "PDF generated successfully")
            Toast.makeText(this@SearchActivity, "pdf generated successfully!", Toast.LENGTH_SHORT)
                .show()

            // Open the PDF file using an intent
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdirs()
            }
            if (!downloadsFolder.canWrite()) {
                Log.e("SearchActivity", "Cannot write to downloads folder")
                return
            }
            val pdfFileUri =
                FileProvider.getUriForFile(this, "com.example.recipeapp.fileprovider", pdfFile)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfFileUri, "application/pdf")
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e("ReportActivity", "No PDF viewer app found", e)
                Toast.makeText(this@SearchActivity, "No PDF viewer app found!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("ReportActivity", "Error generating PDF", e)
            e.printStackTrace() // Log stack trace
           // Toast.makeText(this@SearchActivity, "Error while generating pdf!", Toast.LENGTH_SHORT)
                //.show()
        }
    }




    private fun filterData(filterText: String) {
      val filteredData=ArrayList<Recipe>()
        for(recipe in dataList)
        {
            if(recipe.ing.contains(filterText,ignoreCase = true)||recipe.tittle.lowercase().contains(filterText.lowercase())||recipe.category.contains(filterText,ignoreCase = true))
            {
                filteredData.add(recipe)
            }
            rvAdapter.filerList(filteredData)
        }
    }



    private fun setupRecyclerView() {

        dataList = ArrayList()
        binding.rvSearch.layoutManager =
            LinearLayoutManager(this)

        for (i in recipes!!.indices) {
                dataList.add(recipes[i]!!)
            }
            rvAdapter = SearchAdapter(dataList, this)
            binding.rvSearch.adapter = rvAdapter
        }
    }



