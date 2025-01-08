package com.team.studing.ViewModel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.TokenManager
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Home.NoticeDetailResponse
import com.team.studing.MainActivity
import com.team.studing.Utils.MyApplication
import com.team.studing.Utils.SingleLiveEvent
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeViewModel : ViewModel() {

    var isLiked = SingleLiveEvent<Boolean>()
    var isScraped = SingleLiveEvent<Boolean>()
    var isViewed = SingleLiveEvent<Boolean>()

    // 공지사항 세부 내용
    var noticeDetail: MutableLiveData<NoticeDetailResponse?> = MutableLiveData()


    // 공지사항 세부 화면 조회
    fun getNoticeDetail(activity: Activity, noticeId: Int) {

        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getNoticeDetail(
            "Bearer ${tokenManager.getAccessToken()}",
            noticeId
        )
            .enqueue(object :
                Callback<BaseResponse<NoticeDetailResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<NoticeDetailResponse>>,
                    response: Response<BaseResponse<NoticeDetailResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<NoticeDetailResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        noticeDetail.value = result?.data

                        viewNotice(activity, noticeId)
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<NoticeDetailResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<NoticeDetailResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun likeNotice(activity: Activity, noticeId: Int) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.likeNotice("Bearer ${tokenManager.getAccessToken()}", noticeId)
            .enqueue(object :
                Callback<BaseResponse<Void>> {
                override fun onResponse(
                    call: Call<BaseResponse<Void>>,
                    response: Response<BaseResponse<Void>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        isLiked.value = true
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun cancelLikeNotice(activity: Activity, noticeId: Int) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.cancelLikeNotice("Bearer ${tokenManager.getAccessToken()}", noticeId)
            .enqueue(object :
                Callback<BaseResponse<Void>> {
                override fun onResponse(
                    call: Call<BaseResponse<Void>>,
                    response: Response<BaseResponse<Void>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        isLiked.value = false
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun scrapNotice(activity: Activity, noticeId: Int) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.scrapNotice("Bearer ${tokenManager.getAccessToken()}", noticeId)
            .enqueue(object :
                Callback<BaseResponse<Void>> {
                override fun onResponse(
                    call: Call<BaseResponse<Void>>,
                    response: Response<BaseResponse<Void>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        isScraped.value = true
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun cancelScrapNotice(activity: Activity, noticeId: Int) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.cancelScrapNotice("Bearer ${tokenManager.getAccessToken()}", noticeId)
            .enqueue(object :
                Callback<BaseResponse<Void>> {
                override fun onResponse(
                    call: Call<BaseResponse<Void>>,
                    response: Response<BaseResponse<Void>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        isScraped.value = false
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun viewNotice(activity: Activity, noticeId: Int) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.viewCheckNotice("Bearer ${tokenManager.getAccessToken()}", noticeId)
            .enqueue(object :
                Callback<BaseResponse<Void>> {
                override fun onResponse(
                    call: Call<BaseResponse<Void>>,
                    response: Response<BaseResponse<Void>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        isViewed.value = result?.status == 201

                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun registerNotice(
        activity: Activity,
        title: String,
        content: String,
        tag: String
    ) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        // DTO의 각 필드를 RequestBody로 변환하고 Map에 추가
        val params = HashMap<String, RequestBody>()
        params["title"] =
            title.toRequestBody("text/plain".toMediaTypeOrNull())
        params["content"] =
            content.toRequestBody("text/plain".toMediaTypeOrNull())
        params["tag"] =
            tag.toRequestBody("text/plain".toMediaTypeOrNull())

        apiClient.apiService.registerNotice(
            "Bearer ${tokenManager.getAccessToken()}",
            params,
            MyApplication.noticeImages
        )
            .enqueue(object :
                Callback<BaseResponse<Void>> {
                override fun onResponse(
                    call: Call<BaseResponse<Void>>,
                    response: Response<BaseResponse<Void>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())
                        activity.finish()
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }
}