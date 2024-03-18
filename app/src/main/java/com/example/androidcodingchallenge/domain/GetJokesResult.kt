package com.example.androidcodingchallenge.domain

import com.example.androidcodingchallenge.data.model.Joke
import com.example.androidcodingchallenge.error.ErrorState

/**
 * Result of GetJokesUseCase
 */
sealed class GetJokesResult {
    /**
     * Returned if jokes were successfully downloaded
     * @property jokeList list of jokes that are downloaded
     */
    class Success(val jokeList: List<Joke>) : GetJokesResult()

    /**
     * Returned if any error occured
     * @property errorState error thrown when jokes were being downloaded
     */
    class Error(val errorState: ErrorState) : GetJokesResult()
}
