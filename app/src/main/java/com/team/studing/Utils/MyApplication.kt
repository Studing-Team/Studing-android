package com.team.studing.Utils

import android.app.Application

class MyApplication : Application() {
    companion object {

        lateinit var preferences: PreferenceUtil
    }
}