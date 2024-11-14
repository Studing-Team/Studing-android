package com.team.studing.API

import com.team.studing.API.request.SignUp.CheckIdRequest
import com.team.studing.API.request.SignUp.GetMajorListRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Login.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface ApiService {
    // 로그인
    @Multipart
    @POST("api/v1/member/signin")
    fun login(
        @PartMap parameters: Map<String, @JvmSuppressWildcards RequestBody>
    ): Call<BaseResponse<LoginResponse>>

    // 아이디 중복 확인
    @POST("api/v1/member/checkid")
    fun checkId(
        @Body parameters: CheckIdRequest
    ): Call<BaseResponse<Void>>

    // 학교 목록 리스트 반환
    @GET("api/v1/universityData/university")
    fun getUniversityList(): Call<BaseResponse<List<String>>>

    // 학과 목록 리스트 반환
    @POST("api/v1/universityData/department")
    fun getMajorList(
        @Body parameters: GetMajorListRequest
    ): Call<BaseResponse<List<String>>>
}