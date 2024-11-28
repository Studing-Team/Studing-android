package com.team.studing.API.response.PartnerShip

data class GetPartnerShipInfoResponse(
    val partners: List<Partner>
)

data class Partner(
    val id: Int,
    val partnerName: String,
    val partnerDescription: String,
    val partnerAddress: String,
    val category: String,
    val partnerContent: String,
    val latitude: Double,
    val longitude: Double,
    val partnerLogo: String
)