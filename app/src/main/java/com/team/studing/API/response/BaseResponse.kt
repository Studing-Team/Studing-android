package com.team.studing.API.response

data class BaseResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)