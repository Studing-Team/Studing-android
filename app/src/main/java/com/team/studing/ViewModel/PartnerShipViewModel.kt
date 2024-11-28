package com.team.studing.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.team.studing.API.ApiClient
import com.team.studing.API.TokenManager
import com.team.studing.API.request.PartnerShip.GetPartnerShipInfoRequest
import com.team.studing.API.response.BaseResponse
import com.team.studing.API.response.PartnerShip.GetPartnerShipInfoResponse
import com.team.studing.API.response.PartnerShip.Partner
import com.team.studing.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PartnerShipViewModel : ViewModel() {

    var partners: MutableLiveData<List<Partner>> = MutableLiveData()

    init {
        partners.value = mutableListOf<Partner>()
    }

    fun getPartnerShipInfo(activity: MainActivity, category: String) {
        val apiClient = ApiClient(activity)
        val tokenManager = TokenManager(activity)

        var tempPartnerShipList = mutableListOf<Partner>()

        apiClient.apiService.getPartnerShipInfo(
            "Bearer ${tokenManager.getAccessToken()}",
            GetPartnerShipInfoRequest(category)
        )
            .enqueue(object :
                Callback<BaseResponse<GetPartnerShipInfoResponse>> {
                override fun onResponse(
                    call: Call<BaseResponse<GetPartnerShipInfoResponse>>,
                    response: Response<BaseResponse<GetPartnerShipInfoResponse>>
                ) {
                    Log.d("##", "onResponse 성공: " + response.body().toString())
                    if (response.isSuccessful) {
                        // 정상적으로 통신이 성공된 경우
                        val result: BaseResponse<GetPartnerShipInfoResponse>? = response.body()
                        Log.d("##", "onResponse 성공: " + result?.toString())

                        for (i in 0 until result?.data?.partners!!.size) {
                            var id = result.data.partners[i].id
                            var partnerName = result.data.partners[i].partnerName
                            var partnerDescription = result.data.partners[i].partnerDescription
                            var partnerAddress = result.data.partners[i].partnerAddress
                            var category = result.data.partners[i].category
                            var partnerEvent = result.data.partners[i].partnerContent
                            var latitude = result.data.partners[i].latitude
                            var longitude = result.data.partners[i].longitude
                            var partnerLogo = result.data.partners[i].partnerLogo

                            var p1 = Partner(
                                id,
                                partnerName,
                                partnerDescription,
                                partnerAddress,
                                category,
                                partnerEvent,
                                latitude,
                                longitude,
                                partnerLogo
                            )
                            tempPartnerShipList.add(p1)
                        }

                        partners.value = tempPartnerShipList
                    } else {
                        // 통신이 실패한 경우(응답코드 3xx, 4xx 등)
                        var result: BaseResponse<GetPartnerShipInfoResponse>? = response.body()
                        Log.d("##", "onResponse 실패")
                        Log.d("##", "onResponse 실패: " + response.code())
                        Log.d("##", "onResponse 실패: " + response.body())
                        val errorBody = response.errorBody()?.string() // 에러 응답 데이터를 문자열로 얻음
                        Log.d("##", "Error Response: $errorBody")

                    }
                }

                override fun onFailure(
                    call: Call<BaseResponse<GetPartnerShipInfoResponse>>,
                    t: Throwable
                ) {
                    // 통신 실패
                    Log.d("##", "onFailure 에러: " + t.message.toString())
                }
            })
    }
}