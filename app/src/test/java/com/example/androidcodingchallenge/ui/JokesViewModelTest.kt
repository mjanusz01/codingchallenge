package com.example.androidcodingchallenge.ui

import com.example.androidcodingchallenge.data.model.JokesData
import com.example.androidcodingchallenge.data.network.JokesDataSource
import com.example.androidcodingchallenge.di.appModule
import com.example.androidcodingchallenge.error.ErrorState
import com.example.androidcodingchallenge.ui.vm.JokeUiState
import com.example.androidcodingchallenge.ui.vm.JokesViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import retrofit2.Response
import kotlin.test.assertNotEquals

class JokesViewModelTest : KoinTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun before() {
        startKoin {
            modules(
                appModule,
                module {
                    single<CoroutineDispatcher> {
                        UnconfinedTestDispatcher()
                    }
                    single {
                        mockk<JokesDataSource>()
                    }
                }
            )
        }
    }

    @Test
    fun `Creating view model downloads 20 unique jokes sorted by id, jokes from API don't repeat, app state and error state is set correctly`() {
        // Given
        val expectedJokesList = (testJokesFirstList + testJokesSecondList).sortedBy { it.id }
        val expectedAppState = JokeUiState.JokeScreenState.SUCCESS
        val expectedErrorState = ErrorState.NO_ERROR

        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returnsMany listOf(
            Response.success(JokesData(testJokesFirstList)),
            Response.success(JokesData(testJokesSecondList))
        )

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesList, viewModel.uiState.value.jokes)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)

        assertEquals(20, viewModel.uiState.value.jokes.distinct().size)
        coVerify(exactly = 2) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model downloads unique 20 jokes, API returns jokes that repeat in first two calls`() {
        // Given
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returnsMany listOf(
            Response.success(JokesData(testJokesFirstList)),
            Response.success(JokesData(testJokesMixedFromFirstAndSecondList)),
            Response.success(JokesData(testJokesThirdList))
        )

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(20, viewModel.uiState.value.jokes.distinct().size)
        coVerify(exactly = 3) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model downloads 20 jokes, reload jokes downloads 20 new jokes, every from 4 API calls returns different jokes`() {
        // Given
        val dataSource = get<JokesDataSource>()
        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returnsMany listOf(
            Response.success(JokesData(testJokesFirstList)),
            Response.success(JokesData(testJokesSecondList)),
            Response.success(JokesData(testJokesThirdList)),
            Response.success(JokesData(testJokesFourthList))
        )

        // When
        val viewModel = get<JokesViewModel>()
        val jokesAfterFirstCall = viewModel.uiState.value.jokes

        viewModel.onJokesDownload()
        val jokesAfterSecondCall = viewModel.uiState.value.jokes

        // Then
        assertNotEquals(jokesAfterFirstCall, jokesAfterSecondCall)
        coVerify(exactly = 4) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model triggers download, API throws 400 error, list is empty, error message is correct, flags are set correctly and API is called once`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_4XX
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returns Response.error(400, "Error".toResponseBody(null))

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 1) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model triggers download, API throws 500 error, list is empty, error message is correct, flags are set correctly and API is called once`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_5XX
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returns Response.error(500, "Error".toResponseBody(null))

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 1) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model triggers download, API throws unknown error, list is empty, error message is correct, flags are set correctly and API is called once`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_UNDEFINED_ERROR
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returns Response.error(501, "Error".toResponseBody(null))

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 1) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model triggers download and API throws UnknownHostException causes error and flags are set correctly`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_CONNECTION_ERROR
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } throws java.net.UnknownHostException("Unable to resolve host \"v2.jokeapi.dev\": No address associated with hostname")

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 1) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `Creating view model triggers download and API throws other exception causes error and flags are set correctly`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_UNDEFINED_ERROR
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } throws java.net.BindException("Error message")

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 1) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `API returns 200 OK but error flag in response is true causes error and flags are set correctly`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_EMPTY_JOKES_LIST
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returns Response.success(JokesData(null))

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 1) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `First API call returns 10 jokes, second API call throws 400 error, flags are set correctly`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_4XX
        val dataSource = get<JokesDataSource>()

        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returns Response.success(JokesData(testJokesFirstList)) andThen Response.error(
            400,
            "Error".toResponseBody(null)
        )

        // When
        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 2) { dataSource.getJokes(any(), any(), any()) }
    }

    @Test
    fun `First API call returns 10 jokes, second API call returns same jokes`() {
        // Given
        val expectedJokesListSize = 0
        val expectedAppState = JokeUiState.JokeScreenState.ERROR
        val expectedErrorState = ErrorState.LOADING_FAILED_CANT_DOWNLOAD_UNIQUE_JOKES
        val dataSource = get<JokesDataSource>()

        // When
        coEvery {
            dataSource.getJokes(any(), any(), any())
        } returns Response.success(JokesData(testJokesFirstList))

        val viewModel = get<JokesViewModel>()

        // Then
        assertEquals(expectedJokesListSize, viewModel.uiState.value.jokes.size)
        assertEquals(expectedAppState, viewModel.uiState.value.jokeScreenState)
        assertEquals(expectedErrorState, viewModel.uiState.value.errorState)
        coVerify(exactly = 2) { dataSource.getJokes(any(), any(), any()) }
    }

    @After
    fun after() {
        stopKoin()
    }
}
