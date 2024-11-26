package com.team.studing.API.response.Home

data class NoticeDetailResponse(
    val id: Int,
    val title: String,
    val content: String,
    val likeCount: Int,
    val saveCount: Int,
    val readCount: Int,
    val createdAt: String,
    val affilitionName: String,
    val logoImage: String,
    val tag: String,
    val images: List<String>,
    val saveCheck: Boolean,
    val likeCheck: Boolean
)