package com.moustafa.mymediahub.repository

import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.network.MyMediaHubService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * @author moustafasamhoury
 * created on Wednesday, 09 Oct, 2019
 */

class RepositoryImp(private val service: MyMediaHubService) : Repository {

    override suspend fun fetchHubImages(
        onError: suspend (Exception) -> Unit
    ): List<PhotoInfo>? {
        val response = safeApiCall({
            service.fetchImagesList()
        }, onError)

        if (response != null) {
            return response.photos
        }
        return null
    }

    override suspend fun uploadImage(
        file: File,
        onError: suspend (Exception) -> Unit
    ): Boolean? {
        val response = safeApiCall({
            val part = MultipartBody.Part.createFormData(
                "hub",
                file.name,
                file.asRequestBody("image/png".toMediaType())
            )
            service.uploadImage(part)
        }, onError)
        if (response != null) {
            if (response.isSuccessful == true) {
                return true
            }
        }

        return null
    }
}