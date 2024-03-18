package com.example.androidcodingchallenge.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * Data of one joke. Doesn't contain data that are provided by API and aren't used in the app.
 *
 * @property setup first part of joke
 * @property delivery second part of joke
 * @property id id of joke
 */
@Keep
data class Joke(
    @field:Json(name = "setup")
    val setup: String,
    @field:Json(name = "delivery")
    val delivery: String,
    @field:Json(name = "id")
    val id: Int
)
