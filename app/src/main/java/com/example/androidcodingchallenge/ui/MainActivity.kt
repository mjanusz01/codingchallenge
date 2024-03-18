package com.example.androidcodingchallenge.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.androidcodingchallenge.ui.screen.JokeScreen
import com.example.androidcodingchallenge.ui.theme.AndroidCodingChallengeTheme
import com.example.androidcodingchallenge.ui.vm.DEFAULT_JOKES_AMOUNT
import com.example.androidcodingchallenge.ui.vm.JokesViewModel
import org.koin.androidx.compose.getViewModel

/*
    MainActivity - class that creates main UI component - MainScreen and sets some UI details such as theme, background or surface.
*/
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = getViewModel<JokesViewModel>()
            val uiState by viewModel.uiState.collectAsState()

            AndroidCodingChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    JokeScreen(
                        uiState = uiState,
                        onJokesDownload = { viewModel.onJokesDownload(DEFAULT_JOKES_AMOUNT) },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}
