package com.example.androidcodingchallenge.data.network

import com.example.androidcodingchallenge.data.model.JokesData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

private const val JOKE_TYPE = "twopart"

private const val BLACKLIST_FLAGS = "nsfw"

/**
 * Service responsible for sending HTTP requests
 */

interface JokesDataSource {

    /**
     * Performs HTTP request with parameters:
     * @param amount number of jokes that are supposed to be downloaded
     * @param type type of jokes ('twopart' by default)
     * @param blacklistFlags jokes that shouldn't be downloaded ('nsfw' by default)
     * @return HTTP response with JokesData
     */
    @Headers(
        "Content-Type: application/json"
    )
    @GET("Any")
    suspend fun getJokes(
        @Query("amount") amount: Int,
        @Query("type") type: String = JOKE_TYPE,
        @Query("blacklistFlags") blacklistFlags: String = BLACKLIST_FLAGS
    ): Response<JokesData>
}
