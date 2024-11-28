package com.team.studing.ViewModel

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.TokenManager
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Mypage.MyPageInfoResponse
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Login.LoginFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageViewModel : ViewModel() {

    var user: MutableLiveData<MyPageInfoResponse> = MutableLiveData()

    fun getUserInfo(activity: MainActivity) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.getMypageInfo("Bearer ${tokenManager.getAccessToken()}")
            .enqueue(object :
                Callback<BaseResponse<MyPageInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<MyPageInfoResponse>>,
                    response: Response<BaseResponse<MyPageInfoResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<MyPageInfoResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        user.value = result?.data!!
                        Log.d("##", "viewModel userInfo : ${user.value}")
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<MyPageInfoResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(call: Call<BaseResponse<MyPageInfoResponse>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun withdrawal(activity: MainActivity) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.withdrawal("Bearer ${tokenManager.getAccessToken()}")
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

                        activity.supportFragmentManager.popBackStack(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )

                        activity.supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerView_main, LoginFragment())
                            .addToBackStack(null)
                            .commit()

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