package com.team.studing.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.TokenManager
import com.team.studing.API.request.Home.CategoryRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Home.GetStudentCouncilLogoResponse
import com.team.studing.API.response.Home.GetUnreadStudentCouncilResponse
import com.team.studing.API.response.Home.Notice
import com.team.studing.API.response.Home.NoticeListResponse
import com.team.studing.API.response.Home.ScrapNotice
import com.team.studing.API.response.Home.ScrapNoticeResponse
import com.team.studing.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    // 학생회 카테고리 로고
    var studentCouncilNameList = MutableLiveData<MutableList<String>>()
    var studentCouncilLogoList = MutableLiveData<MutableList<String>>()
    var majorStudentCouncil: MutableLiveData<Boolean> = MutableLiveData()

    // 학생회 카테고리 안읽은 공지 체크
    var unreadStudentCouncilNameList = MutableLiveData<MutableList<String>>()

    // 학생회 카테고리별 최신 공지
    var recentNoticeList = MutableLiveData<MutableList<Notice>>()

    // 전체 공지 리스트
    var noticeList = MutableLiveData<MutableList<Notice>>()

    // 학생회별 공지 리스트
    var studentCouncilNoticeList = MutableLiveData<MutableList<Notice>>()

    // 저장한 공지 리스트
    var recentScrapNoticeList = MutableLiveData<MutableList<ScrapNotice>>()
    var scrapNoticeList = MutableLiveData<MutableList<ScrapNotice>>()


    init {
        studentCouncilNameList.value = mutableListOf<String>()
        studentCouncilLogoList.value = mutableListOf<String>()
        unreadStudentCouncilNameList.value = mutableListOf<String>()
        recentNoticeList.value = mutableListOf<Notice>()
        noticeList.value = mutableListOf<Notice>()
        studentCouncilNoticeList.value = mutableListOf<Notice>()
        recentScrapNoticeList.value = mutableListOf<ScrapNotice>()
        scrapNoticeList.value = mutableListOf<ScrapNotice>()
    }

    fun getStudentCouncilLogo(activity: MainActivity) {

        var tempNameList = mutableListOf<String>()
        var tempImageList = mutableListOf<String>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getStudentCouncilLogo("Bearer ${tokenManager.getAccessToken()}")
            .enqueue(object :
                Callback<BaseResponse<GetStudentCouncilLogoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetStudentCouncilLogoResponse>>,
                    response: Response<BaseResponse<GetStudentCouncilLogoResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<GetStudentCouncilLogoResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        tempNameList.add("전체")
                        tempNameList.add(result?.data?.universityName ?: "noStudentCouncil")
                        tempNameList.add(result?.data?.collegeDepartmentName ?: "noStudentCouncil")
                        tempNameList.add(result?.data?.departmentName ?: "noStudentCouncil")

                        tempImageList.add("whole_default")
                        tempImageList.add(result?.data?.universityLogoImage ?: "default")
                        tempImageList.add(result?.data?.collegeDepartmentLogoImage ?: "default")
                        tempImageList.add(result?.data?.departmentLogoImage ?: "default")

                        studentCouncilNameList.value = tempNameList
                        studentCouncilLogoList.value = tempImageList

                        majorStudentCouncil.value = result?.data?.isRegisteredDepartment

                        Log.d("##", "viewModel temp : ${tempNameList}")
                        Log.d("##", "viewModel : ${studentCouncilNameList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<GetStudentCouncilLogoResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<GetStudentCouncilLogoResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun getUnreadStudentCouncil(activity: MainActivity) {

        var tempUnreadNameList = mutableListOf<String>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getUnreadStudentCouncil("Bearer ${tokenManager.getAccessToken()}")
            .enqueue(object :
                Callback<BaseResponse<GetUnreadStudentCouncilResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetUnreadStudentCouncilResponse>>,
                    response: Response<BaseResponse<GetUnreadStudentCouncilResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<GetUnreadStudentCouncilResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        if (!(result?.data?.categories.isNullOrEmpty())) {
                            for (i in 0 until result?.data?.categories?.size!!) {
                                tempUnreadNameList.add(result.data.categories[i])
                            }
                        }

                        unreadStudentCouncilNameList.value = tempUnreadNameList

                        Log.d("##", "viewModel temp : ${tempUnreadNameList}")
                        Log.d("##", "viewModel : ${unreadStudentCouncilNameList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<GetUnreadStudentCouncilResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<GetUnreadStudentCouncilResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun getRecentNotice(activity: MainActivity, category: String) {

        var tempRecentList = mutableListOf<Notice>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getRecentNotice(
            "Bearer ${tokenManager.getAccessToken()}",
            CategoryRequest(category)
        )
            .enqueue(object :
                Callback<BaseResponse<NoticeListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<NoticeListResponse>>,
                    response: Response<BaseResponse<NoticeListResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<NoticeListResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        if (!(result?.data?.notices.isNullOrEmpty())) {
                            for (i in 0 until result?.data?.notices?.size!!) {
                                var id = result.data.notices[i].id
                                var affiliation = result.data.notices[i].affiliation
                                var title = result.data.notices[i].title
                                var content = result.data.notices[i].content
                                var writerInfo = result.data.notices[i].writerInfo
                                var likeCount = result.data.notices[i].noticeLike
                                var saveCount = result.data.notices[i].saveCount
                                var readCount = result.data.notices[i].viewCount
                                var noticeDate = result.data.notices[i].createdAt
                                var saveCheck = result.data.notices[i].saveCheck
                                var likeCheck = result.data.notices[i].likeCheck
                                var noticeImage = result.data.notices[i].image

                                var n1 = Notice(
                                    id,
                                    affiliation,
                                    title,
                                    content,
                                    null,
                                    writerInfo,
                                    likeCount,
                                    readCount,
                                    saveCount,
                                    noticeImage,
                                    noticeDate,
                                    saveCheck,
                                    likeCheck,
                                )
                                tempRecentList.add(n1)
                            }
                        }

                        recentNoticeList.value = tempRecentList

                        Log.d("##", "viewModel temp : ${tempRecentList}")
                        Log.d("##", "viewModel : ${recentNoticeList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<NoticeListResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<NoticeListResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun getNoticeList(activity: MainActivity) {

        var tempNoticeList = mutableListOf<Notice>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getNoticeList(
            "Bearer ${tokenManager.getAccessToken()}"
        )
            .enqueue(object :
                Callback<BaseResponse<NoticeListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<NoticeListResponse>>,
                    response: Response<BaseResponse<NoticeListResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<NoticeListResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        if (!(result?.data?.notices.isNullOrEmpty())) {
                            for (i in 0 until result?.data?.notices?.size!!) {
                                var id = result.data.notices[i].id
                                var affiliation = result.data.notices[i].affiliation
                                var title = result.data.notices[i].title
                                var content = result.data.notices[i].content
                                var writerInfo = result.data.notices[i].writerInfo
                                var likeCount = result.data.notices[i].noticeLike
                                var saveCount = result.data.notices[i].saveCount
                                var readCount = result.data.notices[i].viewCount
                                var noticeDate = result.data.notices[i].createdAt
                                var saveCheck = result.data.notices[i].saveCheck
                                var likeCheck = result.data.notices[i].likeCheck
                                var noticeImage = result.data.notices[i].image

                                var n1 = Notice(
                                    id,
                                    affiliation,
                                    title,
                                    content,
                                    null,
                                    writerInfo,
                                    likeCount,
                                    readCount,
                                    saveCount,
                                    noticeImage,
                                    noticeDate,
                                    saveCheck,
                                    likeCheck,
                                )
                                tempNoticeList.add(n1)
                            }
                        }

                        noticeList.value = tempNoticeList

                        Log.d("##", "viewModel temp : ${tempNoticeList}")
                        Log.d("##", "viewModel : ${noticeList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<NoticeListResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<NoticeListResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun getStudentCouncilNoticeList(activity: MainActivity, category: String) {

        var tempStudentCouncilNoticeList = mutableListOf<Notice>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getNoticeStudentCouncilList(
            "Bearer ${tokenManager.getAccessToken()}",
            CategoryRequest(category)
        )
            .enqueue(object :
                Callback<BaseResponse<NoticeListResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<NoticeListResponse>>,
                    response: Response<BaseResponse<NoticeListResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<NoticeListResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        if (!(result?.data?.notices.isNullOrEmpty())) {
                            for (i in 0 until result?.data?.notices?.size!!) {
                                var id = result.data.notices[i].id
                                var affiliation = result.data.notices[i].affiliation
                                var title = result.data.notices[i].title
                                var content = result.data.notices[i].content
                                var tag = result.data.notices[i].tag
                                var likeCount = result.data.notices[i].noticeLike
                                var saveCount = result.data.notices[i].saveCount
                                var readCount = result.data.notices[i].viewCount
                                var noticeDate = result.data.notices[i].createdAt
                                var saveCheck = result.data.notices[i].saveCheck
                                var likeCheck = result.data.notices[i].likeCheck
                                var noticeImage = result.data.notices[i].image

                                var n1 = Notice(
                                    id,
                                    affiliation,
                                    title,
                                    content,
                                    tag,
                                    null,
                                    likeCount,
                                    readCount,
                                    saveCount,
                                    noticeImage,
                                    noticeDate,
                                    saveCheck,
                                    likeCheck,
                                )
                                tempStudentCouncilNoticeList.add(n1)
                            }
                        }

                        studentCouncilNoticeList.value = tempStudentCouncilNoticeList

                        Log.d("##", "viewModel temp : ${tempStudentCouncilNoticeList}")
                        Log.d("##", "viewModel : ${studentCouncilNoticeList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<NoticeListResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<NoticeListResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    // 메인 홈 저장한 공지 리스트
    fun getRecentScrapNotice(activity: MainActivity) {

        var tempScrapList = mutableListOf<ScrapNotice>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getRecentScrapNotice(
            "Bearer ${tokenManager.getAccessToken()}"
        )
            .enqueue(object :
                Callback<BaseResponse<ScrapNoticeResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ScrapNoticeResponse>>,
                    response: Response<BaseResponse<ScrapNoticeResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<ScrapNoticeResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        if (!(result?.data?.notices.isNullOrEmpty())) {
                            for (i in 0 until result?.data?.notices?.size!!) {
                                var id = result.data.notices[i].id
                                var affiliation = result.data.notices[i].affiliation
                                var title = result.data.notices[i].title
                                var content = result.data.notices[i].content
                                var noticeDate = result.data.notices[i].createdAt
                                var saveCheck = result.data.notices[i].saveCheck

                                var n1 = ScrapNotice(
                                    id,
                                    affiliation,
                                    title,
                                    content,
                                    noticeDate,
                                    null,
                                    saveCheck
                                )
                                tempScrapList.add(n1)
                            }
                        }

                        recentScrapNoticeList.value = tempScrapList

                        Log.d("##", "viewModel temp : ${tempScrapList}")
                        Log.d("##", "viewModel : ${recentScrapNoticeList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<ScrapNoticeResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ScrapNoticeResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    // 저장한 전체 카테고리별 공지 리스트
    fun getScrapNoticeList(activity: MainActivity, category: String) {

        var tempScrapList = mutableListOf<ScrapNotice>()

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getScrapNoticeListByCategory(
            "Bearer ${tokenManager.getAccessToken()}",
            CategoryRequest(category)
        )
            .enqueue(object :
                Callback<BaseResponse<ScrapNoticeResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<ScrapNoticeResponse>>,
                    response: Response<BaseResponse<ScrapNoticeResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<ScrapNoticeResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        if (!(result?.data?.notices.isNullOrEmpty())) {
                            for (i in 0 until result?.data?.notices?.size!!) {
                                var id = result.data.notices[i].id
                                var title = result.data.notices[i].title
                                var noticeDate = result.data.notices[i].createdAt
                                var image = result.data.notices[i].image
                                var saveCheck = result.data.notices[i].saveCheck

                                var n1 = ScrapNotice(
                                    id,
                                    null,
                                    title,
                                    null,
                                    noticeDate,
                                    image,
                                    saveCheck
                                )
                                tempScrapList.add(n1)
                            }
                        }

                        scrapNoticeList.value = tempScrapList

                        Log.d("##", "viewModel temp : ${tempScrapList}")
                        Log.d("##", "viewModel : ${scrapNoticeList.value}")

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<ScrapNoticeResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<ScrapNoticeResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }
}