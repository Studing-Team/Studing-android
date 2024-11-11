package com.team.studing.API

import com.team.studing.API.request.Login.LoginRequest
import com.team.studing.API.request.SignUp.CheckIdRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Login.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    // 로그인
    @POST("api/v1/member/signin")
    fun login(
        @Body parameters: LoginRequest
    ): Call<BaseResponse<LoginResponse>>

    // 아이디 중복 확인
    @POST("api/v1/member/checkid")
    fun checkId(
        @Body parameters: CheckIdRequest
    ): Call<BaseResponse<Void>>

    // 학교 목록 리스트 반환
    @GET("api/v1/universityData/university")
    fun getUniversityList(): Call<BaseResponse<List<String>>>
}