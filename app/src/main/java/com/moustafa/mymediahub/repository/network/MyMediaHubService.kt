package com.moustafa.mymediahub.repository.network

import com.moustafa.mymediahub.models.PhotoInfoListResponse
import com.moustafa.mymediahub.models.UploadPhotoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 *
 * This is the service that will include all the network calls to
 * MyMediaHub main APIs

 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */

interface MyMediaHubService {

    @GET("uploads")
    suspend fun fetchImagesList(): Response<PhotoInfoListResponse>

    @Multipart
    @POST("photo")
    suspend fun uploadImage(
        @Part personalPhoto: MultipartBody.Part
    ): Response<UploadPhotoResponse>
}