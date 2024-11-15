package com.sekusarisu.mdpings

import android.app.Application
import com.sekusarisu.mdpings.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MDPingsApp: Application(){

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MDPingsApp)
            androidLogger()

            modules(appModule)
        }
    }
}