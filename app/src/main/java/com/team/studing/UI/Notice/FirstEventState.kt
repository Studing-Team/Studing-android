package com.team.studing.UI.Notice

import com.team.studing.R

enum class FirstEventState(val text: String, val colorId: Int, val isEnabled: Boolean) {
    ACTIVE("선착순 이벤트 참여하기", R.color.red, true),
    COMPLETE("내 순위 확인하기", R.color.black_50, true),
    FINISH("종료된 이벤트", R.color.black_20, false),
    BEFORE("선착순 이벤트 참여하기", R.color.black_20, false)
}