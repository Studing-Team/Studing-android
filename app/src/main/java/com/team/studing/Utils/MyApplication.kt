package com.team.studing.Utils

import android.app.Application
import com.team.studing.API.response.Login.MemberData
import com.team.studing.API.response.PartnerShip.Partner
import okhttp3.MultipartBody

class MyApplication : Application() {
    companion object {

        lateinit var preferences: PreferenceUtil

        var notificationNoticeType = ""
        var notificationNoticeId = ""

        // 카테고리 리스트
        var categoryList = listOf("전체", "총학생회", "단과대", "학과")
        var noticeCategory = 0


        // 회원가입 데이터 관리
        var signUpId = ""
        var signUpPassword = ""
        var signUpUniversity = ""
        var signUpMajor = ""
        var signUpStudentNum = ""
        var signUpName = ""
        var signUpStudentIDNumber = ""
        var signUpImage: MultipartBody.Part? = null
        var marketingAgreement = "false"

        // 학생증 재제출 이미지
        var reSubmitImage: MultipartBody.Part? = null
        var reSubmit = false

        // 유저 정보
        var memberData: MemberData? = null
        var memberId = -1

        // 선택한 제휴업체 정보
        var selectedPartnerShip: Partner? = null

        // 현재 공지사항
        var noticeId = 0

        // 공지사항 등록 이미지
        var noticeImages: List<MultipartBody.Part>? = null
    }
}