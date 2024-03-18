package com.example.androidcodingchallenge.ui.vm

import com.example.androidcodingchallenge.data.model.Joke
import com.example.androidcodingchallenge.error.ErrorState

/**
 * Current UI State of application that contains all data necessary to generate UI
 * @property jokeScreenState current state of application (LOADING, SUCCESS, ERROR)
 * @property jokes list of jokes that are currently shown on UI
 * @property errorState current error state (can be NO_ERROR or one of other specified errors)
 */
data class JokeUiState(
    val jokeScreenState: JokeScreenState = JokeScreenState.LOADING,
    val jokes: List<Joke> = emptyList(),
    val errorState: ErrorState = ErrorState.NO_ERROR
) {
    enum class JokeScreenState {
        LOADING,
        SUCCESS,
        ERROR
    }
}
