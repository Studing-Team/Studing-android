package com.team.studing.API.response.Home

data class NoticeListResponse(
    val notices: List<Notice>
)

data class Notice(
    val id: Int,
    val affiliation: String?,
    val title: String,
    val content: String,
    val tag: String?,
    val writerInfo: String?,
    val noticeLike: Int,
    val viewCount: Int,
    val saveCount: Int,
    val image: String,
    val createdAt: String,
    val saveCheck: Boolean,
    val likeCheck: Boolean
)