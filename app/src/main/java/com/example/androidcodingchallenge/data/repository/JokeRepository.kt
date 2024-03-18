package com.example.androidcodingchallenge.data.repository

import com.example.androidcodingchallenge.data.model.JokesData

/**
 * Interface for repository that is used to get jokes from API
 */
interface JokeRepository {
    /**
     * Download jokes from service
     * @param amount amount of jokes that should be downloaded
     * @return NetworkResult - result of API call with data or specified error
     */
    suspend fun getJokes(
        amount: Int
    ): NetworkResult<JokesData>
}
