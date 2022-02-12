package com.homework.project

import android.app.Application

/**
 * This application class sets up our dependency [Graph] with a context
 */
class ReminderApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}