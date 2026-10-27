package com.a.server.presentation

import android.app.Application
import com.a.server.di.AndroidModule
import com.a.server.di.AppModule
import com.a.server.di.initializeKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.dsl.module
import org.koin.plugin.module.dsl.module

class Application: Application() {
    override fun onCreate() {
        super.onCreate()

        initializeKoin(
            config = {
                androidContext(this@Application)
                androidLogger()
                module<AndroidModule>()
            }
        )
    }
}