package com.example.androidcodingchallenge.error

/**
 * State which specifies current error in the app.
 */
enum class ErrorState {
    NO_ERROR,
    LOADING_FAILED_4XX,
    LOADING_FAILED_5XX,
    LOADING_FAILED_EMPTY_JOKES_LIST,
    LOADING_FAILED_CANT_DOWNLOAD_UNIQUE_JOKES,
    LOADING_FAILED_UNDEFINED_ERROR,
    LOADING_FAILED_CONNECTION_ERROR
}
