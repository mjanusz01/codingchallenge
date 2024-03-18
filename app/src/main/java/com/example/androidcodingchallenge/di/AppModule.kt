package com.example.androidcodingchallenge.di

import com.example.androidcodingchallenge.data.network.JokesDataSource
import com.example.androidcodingchallenge.data.repository.JokeRepository
import com.example.androidcodingchallenge.data.repository.JokeRepositoryImpl
import com.example.androidcodingchallenge.domain.GetJokesUseCase
import com.example.androidcodingchallenge.ui.vm.JokesViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single(named("BASE_URL")) {
        "https://v2.jokeapi.dev/joke/"
    }
    single {
        Dispatchers.IO
    }
    single {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    }
    single {
        OkHttpClient().newBuilder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(get<String>(named("BASE_URL")))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(get())
            .build()
            .create(JokesDataSource::class.java)
    }
    single<JokeRepository> {
        JokeRepositoryImpl(get(), get())
    }
    single<GetJokesUseCase> {
        GetJokesUseCase(get())
    }
    viewModel {
        JokesViewModel(get())
    }
}
