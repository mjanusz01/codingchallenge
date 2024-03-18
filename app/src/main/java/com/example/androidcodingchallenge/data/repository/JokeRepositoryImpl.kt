package com.example.androidcodingchallenge.data.repository

import com.example.androidcodingchallenge.data.model.JokesData
import com.example.androidcodingchallenge.data.network.JokesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * Implementation of repository that is used to get jokes from API
 * @property jokesDataSource service that executes API calls
 * @property ioDispatcher default dispatcher used in suspend functions
 */
class JokeRepositoryImpl(
    private val jokesDataSource: JokesDataSource,
    private val ioDispatcher: CoroutineDispatcher
) : JokeRepository {
    /**
     * Method that is executed in view model every time jokes are updated.
     * @param amount amount of jokes that are supposed to be downloaded
     * @return NetworkResult of API call
     */
    override suspend fun getJokes(amount: Int): NetworkResult<JokesData> = withContext(ioDispatcher) {
        handleNetworkResult {
            jokesDataSource.getJokes(amount)
        }
    }
}
