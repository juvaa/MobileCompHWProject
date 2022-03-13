package com.homework.project.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.SystemClock
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.homework.project.Graph

class LocationReminderNotificationWorker(
    context: Context,
    userParameters: WorkerParameters
) : Worker(context, userParameters) {
    private val workerInputData = userParameters.inputData
    private val reminderLatitude = workerInputData.getDouble("latitude", 0.0)
    private val reminderLongitude = workerInputData.getDouble("longitude", 0.0)
    
    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        val fusedLocationClient= LocationServices.getFusedLocationProviderClient(Graph.appContext)
        var longitude = 0.0
        var latitude = 0.0

        return try {
            var i = 0
            val sleepTime: Long = 5000
            do {
                SystemClock.sleep(sleepTime);
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    longitude = location?.longitude ?: 0.0
                    latitude = location?.latitude ?: 0.0
                }
                Log.i("Location", "Tested $i")
                i++
            } while (!checkLocation(latitude, longitude))
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun checkLocation(latitude: Double, longitude: Double) : Boolean {
        val distance = 0.01
        val latitudeCheck = ((reminderLatitude - distance < latitude) && (latitude < reminderLatitude + distance))
        val longitudeCheck = ((reminderLongitude - distance < longitude) && (longitude < reminderLongitude + distance))
        return latitudeCheck && longitudeCheck
    }
}