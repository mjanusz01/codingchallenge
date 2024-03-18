package com.example.androidcodingchallenge

import android.app.Application
import com.example.androidcodingchallenge.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Main application class that enables using instances specified in appModule
 */
class MyApplication : Application() {

    /**
     * Function that is executed when application is created
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}
