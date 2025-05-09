package ru.lisdevs.calcapp

import android.app.Application
import android.content.Context
import com.google.android.material.color.DynamicColors

class App : Application() {

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

    companion object {
        private lateinit var instance: App

        fun getContext(): Context {
            return instance.applicationContext
        }
    }
}