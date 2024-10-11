package com.team.studing.SignUp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.databinding.FragmentSignUpStep6Binding

class SignUpStep6Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep6Binding
    lateinit var loginActivity: LoginActivity

    var isImageUpload = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignUpStep6Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity

        initView()

        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the photo picker.
            if (uri != null) {
                isImageUpload = true
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.run {
                    imageViewStudentCard.setImageURI(uri)
                    layoutImageUpload.visibility = View.INVISIBLE
                }
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.run {
            buttonImageUpload.setOnClickListener {
                // 이미지 업로드 (갤러리)
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                checkComplete()
            }

            imageViewStudentCard.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                checkComplete()
            }

            editTextStudentName.addTextChangedListener {
                checkComplete()
                if(editTextStudentName.text.isNotEmpty()) {
                    editTextStudentName.setTextAppearance(R.style.BodyAdd)
                } else {
                    editTextStudentName.setTextAppearance(R.style.Body2)
                }
            }

            editTextWholeStudentNumber.addTextChangedListener {
                checkComplete()
                if(editTextWholeStudentNumber.text.isNotEmpty()) {
                    editTextWholeStudentNumber.setTextAppearance(R.style.BodyAdd)
                } else {
                    editTextWholeStudentNumber.setTextAppearance(R.style.Body2)
                }
            }

            buttonNext.setOnClickListener {
                val nextFragment = SignUpWaitingFragment()

                val transaction = loginActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_login, nextFragment)
                transaction.commit()
                true
            }

        }

        return binding.root
    }

    fun checkComplete() {
        binding.run {
            if(isImageUpload && editTextStudentName.text.isNotEmpty() && editTextWholeStudentNumber.text.isNotEmpty()) {
                buttonNext.isEnabled = true
            } else {
                buttonNext.isEnabled = false
            }
        }
    }

    fun initView() {
        loginActivity.setStatusBarTransparent()

        binding.run {
            toolbar.run {

                textViewTitle.text = "회원가입"
                buttonBack.setOnClickListener {
                    fragmentManager?.popBackStack()
                }
            }
        }
    }
}