package com.example.recipeapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.FileOutputStream


@Database(entities = ([Recipe::class]), exportSchema = false, version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? =null



        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "recipe.db"
                    )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}