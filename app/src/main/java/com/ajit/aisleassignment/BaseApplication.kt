package com.ajit.aisleassignment

import android.app.Application
import com.ajit.aisleassignment.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()


        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule)
        }
    }


}