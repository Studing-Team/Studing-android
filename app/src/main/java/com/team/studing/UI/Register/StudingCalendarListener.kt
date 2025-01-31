package com.team.studing.UI.Register

import java.util.Calendar

interface StudingCalendarListener {
    fun onFirstDateSelected(startDate: Calendar)
    fun onDateRangeSelected(startDate: Calendar, endDate: Calendar)
}