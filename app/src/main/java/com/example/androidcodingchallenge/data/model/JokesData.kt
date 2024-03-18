package com.example.androidcodingchallenge.data.model

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * Data received from API with a list of jokes. Doesn't contain data that are provided by API and aren't used in the app.
 *
 * @property jokes list of jokes received from API. If error occured then jokes are null
 */
@Keep
data class JokesData(
    @field:Json(name = "jokes")
    val jokes: List<Joke>?
)
