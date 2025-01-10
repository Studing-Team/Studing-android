package com.team.studing.ViewModel

import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.TokenManager
import com.team.studing.API.request.SignUp.CheckIdRequest
import com.team.studing.API.request.SignUp.GetMajorListRequest
import com.team.studing.API.request.SignUp.SendFcmTokenRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.SignUp.SignUpResponse
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.ReSubmitActivity
import com.team.studing.UI.SignUp.SignUpWaitingFragment
import com.team.studing.Utils.MyApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpViewModel : ViewModel() {

    var checkIdResult: MutableLiveData<Boolean> = MutableLiveData()
    var universityList = MutableLiveData<MutableList<String>>()
    var majorList = MutableLiveData<MutableList<String>>()


    init {
        universityList.value = mutableListOf<String>()
        majorList.value = mutableListOf<String>()
    }

    // 아이디 중복 확인 API
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

    // 학교 목록 리스트 반환 API
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

    // 학과 목록 리스트 반환 API
    fun getMajorList(activity: LoginActivity, university: String) {

        var tempMajorList = mutableListOf<String>()

        val apiClient = ApiClient(activity)

        apiClient.apiService.getMajorList(GetMajorListRequest(university))
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

                        var major = result?.data
                        for (i in 0 until major?.size!!) {
                            var majorName = major[i]
                            tempMajorList.add(majorName)
                        }

                        majorList.value = tempMajorList
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

    // 회원가입 API
    fun signUp(activity: LoginActivity) {
        val apiClient = ApiClient(activity)

        // DTO의 각 필드를 RequestBody로 변환하고 Map에 추가
        val params = HashMap<String, RequestBody>()
        params["loginIdentifier"] =
            MyApplication.signUpId.toRequestBody("text/plain".toMediaTypeOrNull())
        params["password"] =
            MyApplication.signUpPassword.toRequestBody("text/plain".toMediaTypeOrNull())
        params["admissionNumber"] =
            MyApplication.signUpStudentNum.toRequestBody("text/plain".toMediaTypeOrNull())
        params["studentNumber"] =
            MyApplication.signUpStudentIDNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        params["name"] = MyApplication.signUpName.toRequestBody("text/plain".toMediaTypeOrNull())
        params["memberUniversity"] =
            MyApplication.signUpUniversity.toRequestBody("text/plain".toMediaTypeOrNull())
        params["memberDepartment"] =
            MyApplication.signUpMajor.toRequestBody("text/plain".toMediaTypeOrNull())
        params["marketingAgreement"] =
            MyApplication.marketingAgreement.toRequestBody("text/plain".toMediaTypeOrNull())


        apiClient.apiService.signUp(
            params,
            MyApplication.signUpImage!!
        )
            .enqueue(object :
                Callback<BaseResponse<SignUpResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<SignUpResponse>>,
                    response: Response<BaseResponse<SignUpResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<SignUpResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        MyApplication.memberId = result?.data?.memberId!!.toInt()

                        val nextFragment = SignUpWaitingFragment()

                        // 이전 BackStack의 모든 Fragment 제거
                        activity.supportFragmentManager.popBackStackImmediate(
                            null,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE
                        )

                        val transaction = activity.supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                        transaction.commit()
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<SignUpResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")
                    }
                }

                override fun onFailure(call: Call<BaseResponse<SignUpResponse>>, t: Throwable) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }

    // 회원가입 API
    fun reSubmit(activity: ReSubmitActivity) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        // DTO의 각 필드를 RequestBody로 변환하고 Map에 추가
        val params = HashMap<String, RequestBody>()

        params["admissionNumber"] =
            MyApplication.signUpStudentNum.toRequestBody("text/plain".toMediaTypeOrNull())
        params["name"] = MyApplication.signUpName.toRequestBody("text/plain".toMediaTypeOrNull())

        apiClient.apiService.reSubmit(
            "Bearer ${tokenManager.getAccessToken()}",
            params,
            MyApplication.reSubmitImage!!
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

                        MyApplication.reSubmit = true
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

    // FCM 토큰 저장 API
    fun sendFcmToken(activity: LoginActivity) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        apiClient.apiService.sendFcmToken(
            SendFcmTokenRequest(
                MyApplication.preferences.getFCMToken().toString(),
                MyApplication.memberId,
                "ANDROID"
            )
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

                override fun onFailure(
                    call: Call<BaseResponse<Void>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }
}