plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.androidcodingchallenge"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.androidcodingchallenge"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "11" }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.koin.compose)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    implementation(libs.koin.compose)
    implementation(libs.kotlin.serialization)
    implementation(libs.retrofit2.retrofit)
    implementation(libs.retrofit2.serialization)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.rxjavaadapter)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.io.mockk)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.adapter)
    implementation(libs.moshi.kotlin)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> { kotlinOptions.jvmTarget = "11" }
