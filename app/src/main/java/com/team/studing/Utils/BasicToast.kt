package com.team.studing.Utils

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team.studing.R
import com.team.studing.databinding.ToastBinding

object BasicToast {

    fun createToastAboveView(context: Context, message: String, anchorView: View): Toast {
        val inflater = LayoutInflater.from(context)
        val binding: ToastBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast, null, false)

        binding.textViewToast.text = message

        // View가 레이아웃에 배치된 후 실행
        anchorView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                // 레이아웃 완료 후 높이와 위치를 가져옴
                val location = IntArray(2)
                anchorView.getLocationOnScreen(location)
                val yOffset = location[1] - anchorView.height - 15.toPx()

                Toast(context).apply {
                    setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, yOffset)
                    duration = Toast.LENGTH_LONG
                    view = binding.root
                }.show()

                // 리스너 해제하여 중복 호출 방지
                anchorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        // 기본 Toast 반환 (실제 보여지는 것은 addOnGlobalLayoutListener에서 생성한 Toast)
        return Toast(context)
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}
