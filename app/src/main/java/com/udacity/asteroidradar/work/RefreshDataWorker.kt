package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.utils.formatDate
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context , parameters: WorkerParameters) : CoroutineWorker(appContext , parameters) {
    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidsRepository(database)
        try {
            repository.refreshAsteroids(formatDate() , formatDate(7))
            return Result.success()
        }catch (e : HttpException){
            return Result.retry()
        }
    }
}