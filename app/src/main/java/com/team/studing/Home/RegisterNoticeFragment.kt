package com.team.studing.Home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.team.studing.MainActivity
import com.team.studing.databinding.FragmentRegisterNoticeBinding

class RegisterNoticeFragment : Fragment() {

    lateinit var binding: FragmentRegisterNoticeBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterNoticeBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {
            buttonGallery.setOnClickListener {

            }

            editTextNoticeTitle.addTextChangedListener {
                checkEnabled()
            }

            editTextNoticeContent.addTextChangedListener {
                checkEnabled()
            }
        }

        return binding.root
    }

    fun checkEnabled() {
        binding.run {
            buttonRegister.isEnabled =
                editTextNoticeTitle.text.isNotEmpty() && editTextNoticeContent.text.isNotEmpty()
        }
    }

    fun initView() {

        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)

        binding.run {

            toolbar.run {
                buttonClose.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
                textViewTitle.text = "공지사항 작성"
            }
        }
    }
}