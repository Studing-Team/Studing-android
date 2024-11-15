package com.team.studing.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.TokenManager
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.Home.GetStudentCouncilLogoResponse
import com.team.studing.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    var studentCouncilNameList = MutableLiveData<MutableList<String>>()
    var studentCouncilLogoList = MutableLiveData<MutableList<String>>()


    init {
        studentCouncilNameList.value = mutableListOf<String>()
        studentCouncilLogoList.value = mutableListOf<String>()
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
}