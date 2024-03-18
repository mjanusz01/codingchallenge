package com.example.androidcodingchallenge.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcodingchallenge.domain.GetJokesResult
import com.example.androidcodingchallenge.domain.GetJokesUseCase
import com.example.androidcodingchallenge.error.ErrorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/*
    DEFAULT_JOKES_AMOUNT - defines how many jokes should be displayed on UI.
 */
const val DEFAULT_JOKES_AMOUNT = 20

/**
 * View model responsible for handling user actions and updating joke's data
 * @property getJokesUseCase use case that allows to download jokes
 */
class JokesViewModel(
    private val getJokesUseCase: GetJokesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JokeUiState())
    val uiState: StateFlow<JokeUiState> = _uiState
    init {
        onJokesDownload(DEFAULT_JOKES_AMOUNT)
    }

    /**
     * Runs a coroutine downloading jokes and updating uiState
     * @param jokeAmount amount of jokes that are supposed to be downloaded
     */
    fun onJokesDownload(
        jokeAmount: Int = DEFAULT_JOKES_AMOUNT
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(jokeScreenState = JokeUiState.JokeScreenState.LOADING) }
            when (val result = getJokesUseCase.invoke(jokeAmount)) {
                is GetJokesResult.Success -> {
                    _uiState.update {
                        it.copy(
                            jokeScreenState = JokeUiState.JokeScreenState.SUCCESS,
                            jokes = result.jokeList,
                            errorState = ErrorState.NO_ERROR
                        )
                    }
                }

                is GetJokesResult.Error -> {
                    _uiState.update {
                        it.copy(
                            jokeScreenState = JokeUiState.JokeScreenState.ERROR,
                            jokes = emptyList(),
                            errorState = result.errorState
                        )
                    }
                }
            }
        }
    }
}
