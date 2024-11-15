package com.team.studing.API.response.Login

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken") val accessToken: String,
    val memberData: MemberData
)


data class MemberData(
    val id: Int,
    val loginIdentifier: String,
    val name: String,
    val memberUniversity: String,
    val memberDepartment: String,
    val role: String
)


