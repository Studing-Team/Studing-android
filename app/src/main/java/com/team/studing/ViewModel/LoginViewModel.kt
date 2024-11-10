package com.team.studing.ViewModel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.request.Login.LoginRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Login.LoginResponse
import com.team.studing.LoginActivity
import com.team.studing.MainActivity
import com.team.studing.UI.Login.DialogLogin
import com.team.studing.Utils.MyApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    fun login(activity: LoginActivity) {
        val apiClient = ApiClient(activity)

        apiClient.apiService.login(LoginRequest(MyApplication.id, MyApplication.password))
            .enqueue(object :
                Callback<BaseResponse<LoginResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<LoginResponse>>,
                    response: Response<BaseResponse<LoginResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<LoginResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        val mainIntent = Intent(activity, MainActivity::class.java)
                        mainIntent.putExtra("isLogin", true)
                        activity.startActivity(mainIntent)
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<LoginResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                        if (response.code() == 401) {
                            val dialog = DialogLogin()
                            activity.let {
                                dialog.show(activity.manager, "LoginDialog")
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<LoginResponse>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }
}