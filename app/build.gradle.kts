import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

val properties = Properties()
properties.load(FileInputStream(rootProject.file("local.properties")))

android {
    namespace = "com.team.studing"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.team.studing"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "1.0.3"

        buildConfigField("String", "MAP_API_KEY", "\"${properties["map_client_id"]}\"")
        buildConfigField("String", "SERVER_URL", "\"${properties["server_url"]}\"")
        buildConfigField("String", "DEV_URL", "\"${properties["dev_url"]}\"")
        buildConfigField("String", "AMPLITUDE_KEY", "\"${properties["amplitude_api_key"]}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    dataBinding {
        enable = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.messaging)
    implementation(libs.androidx.monitor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // tooltip
    implementation("com.github.skydoves:balloon:1.6.6")

    // 캘린더
    implementation("io.github.architshah248.calendar:awesome-calendar:2.0.0")

    // onboarding dot indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.13.0")

//    implementation("com.google.android.gms:play-services-maps:21.0.1")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.naver.maps:map-sdk:3.19.1")
//    implementation("com.google.firebase:firebase-messaging-directboot:20.2.0")

    // api
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // JSON 변환
    implementation("com.squareup.okhttp3:okhttp:4.10.0") // OkHttp 라이브러리
    implementation("com.squareup.okhttp3:logging-interceptor:3.11.0")
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.0")

    // amplitude
    implementation("com.amplitude:analytics-android:1.+")
}