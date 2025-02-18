package com.team.studing.API.response.Home

data class GetFirstEventRankingResponse(
    val rankings: List<RankingItem>,
    val myRanking: Int
)

data class RankingItem(
    val orderNumber: Int,
    val applyDateTime: String,
    val maskedStudentNumber: String
)
