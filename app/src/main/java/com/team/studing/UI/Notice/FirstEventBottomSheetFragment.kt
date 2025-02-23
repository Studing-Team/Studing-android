package com.team.studing.UI.Notice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.API.response.Home.GetFirstEventRankingResponse
import com.team.studing.UI.Notice.Adapter.FirstEventResultAdapter
import com.team.studing.databinding.FragmentFirstEventBottomSheetBinding

interface FirstEventBottomSheetInterface {
    fun onClickMyRankingButton(id: Int)
    fun onClickCloseButton(id: Int)
}

class FirstEventBottomSheetFragment(var result: GetFirstEventRankingResponse?) : DialogFragment() {

    lateinit var binding: FragmentFirstEventBottomSheetBinding
    private lateinit var firstEventResultAdapter: FirstEventResultAdapter

    // 인터페이스 인스턴스
    private var listener: FirstEventBottomSheetInterface? = null

    // 리스너 설정 메서드
    fun setFirstEventDialogInterface(listener: FirstEventBottomSheetInterface) {
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()

        // 다이얼로그를 하단에 배치하고 마진 설정
        dialog?.window?.let { window ->
            val params = window.attributes

            // 화면 너비에서 좌우 마진과 하단 마진 계산
            val marginHorizontalInPx =
                (20 * requireContext().resources.displayMetrics.density).toInt() // 좌우 마진 20dp
            val marginBottomInPx =
                (11 * requireContext().resources.displayMetrics.density).toInt() // 하단 마진 11dp

            params.width =
                requireContext().resources.displayMetrics.widthPixels - (marginHorizontalInPx * 2) // 좌우 마진 적용
            params.height = WindowManager.LayoutParams.WRAP_CONTENT // 세로는 콘텐츠 크기
            params.gravity = android.view.Gravity.BOTTOM // 하단에 배치
            params.y = marginBottomInPx // 하단 마진 적용

            window.attributes = params
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstEventBottomSheetBinding.inflate(inflater, container, false)

        // 배경을 투명하게 설정
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding.run {
            firstEventResultAdapter = result?.rankings?.let { FirstEventResultAdapter(it) }!!
            binding.recyclerViewFirstEventResult.apply {
                adapter = firstEventResultAdapter
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }

            buttonClose.setOnClickListener {
                dismiss()
            }

            buttonMyRanking.setOnClickListener {
                firstEventResultAdapter.updateList(result?.rankings!!, result?.myRanking)
            }
        }

        return binding.root
    }
}
