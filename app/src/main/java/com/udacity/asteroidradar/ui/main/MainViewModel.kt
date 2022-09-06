package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val database: AsteroidDatabase, app: Application) :
    AndroidViewModel(app) {


    private val asteroidsRepository = AsteroidsRepository(database)

    private val _navigateToDetails = MutableLiveData<Asteroid?>()
    val navigateToDetails: LiveData<Asteroid?>
        get() = _navigateToDetails

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    private val _progressBar = MutableLiveData<Boolean>()
    val progressBar: LiveData<Boolean>
        get() = _progressBar


    private var _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList




    init {

        getAllAsteroids()

        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids(formatDate(), formatDate(7))
            } catch (e: Exception) {
                //doNothing
                Log.e("MainViewModel", e.toString())
            }
            try {
                _pictureOfDay.value = asteroidsRepository.loadPictureOfToday()
            } catch (e: Exception) {
                //doNothing
                Log.e("MainViewModel", e.message.toString())
            }
        }
    }



    fun getAllAsteroids() {
        _progressBar.value = true
        viewModelScope.launch {
            _asteroidList.value = getAll()

        }
    }

    private suspend fun getAll(): List<Asteroid>? {
        return withContext(Dispatchers.IO) {
            asteroidsRepository.getData()
        }
    }



    fun getAsteroidsByFilter(filter: AsteroidApiFilter) {
        _progressBar.value = true
        viewModelScope.launch {
            _asteroidList.value = getToday(filter)
        }
    }

    private suspend fun getToday(filter: AsteroidApiFilter): List<Asteroid>? {
        return withContext(Dispatchers.IO) {
            asteroidsRepository.getDataByDate(filter)
        }
    }

    fun navigateToDetails(asteroid: Asteroid) {
        _navigateToDetails.value = asteroid
    }

    fun doneNavigateToDetails() {
        _navigateToDetails.value = null
    }

    fun doneShowingProgressBar() {
        _progressBar.value = false
    }


}