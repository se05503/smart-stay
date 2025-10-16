plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.smartstay"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.smartstay"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.material.v1120)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.retrofit2:retrofit:2.9.0") // retrofit2
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1") // gson

    implementation("com.github.bumptech.glide:glide:4.16.0") // glide
    implementation("com.airbnb.android:lottie:5.2.0") // lottie animation
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // coroutine

    implementation("com.kakao.sdk:v2-user:2.21.7") // kakao login

    implementation("com.naver.maps:map-sdk:3.21.0") // naver map
    implementation("com.navercorp.nid:oauth:5.10.0") // naver login (jdk 11)

    implementation(platform("com.google.firebase:firebase-bom:34.3.0")) // firebase (google)
    implementation("com.google.android.gms:play-services-auth:21.4.0") // google login

    // TMAP - Vector Map API
    implementation(files("libs/tmap-sdk-3.0.aar"))
    implementation(files("libs/vsm-tmap-sdk-v2-android-1.7.45.aar"))

    // GPS Location
    implementation(libs.play.services.location)

}