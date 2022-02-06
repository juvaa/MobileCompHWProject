package com.homework.project.data

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.homework.project.R


// Adapted from https://stackoverflow.com/a/40446531

object UserPreferences {
    private var sharedPref: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPref == null) sharedPref =
            context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)

        // Write dummy user info to the file
        this.write(context.getString(R.string.username_key), "user")
        this.write(context.getString(R.string.password_key), "password1")
    }

    fun read(key: String?, defValue: String?): String? {
        return sharedPref!!.getString(key, defValue)
    }

    fun write(key: String?, value: String?) {
        val prefsEditor = sharedPref!!.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun read(key: String?, defValue: Boolean): Boolean {
        return sharedPref!!.getBoolean(key, defValue)
    }

    fun write(key: String?, value: Boolean) {
        val prefsEditor = sharedPref!!.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun read(key: String?, defValue: Int): Int {
        return sharedPref!!.getInt(key, defValue)
    }

    fun write(key: String?, value: Int?) {
        val prefsEditor = sharedPref!!.edit()
        prefsEditor.putInt(key, value!!).apply()
    }
}