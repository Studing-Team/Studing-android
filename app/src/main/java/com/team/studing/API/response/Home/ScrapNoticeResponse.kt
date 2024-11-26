package com.team.studing.API.response.Home

data class ScrapNoticeResponse(
    val notices: List<ScrapNotice>,
)

data class ScrapNotice(
    val id: Long,
    val affiliation: String?,
    val title: String,
    val content: String?,
    val createdAt: String,
    val image: String?,
    val saveCheck: Boolean
)
