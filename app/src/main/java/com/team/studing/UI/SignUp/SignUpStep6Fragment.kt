package com.team.studing.UI.SignUp

import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.team.studing.LoginActivity
import com.team.studing.R
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MainUtil.setStatusBarTransparent
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.SignUpViewModel
import com.team.studing.databinding.FragmentSignUpStep6Binding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class SignUpStep6Fragment : Fragment() {

    lateinit var binding: FragmentSignUpStep6Binding
    lateinit var loginActivity: LoginActivity
    lateinit var viewModel: SignUpViewModel

    var isImageUpload = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSignUpStep6Binding.inflate(layoutInflater)
        loginActivity = activity as LoginActivity
        viewModel = ViewModelProvider(loginActivity)[SignUpViewModel::class.java]

        initView()

        val contentResolver: ContentResolver = requireContext().contentResolver

        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                // Callback is invoked after the user selects a media item or closes the photo picker.
                if (uri != null) {
                    isImageUpload = true
                    Log.d("PhotoPicker", "Selected URI: $uri")

                    // 이미지 처리 및 압축
                    val resizedUri = convertResizeImage(uri) // 기존 이미지를 리사이징한 후 Uri 얻기
                    val compressedFile = File(resizedUri.path!!)

                    // 파일이 정상적으로 생성되었는지 확인
                    if (compressedFile.exists() && compressedFile.length() > 0) {
                        // RequestBody로 이미지 파일 생성
                        val requestFile: RequestBody =
                            compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                        // MultipartBody.Part로 이미지 파일 준비
                        val studentCardImage = MultipartBody.Part.createFormData(
                            "studentCardImage",  // 서버에서 기대하는 필드 이름
                            compressedFile.name, // 파일 이름
                            requestFile
                        )

                        // 이미지 파일을 MyApplication 객체에 저장
                        MyApplication.signUpImage = studentCardImage
                    } else {
                        Log.e("ImageCompression", "압축된 파일이 존재하지 않거나 비어 있습니다.")
                    }

                    binding.run {
                        imageViewStudentCard.setImageURI(uri)
                        layoutImageUpload.visibility = View.INVISIBLE
                    }
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        ViewCompat.setOnApplyWindowInsetsListener(requireActivity().window.decorView.rootView) { _, insets ->
            val sysBarInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            updateViewPositionForKeyboard(imeHeight - sysBarInsets.bottom)
            insets
        }

        binding.run {

            scrollView.setOnTouchListener { v, event ->
                loginActivity.hideKeyboard()
                false
            }

            buttonImageUpload.setOnClickListener {
                // 이미지 업로드 (갤러리)
                amplitude.track("click_next_signup_step6_upload")
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                checkComplete()
            }

            imageViewStudentCard.setOnClickListener {
                amplitude.track("click_next_signup_step6_upload")
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                checkComplete()
            }

            editTextStudentName.addTextChangedListener {
                if (editTextStudentName.text.isNotEmpty()) {
                    editTextStudentName.run {
                        setTextAppearance(R.style.BodyAdd)
                        setTextColor(resources.getColor(R.color.black_50))
                    }
                } else {
                    editTextStudentName.run {
                        setTextAppearance(R.style.Body2)
                        setTextColor(resources.getColor(R.color.black_20))
                    }
                }
                checkComplete()
            }

            editTextWholeStudentNumber.addTextChangedListener {
                if (editTextWholeStudentNumber.text.isNotEmpty()) {
                    editTextWholeStudentNumber.run {
                        setTextAppearance(R.style.BodyAdd)
                        setTextColor(resources.getColor(R.color.black_50))
                    }
                } else {
                    editTextWholeStudentNumber.run {
                        setTextAppearance(R.style.Body2)
                        setTextColor(resources.getColor(R.color.black_20))
                    }
                }
                checkComplete()
            }

            buttonNext.setOnClickListener {
                MyApplication.signUpName = editTextStudentName.text.toString()
                MyApplication.signUpStudentIDNumber = editTextWholeStudentNumber.text.toString()

                viewModel.signUp(loginActivity)
            }

        }

        return binding.root
    }

    private fun updateViewPositionForKeyboard(keyboardHeight: Int) {
        val layoutParams =
            binding.scrollView.layoutParams as ConstraintLayout.LayoutParams
        if (keyboardHeight > 0) {
            layoutParams.bottomMargin = keyboardHeight
        } else {
            layoutParams.bottomMargin = 0
        }
        binding.scrollView.layoutParams = layoutParams
    }

    fun checkComplete() {
        binding.run {
            buttonNext.isEnabled =
                isImageUpload && editTextStudentName.text.isNotEmpty() && editTextWholeStudentNumber.text.isNotEmpty()
        }
    }

    private fun convertResizeImage(imageUri: Uri): Uri {
        val contentResolver = requireContext().contentResolver
        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

        // 이미지 리사이즈 (절반 크기로)
        val resizedBitmap =
            Bitmap.createScaledBitmap(bitmap, bitmap.width / 2, bitmap.height / 2, true)

        // 임시 파일 생성
        val tempFile = File.createTempFile("resized_image", ".jpg", requireContext().cacheDir)

        // 이미지 파일 쓰기
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)

            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(byteArrayOutputStream.toByteArray())
                outputStream.flush()
            }

        } catch (e: Exception) {
            Log.e("ImageResize", "Failed to write file: ${e.message}")
        } finally {
            // 메모리 해제
            resizedBitmap.recycle()
        }

        return Uri.fromFile(tempFile)
    }


    private fun getRealPathFromURI(uri: Uri, contentResolver: ContentResolver): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)
        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val filePath = cursor.getString(columnIndex)
            cursor.close()
            return filePath
        }
        return uri.path ?: ""
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