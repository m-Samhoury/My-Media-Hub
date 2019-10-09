package com.moustafa.mymediahub.repository

import com.moustafa.mymediahub.models.PhotoInfo
import com.moustafa.mymediahub.repository.network.MyMediaHubService
import com.moustafa.mymediahub.testing.OpenForTesting
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

/**
 * This repository is our source of truth, we fetch all the data using this repository
 * It may fetch the required data from the network layer or from the local db layer
 *
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */
@OpenForTesting
class Repository(private val service: MyMediaHubService) {

    suspend fun fetchHubImages(
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

    suspend fun uploadImage(
        bitmap: File,
        onError: suspend (Exception) -> Unit
    ): Boolean? {
        val response = safeApiCall({
            val part = MultipartBody.Part.createFormData(
                "hub",
                "hub.png",
                bitmap.asRequestBody("image/png".toMediaType())
            )
            service.uploadImage(part)
        }, onError)
        if (response != null) {
            if (response.isSuccessful == true) {
                return true
            } else {
                onError(Exception(response.message))
            }
        }

        return null
    }
}
// auxiliary helping functions

suspend fun <T : Any> Repository.safeApiCall(
    call: suspend () -> Response<T>,
    onError: suspend (Exception) -> Any
): T? {
    val result: Result<T> = safeApiResult(call)
    var data: T? = null

    when (result) {
        is Result.Success ->
            data = result.data
        is Result.Error -> {
            onError(result.exception)
        }
    }
    return data
}

private suspend fun <T : Any> Repository.safeApiResult(
    call: suspend () -> Response<T>
): Result<T> {
    try {
        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!, response.headers())

        return Result.Error(java.lang.Exception())
    } catch (exception: Exception) {
        return Result.Error(exception)
    }
}


sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T, val headers: Headers? = null) :
        Result<T>()

    data class Error(val exception: Exception) : Result<Nothing>()
}