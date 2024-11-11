package com.team.studing.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.request.SignUp.CheckIdRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    var checkIdResult: MutableLiveData<Boolean> = MutableLiveData()
    var universityList = MutableLiveData<MutableList<String>>()


    init {
        universityList.value = mutableListOf<String>()
    }

    fun checkId(activity: LoginActivity, inputId: String) {
        val apiClient = ApiClient(activity)

        apiClient.apiService.checkId(CheckIdRequest(inputId))
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

                        checkIdResult.value = true
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<Void>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                        if (response.code() == 409) {
                            checkIdResult.value = false
                        }
                    }
                }

                override fun onFailure(call: Call<BaseResponse<Void>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    fun getUniversityList(activity: LoginActivity) {

        var tempUniversityList = mutableListOf<String>()

        val apiClient = ApiClient(activity)

        apiClient.apiService.getUniversityList()
            .enqueue(object :
                Callback<BaseResponse<List<String>>> {
                override fun onResponse(
                    call: Call<BaseResponse<List<String>>>,
                    response: Response<BaseResponse<List<String>>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<List<String>>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        var university = result?.data
                        for (i in 0 until university?.size!!) {
                            var universityName = university[i]
                            tempUniversityList.add(universityName)
                        }

                        universityList.value = tempUniversityList
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<List<String>>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<List<String>>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }
}