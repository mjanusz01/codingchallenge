package com.example.androidcodingchallenge.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidcodingchallenge.R
import com.example.androidcodingchallenge.data.model.Joke
import com.example.androidcodingchallenge.error.ErrorState
import com.example.androidcodingchallenge.error.toErrorDescription
import com.example.androidcodingchallenge.ui.theme.AndroidCodingChallengeTheme
import com.example.androidcodingchallenge.ui.vm.JokeUiState

@Composable
fun JokeScreen(
    uiState: JokeUiState,
    onJokesDownload: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier) {
        TopBar(
            isLoading = (uiState.jokeScreenState == JokeUiState.JokeScreenState.LOADING),
            onJokesUpdateButtonClick = {
                onJokesDownload()
            }
        )
        when (uiState.jokeScreenState) {
            JokeUiState.JokeScreenState.LOADING -> Unit
            JokeUiState.JokeScreenState.SUCCESS -> JokesColumn(uiState.jokes)
            JokeUiState.JokeScreenState.ERROR -> JokeErrorText(LocalContext.current.getString(uiState.errorState.toErrorDescription()))
        }
    }
}

@Composable
private fun JokeButton(
    isLoading: Boolean,
    onJokesUpdateButtonClick: () -> Unit
) {
    Button(
        onClick = onJokesUpdateButtonClick,
        modifier = Modifier
            .width(150.dp)
            .height(40.dp),
        enabled = !isLoading
    ) {
        if (isLoading) {
            Icon(
                painterResource(R.drawable.loading_removebg_preview),
                contentDescription = stringResource(R.string.loading_indicator_description),
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(stringResource(R.string.button_text))
        }
    }
}

@Composable
private fun JokeErrorText(
    errorText: String
) {
    Text(
        color = MaterialTheme.colorScheme.error,
        text = errorText,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    )
}

@Composable
private fun JokeRow(
    jokeData: Joke
) {
    var isOpen by remember { mutableStateOf(false) }

    Row {
        Spacer(Modifier.weight(0.05F))
        Row(
            modifier = Modifier
                .weight(0.9F)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isOpen) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondaryContainer)
                .clickable {
                    isOpen = !isOpen
                }
        ) {
            Text(
                text = if (isOpen) jokeData.delivery else jokeData.setup,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                color = if (isOpen) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(Modifier.weight(0.05F))
    }
}

@Composable
private fun TopBar(
    onJokesUpdateButtonClick: () -> Unit,
    isLoading: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.top_bar_header_text),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.top_bar_title_text),
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(10.dp))
        JokeButton(isLoading, onJokesUpdateButtonClick)
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
private fun JokesColumn(
    jokes: List<Joke>
) {
    LazyColumn {
        items(jokes) { joke ->
            Spacer(Modifier.height(10.dp))
            JokeRow(joke)
        }
    }
}

@Preview
@Composable
fun JokeScreenSuccessPreview() {
    AndroidCodingChallengeTheme {
        Surface(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            JokeScreen(
                uiState = JokeUiState(
                    JokeUiState.JokeScreenState.SUCCESS,
                    listOf(
                        Joke("Setup 1", "Delivery 1", 1),
                        Joke("Setup 2", "Delivery 2", 1),
                        Joke("Setup 3", "Delivery 3", 1),
                        Joke("Setup 4", "Delivery 4", 1),
                        Joke("Setup 5", "Delivery 5", 1)
                    ),
                    ErrorState.NO_ERROR
                ),
                onJokesDownload = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JokeScreenSuccessDarkModePreview() {
    AndroidCodingChallengeTheme {
        Surface(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            JokeScreen(
                uiState = JokeUiState(
                    JokeUiState.JokeScreenState.SUCCESS,
                    listOf(
                        Joke("Setup 1", "Delivery 1", 1),
                        Joke("Setup 2", "Delivery 2", 2),
                        Joke("Setup 3", "Delivery 3", 3),
                        Joke("Setup 4", "Delivery 4", 4),
                        Joke("Setup 5", "Delivery 5", 5)
                    ),
                    ErrorState.NO_ERROR
                ),
                onJokesDownload = {}
            )
        }
    }
}

@Preview
@Composable
fun JokeScreenLoadingPreview() {
    AndroidCodingChallengeTheme {
        Surface(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            JokeScreen(
                uiState = JokeUiState(
                    JokeUiState.JokeScreenState.LOADING,
                    emptyList(),
                    ErrorState.NO_ERROR
                ),
                onJokesDownload = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JokeScreenLoadingDarkModePreview() {
    AndroidCodingChallengeTheme {
        Surface(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            JokeScreen(
                uiState = JokeUiState(
                    JokeUiState.JokeScreenState.LOADING,
                    emptyList(),
                    ErrorState.NO_ERROR
                ),
                onJokesDownload = {}
            )
        }
    }
}

@Preview
@Composable
fun JokeScreenErrorPreview() {
    AndroidCodingChallengeTheme {
        Surface(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            JokeScreen(
                uiState = JokeUiState(
                    JokeUiState.JokeScreenState.ERROR,
                    emptyList(),
                    ErrorState.LOADING_FAILED_CONNECTION_ERROR
                ),
                onJokesDownload = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JokeScreenErrorDarkModePreview() {
    AndroidCodingChallengeTheme {
        Surface(
            Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            JokeScreen(
                uiState = JokeUiState(
                    JokeUiState.JokeScreenState.ERROR,
                    emptyList(),
                    ErrorState.LOADING_FAILED_CONNECTION_ERROR
                ),
                onJokesDownload = {}
            )
        }
    }
}
