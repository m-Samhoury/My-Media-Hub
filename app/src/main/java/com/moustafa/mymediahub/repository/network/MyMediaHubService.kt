package com.moustafa.mymediahub.repository.network

import com.moustafa.mymediahub.models.PhotoInfoListResponse
import retrofit2.Response
import retrofit2.http.GET

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
}