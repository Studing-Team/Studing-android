package com.team.studing.API.response.Home

import java.time.LocalTime

data class NoticeDetailResponse(
    val id: Int,
    val title: String,
    val content: String,
    var likeCount: Int,
    var saveCount: Int,
    var readCount: Int,
    val createdAt: String,
    val affilitionName: String,
    val logoImage: String,
    val tag: String,
    val images: List<String>,
    var saveCheck: Boolean,
    var likeCheck: Boolean,
    var isAuthor: Boolean,
    var startTime: String?,
    var endTime: String?,
    var isFirstComeNotice: Boolean,
    var isFirstComeApplied: Boolean,
    var alarmTime: LocalTime?
)