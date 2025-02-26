package com.team.studing.API.request.Notice

data class SetRemindNotificationRequest(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int
)
