package com.team.studing.API

import com.team.studing.API.request.Home.CategoryRequest
import com.team.studing.API.request.PartnerShip.GetPartnerShipInfoRequest
import com.team.studing.API.request.SignUp.CheckIdRequest
import com.team.studing.API.request.SignUp.GetMajorListRequest
import com.team.studing.API.request.SignUp.SendFcmTokenRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Home.GetFirstEventRankingResponse
import com.team.studing.API.response.Home.GetStudentCouncilLogoResponse
import com.team.studing.API.response.Home.GetUnreadNoticeCountResponse
import com.team.studing.API.response.Home.GetUnreadNoticeListResponse
import com.team.studing.API.response.Home.GetUnreadStudentCouncilResponse
import com.team.studing.API.response.Home.NoticeDetailResponse
import com.team.studing.API.response.Home.NoticeListResponse
import com.team.studing.API.response.Home.ScrapNoticeResponse
import com.team.studing.API.response.Login.LoginResponse
import com.team.studing.API.response.Mypage.MyPageInfoResponse
import com.team.studing.API.response.PartnerShip.GetPartnerShipInfoResponse
import com.team.studing.API.response.SignUp.SignUpResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path

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

    // 학생증 재제출
    @Multipart
    @POST("api/v1/member/resubmit")
    fun reSubmit(
        @Header("Authorization") token: String,
        @PartMap parameters: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part studentCardImage: MultipartBody.Part
    ): Call<BaseResponse<Void>>

    // FCM 토큰 저장
    @POST("api/v1/notifications/token")
    fun sendFcmToken(
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

    // 카테고리별 안읽은 공지 개수 확인
    @POST("api/v1/home/unread-notice-count")
    fun getUnreadNoticeCount(
        @Header("Authorization") token: String,
        @Body parameters: CategoryRequest
    ): Call<BaseResponse<GetUnreadNoticeCountResponse>>

    // 카테고리별 놓친 공지 리스트
    @POST("api/v1/notices/unread/all")
    fun getUnreadNoticeList(
        @Header("Authorization") token: String,
        @Body parameters: CategoryRequest
    ): Call<BaseResponse<GetUnreadNoticeListResponse>>

    // 메인 홈 카테고리별 최신 공지 리스트
    @POST("api/v1/home/recent-notices")
    fun getRecentNotice(
        @Header("Authorization") token: String,
        @Body parameters: CategoryRequest
    ): Call<BaseResponse<NoticeListResponse>>

    // 전체 공지 리스트 (전체)
    @GET("api/v1/notices/all")
    fun getNoticeList(
        @Header("Authorization") token: String
    ): Call<BaseResponse<NoticeListResponse>>

    // 전체 공지 리스트 (학생회)
    @POST("api/v1/notices/all-category")
    fun getNoticeStudentCouncilList(
        @Header("Authorization") token: String,
        @Body parameters: CategoryRequest
    ): Call<BaseResponse<NoticeListResponse>>

    // 메인 홈 저장한 공지 리스트
    @GET("api/v1/home/save")
    fun getRecentScrapNotice(
        @Header("Authorization") token: String
    ): Call<BaseResponse<ScrapNoticeResponse>>

    // 저장한 전체 카테고리별 공지 리스트
    @POST("api/v1/notices/save-category")
    fun getScrapNoticeListByCategory(
        @Header("Authorization") token: String,
        @Body parameters: CategoryRequest
    ): Call<BaseResponse<ScrapNoticeResponse>>

    // 공지사항 세부 화면
    @GET("api/v1/notices/{noticeId}")
    fun getNoticeDetail(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<NoticeDetailResponse>>

    // 공지사항 좋아요
    @POST("api/v1/notices/like/{noticeId}")
    fun likeNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 공지사항 좋아요 취소
    @DELETE("api/v1/notices/like/{noticeId}")
    fun cancelLikeNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 공지사항 저장하기
    @POST("api/v1/notices/save/{noticeId}")
    fun scrapNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 공지사항 저장 취소
    @DELETE("api/v1/notices/save/{noticeId}")
    fun cancelScrapNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 공지사항 조회수
    @POST("api/v1/notices/view-check/{noticeId}")
    fun viewCheckNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 공지사항 등록하기
    @Multipart
    @POST("api/v1/notices/create")
    fun registerNotice(
        @Header("Authorization") token: String,
        @PartMap parameters: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part noticeImages: List<MultipartBody.Part>?
    ): Call<BaseResponse<Void>>

    // 선착순 이벤트 신청
    @POST("api/v1/notices/first-come/{noticeId}")
    fun joinFirstEvent(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 선착순 이벤트 순위 조회
    @GET("api/v1/notices/first-come/rankings/{noticeId}")
    fun getFirstEventRanking(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<GetFirstEventRankingResponse>>

    // 공지사항 수정하기
    @Multipart
    @PUT("api/v1/notices/{noticeId}")
    fun editNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int,
        @PartMap parameters: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part noticeImages: List<MultipartBody.Part>?
    ): Call<BaseResponse<Void>>

    // 공지사항 삭제하기
    @DELETE("api/v1/notices/{noticeId}")
    fun deleteNotice(
        @Header("Authorization") token: String,
        @Path("noticeId") noticeId: Int
    ): Call<BaseResponse<Void>>

    // 제휴 업체 데이터 리스트 반환
    @POST("api/v1/partner")
    fun getPartnerShipInfo(
        @Header("Authorization") token: String,
        @Body parameters: GetPartnerShipInfoRequest
    ): Call<BaseResponse<GetPartnerShipInfoResponse>>

    // 사용자 정보 조회 (마이페이지)
    @GET("api/v1/home/mydata")
    fun getMypageInfo(
        @Header("Authorization") token: String,
    ): Call<BaseResponse<MyPageInfoResponse>>

    // 회원 탈퇴
    @DELETE("api/v1/member/withdraw")
    fun withdrawal(
        @Header("Authorization") token: String,
    ): Call<BaseResponse<Void>>
}