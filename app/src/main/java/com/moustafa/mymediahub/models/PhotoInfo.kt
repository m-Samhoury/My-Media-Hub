package com.moustafa.mymediahub.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * @author moustafasamhoury
 * created on Tuesday, 08 Oct, 2019
 */
@JsonClass(generateAdapter = true)
data class PhotoInfo(
    @field:Json(name = "imageUrl")
    val imageUrl: String? = null
)
