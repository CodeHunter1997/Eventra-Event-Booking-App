plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id ("kotlin-kapt")  // ← Must add this for kapt to work
        id ("kotlin-parcelize")
    }

android {
    namespace = "com.example.eventapp"
    compileSdk = 35


    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "com.example.eventapp"
        minSdk = 24
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("com.google.android.gms:play-services-auth:21.0.0")
    implementation ("com.google.android.material:material:1.8.0")
    implementation ("androidx.core:core-splashscreen:1.0.1")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("me.relex:circleindicator:2.1.6")
    implementation ("com.google.android.material:material:1.11.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("androidx.core:core-splashscreen:1.0.0")
    implementation ("com.google.android.gms:play-services-maps:18.2.0")
    // Image loading (optional)
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    kapt ("com.github.bumptech.glide:compiler:4.12.0")

    // Dots indicator (optional)
    implementation ("com.tbuonomo:dotsindicator:4.3")
}