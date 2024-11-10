package com.team.studing.API

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    // 로그인
    @POST("api/v1/member/signin")
    fun login(
        @Body parameters: LoginRequest
    ): Call<BaseResponse<LoginResponse>>
}