package com.moustafa.mymediahub.repository.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.moustafa.mymediahub.models.PhotoInfoListResponse
import com.moustafa.mymediahub.models.UploadPhotoResponse
import com.squareup.moshi.Moshi
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * This helper class provide methods to setup retrofit in the application
 *
 * @author moustafasamhoury
 * created on Tuesday, 10 September, 2019
 */

object NetworkLayerUtils {
    private const val TIME_OUTS: Long = 40L

    fun makeHttpClient(context: Context): OkHttpClient =
        makeHttpClientBuilder(context)
            .addInterceptor(ChuckerInterceptor(context))
            .build()

    fun makeHttpClientBuilder(context: Context): OkHttpClient.Builder =
        OkHttpClient.Builder()
            .connectTimeout(TIME_OUTS, TimeUnit.SECONDS)
            .readTimeout(TIME_OUTS, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUTS, TimeUnit.SECONDS)
            .certificatePinner(CertificatePinner.DEFAULT)
            .cache(Cache(context.cacheDir, 10 * 1024 * 1024))
            .retryOnConnectionFailure(false)

    fun createMoshiInstance() = Moshi.Builder()
        .build()
        .apply {
            adapter<PhotoInfoListResponse>(PhotoInfoListResponse::class.java)
            adapter<UploadPhotoResponse>(PhotoInfoListResponse::class.java)
        }

    inline fun <reified T> makeServiceFactory(
        retrofit: Retrofit
    ): T = retrofit.create(T::class.java)

    fun makeRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(createMoshiInstance()))
            .build()
        return retrofit
    }


}
