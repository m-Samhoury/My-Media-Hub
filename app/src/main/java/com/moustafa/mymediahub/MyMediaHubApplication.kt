package com.moustafa.mymediahub

import android.app.Application
import com.moustafa.mymediahub.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

class MyMediaHubApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyMediaHubApplication)

            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidFileProperties()

            modules(listOf(repositoryModule))
        }
    }
}