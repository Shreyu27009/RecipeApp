package com.example.recipeapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RecipeNotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val recipeDatabase = AppDatabase.getInstance(applicationContext)
        val recipeDao = recipeDatabase.getDao()

        val recipe = recipeDao.getRandomRecipe()
        if (recipe != null) {
            sendNotification(recipe)
            return Result.success()
        } else {
            return Result.failure()
        }
    }

    private fun sendNotification(recipe: Recipe) {
        val notificationBuilder = NotificationCompat.Builder(
            applicationContext,
            "recipe_channel"
        )
            .setSmallIcon(R.drawable.drinks)
            .setContentTitle("Recipe of the Day")
            .setContentText(recipe.tittle)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(recipe.des)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager =
            NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(1, notificationBuilder.build())
    }
}