package com.moustafa.mymediahub.di

import com.moustafa.mymediahub.BuildConfig
import com.moustafa.mymediahub.repository.Repository
import com.moustafa.mymediahub.repository.network.MyMediaHubService
import com.moustafa.mymediahub.repository.network.NetworkLayerUtils
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

val repositoryModule: Module = module {
    single { NetworkLayerUtils.makeHttpClient(androidContext()) }

    single<MyMediaHubService> { NetworkLayerUtils.makeServiceFactory(get()) }
    single {
        NetworkLayerUtils
            .makeRetrofit(
                BuildConfig.BASE_API_URL,
                get()
            )
    }
    single<Repository> { Repository(get()) }
}