package com.moustafa.mymediahub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author moustafasamhoury
 * created on Wednesday, 09 Oct, 2019
 */

@JsonClass(generateAdapter = true)
data class UploadPhotoResponse(
    @field:Json(name = "status")
    val status: String? = null,
    @field:Json(name = "successful")
    val isSuccessful: Boolean? = null,
    @field:Json(name = "message")
    val message: String? = null
)

//"status": "succeeded",
//"successful": true,
//"message": "File has been uploaded successfully",