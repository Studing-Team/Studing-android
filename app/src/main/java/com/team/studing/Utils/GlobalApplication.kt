package com.team.studing.Utils

import android.app.Application
import com.amplitude.android.Amplitude
import com.amplitude.android.Configuration
import com.team.studing.BuildConfig


class GlobalApplication : Application() {

    companion object {
        lateinit var amplitude: Amplitude
            private set
    }


    override fun onCreate() {
        super.onCreate()

        // 앰플리튜드 초기 세팅
        amplitude = Amplitude(
            Configuration(
                apiKey = BuildConfig.AMPLITUDE_KEY,
                context = applicationContext,
                flushIntervalMillis = 50000,
                flushQueueSize = 20,
            )
        )
    }
}