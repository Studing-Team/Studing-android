package com.team.studing.API.response.Home

data class GetStudentCouncilLogoResponse(
    val universityLogoImage: String?,
    val universityName: String?,
    val collegeDepartmentLogoImage: String?,
    val collegeDepartmentName: String?,
    val departmentLogoImage: String?,
    val departmentName: String?,
    val isRegisteredDepartment: Boolean
)
