package com.example.androidcodingchallenge.data.repository

import retrofit2.Response

/**
 * Class that represents result of network calls.
 * @property Success request was successful and data (of any type) are being received
 * @property Error error with specified status code has been thrown
 * @property ConnectionError internet connection error - user is not connected to the internet
 * @property UnknownError unspecified exception has been thrown
 */
sealed class NetworkResult<T : Any> {
    class Success<T : Any>(val data: T) : NetworkResult<T>()
    class Error<T : Any>(val code: Int) : NetworkResult<T>()
    class ConnectionError<T : Any> : NetworkResult<T>()
    class UnknownError<T : Any> : NetworkResult<T>()
}

/**
 * Executes specified function 'execute' and processes its Response
 * @param execute function that is being executed
 * @return NetworkResult depending on Response of API call
 */
suspend fun <T : Any> handleNetworkResult(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Error(code = response.code())
        }
    } catch (e: java.net.UnknownHostException) {
        NetworkResult.ConnectionError()
    } catch (e: Throwable) {
        NetworkResult.UnknownError()
    }
}
