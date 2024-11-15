package com.team.studing.API.response.Home

data class GetRecentNoticeResponse(
    val notices: List<Notice>
)

data class Notice(
    val id: Int,
    val title: String,
    val content: String,
    val writerInfo: String,
    val noticeLike: Int,
    val viewCount: Int,
    val saveCount: Int,
    val image: String,
    val createdAt: String,
    val saveCheck: Boolean,
    val likeCheck: Boolean
)