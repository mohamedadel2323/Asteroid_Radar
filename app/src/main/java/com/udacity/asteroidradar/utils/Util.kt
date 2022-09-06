package com.udacity.asteroidradar.utils


import com.udacity.asteroidradar.Constants
import java.text.SimpleDateFormat
import java.util.*


    fun formatDate(endDate : Int? = 0) : String {
        var calender = Calendar.getInstance()
        if (endDate != 0){
            calender.add(Calendar.DAY_OF_YEAR , endDate!!)
        }
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT , Locale.ENGLISH)
        return dateFormat.format(calender.time)
    }