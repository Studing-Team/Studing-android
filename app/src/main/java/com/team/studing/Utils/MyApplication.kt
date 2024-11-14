package com.team.studing.Utils

import android.app.Application
import okhttp3.MultipartBody

class MyApplication : Application() {
    companion object {

        lateinit var preferences: PreferenceUtil

        // 회원가입 데이터 관리
        var signUpId = ""
        var signUpPassword = ""
        var signUpUniversity = ""
        var signUpMajor = ""
        var signUpStudentNum = ""
        var signUpName = ""
        var signUpStudentIDNumber = ""
        var signUpImage: MultipartBody.Part? = null
        var marketingAgreement = "false"
    }
}