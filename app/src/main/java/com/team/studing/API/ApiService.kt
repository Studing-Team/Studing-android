package com.team.studing.API

import com.team.studing.API.request.Home.GetRecentNoticeRequest
import com.team.studing.API.request.SignUp.CheckIdRequest
import com.team.studing.API.request.SignUp.GetMajorListRequest
import com.team.studing.API.request.SignUp.SendFcmTokenRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Home.GetRecentNoticeResponse
import com.team.studing.API.response.Home.GetStudentCouncilLogoResponse
import com.team.studing.API.response.Home.GetUnreadStudentCouncilResponse
import com.team.studing.API.response.Login.LoginResponse
import com.team.studing.API.response.SignUp.SignUpResponse
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

    // 회원가입
    @Multipart
    @POST("api/v1/member/signup")
    fun signUp(
        @PartMap parameters: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part studentCardImage: MultipartBody.Part
    ): Call<BaseResponse<SignUpResponse>>

    // FCM 토큰 저장
    @POST("api/v1/notifications/token")
    fun sendFcmToken(
        @Header("Authorization") token: String,
        @Body parameters: SendFcmTokenRequest
    ): Call<BaseResponse<Void>>

    // 학생회 카테고리 로고 리스트 반환
    @GET("api/v1/home/logo")
    fun getStudentCouncilLogo(
        @Header("Authorization") token: String,
    ): Call<BaseResponse<GetStudentCouncilLogoResponse>>

    // 학생회 카테고리 안읽은 공지 체크
    @GET("api/v1/home/unread-categories")
    fun getUnreadStudentCouncil(
        @Header("Authorization") token: String,
    ): Call<BaseResponse<GetUnreadStudentCouncilResponse>>

    // 메인 홈 카테고리별 최신 공지 리스트
    @POST("api/v1/home/recent-notices")
    fun getRecentNotice(
        @Header("Authorization") token: String,
        @Body parameters: GetRecentNoticeRequest
    ): Call<BaseResponse<GetRecentNoticeResponse>>
}