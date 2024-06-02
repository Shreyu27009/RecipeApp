package com.example.recipeapp


import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil.setContentView
import androidx.room.Room
import com.example.recipeapp.databinding.ActivityReportBinding
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class ReportActivity : AppCompatActivity() {
    // Assuming entries is a list of your data objects
    private val entries = mutableListOf<Recipe?>()
    private lateinit var recipeDao: Dao
    private lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "db_name"
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .createFromAsset("recipe.db")
            .build()
        recipeDao = db.getDao()

        binding.generateReportButton.setOnClickListener {
            fetchDataAndGenerateReport(it)
        }
        binding.goback.setOnClickListener{
            Log.d("PrivacyPolicyActivity", "more1 clicked")
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }

    }

    private fun fetchDataAndGenerateReport(it: View?) {
        GlobalScope.launch(Dispatchers.IO) {
            val entries =
                recipeDao.getAll() // Assuming getAllRecipes() is a function in your DAO that returns all recipes
            withContext(Dispatchers.Main) {
                entries.let {
                    Log.d("ReportActivity", "Size of entries list: ${it.size}")
                    this@ReportActivity.entries.clear()
                    this@ReportActivity.entries.addAll(it)
                    generateReport()
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
            val pdfFile = File(downloadsFolder, "report1.pdf")
            val pdfDocument = PdfDocument(PdfWriter(pdfFile))
            val pageSize = PageSize.A4
            val document = Document(pdfDocument,pageSize)
            val tableWidth = pageSize.width - document.leftMargin- document.rightMargin
            val columnWidth = tableWidth* 0.95f / 2f

            val table = Table(
                UnitValue.createPercentArray(
                    floatArrayOf(
                       // 0.2f,
                        0.3f,
                        0.3f
                    )
                )
            ).useAllAvailableWidth()

            //Add Header Cells
          /*  table.addHeaderCell(
                Cell().add(
                    Paragraph("img").setTextAlignment(
                        TextAlignment.CENTER
                    )
                )
            )*/
            table.addHeaderCell(
                Cell().add(
                    Paragraph("tittle").setTextAlignment(
                        TextAlignment.CENTER
                    )
                )
            )


            table.addHeaderCell(
                Cell().add(
                    Paragraph("category").setTextAlignment(
                        TextAlignment.CENTER
                    )
                )
            )

            for (entry in entries) {
               /* table.addCell(
                    Cell().add(
                        Paragraph((entry?.img)).setTextAlignment(
                            TextAlignment.CENTER
                        )
                    ).setPadding(5f)
                )*/
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
            Toast.makeText(this@ReportActivity, "pdf generated successfully!", Toast.LENGTH_SHORT)
                .show()

            // Open the PDF file using an intent
            if (!downloadsFolder.exists()) {
                downloadsFolder.mkdirs()
            }
            if (!downloadsFolder.canWrite()) {
                Log.e("ReportActivity", "Cannot write to downloads folder")
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
                Toast.makeText(this@ReportActivity, "No PDF viewer app found!", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            Log.e("ReportActivity", "Error generating PDF", e)
            e.printStackTrace() // Log stack trace
           // Toast.makeText(this@ReportActivity, "Error while generating pdf!", Toast.LENGTH_SHORT)
               // .show()
        }
    }
}












