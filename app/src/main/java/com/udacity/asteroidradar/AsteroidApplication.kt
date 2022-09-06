package com.udacity.asteroidradar

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        doRefresh()
    }

    private fun doRefresh() {
        applicationScope.launch {
            val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
                1,
                TimeUnit.DAYS
            ).build()
            WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME , ExistingPeriodicWorkPolicy.KEEP ,repeatingRequest)
        }
    }
}