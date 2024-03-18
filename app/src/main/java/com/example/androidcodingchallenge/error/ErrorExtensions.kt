package com.example.androidcodingchallenge.error

import com.example.androidcodingchallenge.R

/**
 * Maps status codes to specified ErrorState
 */
fun Int.toLoadingState(): ErrorState =
    when (this) {
        in listOf(400, 403, 404, 413, 414, 429) -> ErrorState.LOADING_FAILED_4XX
        in listOf(500, 523) -> ErrorState.LOADING_FAILED_5XX
        else -> ErrorState.LOADING_FAILED_UNDEFINED_ERROR
    }

/**
 * Maps ErrorState to error description resource ID shown on UI.
 */
fun ErrorState.toErrorDescription(): Int {
    return when (this) {
        ErrorState.LOADING_FAILED_CONNECTION_ERROR -> R.string.loading_failed_connection_error_description_en
        ErrorState.LOADING_FAILED_CANT_DOWNLOAD_UNIQUE_JOKES -> R.string.loading_successful_cant_download_unique_jokes_description_en
        ErrorState.LOADING_FAILED_4XX -> R.string.error_4xx_description_en
        ErrorState.LOADING_FAILED_5XX -> R.string.error_5xx_description_en
        ErrorState.LOADING_FAILED_EMPTY_JOKES_LIST -> R.string.api_problems_error_description_en
        ErrorState.LOADING_FAILED_UNDEFINED_ERROR -> R.string.unspecified_error_description_en
        ErrorState.NO_ERROR -> R.string.unspecified_error_description_en
    }
}
