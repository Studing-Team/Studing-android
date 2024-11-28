package com.team.studing.API.response.Mypage

data class MyPageInfoResponse(
    val admissionNumber: Int,
    val name: String,
    val memberUniversity: String,
    val memberDepartment: String,
    val role: String
)
