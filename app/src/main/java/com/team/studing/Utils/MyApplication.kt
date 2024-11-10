package com.team.studing.Utils

import android.app.Application

class MyApplication : Application() {
    companion object {

        lateinit var preferences: PreferenceUtil

        // 로그인 데이터 관리
        var id = ""
        var password = ""
    }
}