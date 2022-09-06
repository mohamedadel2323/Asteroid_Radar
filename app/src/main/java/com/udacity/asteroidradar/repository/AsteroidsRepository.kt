package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDatabaseModel
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {


    fun getData(): List<Asteroid> {
        return database.asteroidDao.getAllAsteroids().asDomainModel()
    }

    fun getDataByDate(filter: AsteroidApiFilter): List<Asteroid> {
        return database.asteroidDao.getAllAsteroidsByDate(
            filter.startDate,
            filter.endDate
        ).asDomainModel()
    }
    

    suspend fun refreshAsteroids(
        startDate: String,
        endDate: String
    ) {
        withContext(Dispatchers.IO) {
            val result = AsteroidApi.retrofitService.getAsteroids(startDate, endDate)
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(result))
            Log.e("Asteroids", asteroidsList.toString())
            database.asteroidDao.insertAll(*asteroidsList.asDatabaseModel())
        }
    }

    suspend fun loadPictureOfToday(): PictureOfDay? {
        var picture: PictureOfDay
        withContext(Dispatchers.IO) {
            picture = AsteroidApi.retrofitService.getPictureOfDay()
        }
        if (picture.mediaType == "image") {
            return picture
        } else {
            return null
        }
    }

}