package com.team.studing.API.request.PartnerShip

data class GetPartnerShipInfoRequest(
    // ‘음식점’,’카페’,’운동’,문화’,주점’,’전체’ 중 1개
    val categorie: String
)
