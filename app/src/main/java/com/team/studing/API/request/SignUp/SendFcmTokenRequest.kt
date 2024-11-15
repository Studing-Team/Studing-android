package com.team.studing.API.request.SignUp

data class SendFcmTokenRequest(
    val fcmToken: String,
    val memberId: Int
)