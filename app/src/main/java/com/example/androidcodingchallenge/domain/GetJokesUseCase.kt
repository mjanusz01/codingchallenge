package com.example.androidcodingchallenge.domain

import com.example.androidcodingchallenge.data.model.Joke
import com.example.androidcodingchallenge.data.repository.JokeRepository
import com.example.androidcodingchallenge.data.repository.NetworkResult
import com.example.androidcodingchallenge.error.ErrorState
import com.example.androidcodingchallenge.error.toLoadingState

private const val MAX_JOKES_AMOUNT_FROM_API = 10

/**
 * Use case which allows to download jokes from repository.
 * @property repository repository with method providing jokes
 */
class GetJokesUseCase(
    private val repository: JokeRepository
) {
    /**
     * Downloads jokes from repository
     * @param jokeAmount amount of jokes that are supposed to be downloaded
     * @return GetJokesResult, which indicates if jokes were successfully downloaded
     */
    suspend operator fun invoke(jokeAmount: Int): GetJokesResult {
        var jokesList = listOf<Joke>()
        /*
            API can't send more than 10 jokes in one response, so more than one request is sent when more than 10 jokes are needed.
         */
        while (jokesList.size < jokeAmount) {
            when (val response = repository.getJokes(MAX_JOKES_AMOUNT_FROM_API)) {
                is NetworkResult.Success -> {
                    /*
                        JokeAPI can return return 200 OK status, error flag set to true and empty jokes list in response body, when API cannot find any jokes meeting requirements.
                        In that case function returns error.
                     */
                    if (response.data.jokes == null) {
                        return GetJokesResult.Error(ErrorState.LOADING_FAILED_EMPTY_JOKES_LIST)
                    }

                    val newJokesList = (jokesList + (response.data.jokes)).distinct()
                    /*
                        When API can't provide any new jokes, function returns error.
                    */
                    if (jokesList == newJokesList) {
                        return GetJokesResult.Error(ErrorState.LOADING_FAILED_CANT_DOWNLOAD_UNIQUE_JOKES)
                    }
                    jokesList = newJokesList
                }

                is NetworkResult.Error -> {
                    return GetJokesResult.Error(response.code.toLoadingState())
                }

                is NetworkResult.ConnectionError -> {
                    return GetJokesResult.Error(ErrorState.LOADING_FAILED_CONNECTION_ERROR)
                }

                is NetworkResult.UnknownError -> {
                    return GetJokesResult.Error(ErrorState.LOADING_FAILED_UNDEFINED_ERROR)
                }
            }
        }

        /*
            JokesList contains now at least 'jokeAmount unique jokes. Only 'jokeAmount' jokes are taken and sorted ascending by id.
         */
        return GetJokesResult.Success(
            jokesList.take(jokeAmount).sortedBy { it.id }
        )
    }
}
