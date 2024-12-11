package com.team.studing.API.response.Login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    val memberData: MemberData
)


data class MemberData(
    var id: Int,
    var loginIdentifier: String,
    var name: String,
    var memberUniversity: String,
    var memberDepartment: String,
    var role: String
)


