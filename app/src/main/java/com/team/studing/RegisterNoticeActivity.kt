package com.team.studing

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.UI.Notice.Adapter.RegisterNoticeImageAdapter
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.ActivityRegisterNoticeBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class RegisterNoticeActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterNoticeBinding
    lateinit var viewModel: NoticeViewModel

    private var selectedImages = mutableListOf<Uri>() // Set으로 중복 방지
    private lateinit var noticeImageAdapter: RegisterNoticeImageAdapter

    var noticeTag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterNoticeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]

        initView()
        initRecyclerView()

        // 다중 선택
        // PickMultipleVisualMedia 클래스의 생성자로 maxItems(사진 개수 제한)을 넘겨줄 수 있음.
        val pickMultipleMedia =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) { uris ->
                Log.d("##", "selected image : ${uris}")
                // 콜백
                if (uris.isNotEmpty()) {
                    // uri 리스트에 값이 있을 경우
                    val compressedImages = processAndCompressImages(uris)

                    selectedImages = uris.toMutableList()
                    Log.d("##", "selected image : ${selectedImages}")
                    binding.textViewImageNum.text = "${selectedImages.size}/10"
                    Log.d("##", "selected image : ${selectedImages.size}")
                    noticeImageAdapter.updateList(selectedImages)
                    // 압축된 이미지 리스트를 사용
                    MyApplication.noticeImages = compressedImages
                    Log.d("##", "selected image : ${MyApplication.noticeImages}")

                } else {
                    // uri 리스트에 값이 없을 경우
                    Log.d("##", "No media selected")
                }
            }

        binding.run {
            buttonGallery.setOnClickListener {
                amplitude.track("click_add_photo")

                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            editTextNoticeTitle.addTextChangedListener {
                checkEnabled()
            }

            editTextNoticeContent.addTextChangedListener {
                checkEnabled()
            }

            buttonTagNotice.setOnClickListener {
                noticeTag = "공지"
                checkTag()
            }

            buttonTagEvent.setOnClickListener {
                noticeTag = "이벤트"
                checkTag()
            }

            buttonRegister.setOnClickListener {
                amplitude.track("click_upload_notice")

                viewModel.registerNotice(
                    this@RegisterNoticeActivity,
                    editTextNoticeTitle.text.toString(),
                    editTextNoticeContent.text.toString(),
                    noticeTag
                )
            }
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        MyApplication.noticeImages = null
    }

    private fun initRecyclerView() {
        noticeImageAdapter =
            RegisterNoticeImageAdapter(this, selectedImages.toMutableList()).apply {
                setOnItemClickListener { position ->
                    val removedImage = selectedImages.elementAt(position)
                    selectedImages.remove(removedImage)
                    updateList(selectedImages.toMutableList())
                    binding.textViewImageNum.text = "${selectedImages.size}/10"
                }
            }

        binding.recyclerViewImage.apply {
            adapter = noticeImageAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }
    }

    fun checkTag() {
        binding.run {
            if (noticeTag == "공지") {
                buttonTagNotice.run {
                    setBackgroundResource(R.drawable.background_notice_type_chip_primary20)
                    setTextColor(resources.getColor(R.color.primary_50))
                }
                buttonTagEvent.run {
                    setBackgroundResource(R.drawable.background_chip_black10)
                    setTextColor(resources.getColor(R.color.black_30))
                }
            } else if (noticeTag == "이벤트") {
                buttonTagNotice.run {
                    setBackgroundResource(R.drawable.background_chip_black10)
                    setTextColor(resources.getColor(R.color.black_30))
                }
                buttonTagEvent.run {
                    setBackgroundResource(R.drawable.background_notice_type_chip_red)
                    setTextColor(resources.getColor(R.color.red))
                }
            }
        }
        checkEnabled()
    }

    private fun checkEnabled() {
        binding.run {
            buttonRegister.isEnabled =
                editTextNoticeTitle.text.isNotEmpty() && editTextNoticeContent.text.isNotEmpty() && noticeTag != ""
        }
    }

    private fun processSelectedImages(uris: List<Uri>): List<MultipartBody.Part> {
        val imageParts = mutableListOf<MultipartBody.Part>()
        uris.forEach { uri ->
            val resizedUri = convertResizeImage(uri)
            val compressedFile = File(resizedUri?.path ?: return@forEach)

            if (compressedFile.exists() && compressedFile.length() > 0) {
                val requestFile: RequestBody =
                    compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData(
                    "images",
                    compressedFile.name,
                    requestFile
                )
                imageParts.add(imagePart)
            } else {
                Log.e("##", "압축된 파일이 존재하지 않거나 비어 있습니다.")
            }
        }
        return imageParts
    }

    private fun convertResizeImage(imageUri: Uri): Uri? {
        return try {
            val contentResolver = this.contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            val resizedBitmap =
                Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

            val tempFile = File.createTempFile("resized_image", ".jpg", this.cacheDir)

            val byteArrayOutputStream = ByteArrayOutputStream()
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

            FileOutputStream(tempFile).use { outputStream ->
                outputStream.write(byteArrayOutputStream.toByteArray())
                outputStream.flush()
            }
            resizedBitmap.recycle()

            Uri.fromFile(tempFile)
        } catch (e: Exception) {
            Log.e("##", "Failed to resize image: ${e.message}")
            null
        }
    }

    private fun processAndCompressImages(uris: List<Uri>): List<MultipartBody.Part> {
        val compressedImages = mutableListOf<MultipartBody.Part>()

        uris.forEach { uri ->
            // 이미지 처리 및 압축
            val resizedUri = convertResizeImage(uri) // 리사이즈 후 Uri 생성
            val compressedFile = File(resizedUri?.path ?: return@forEach) // 경로가 없으면 해당 Uri 스킵

            if (compressedFile.exists() && compressedFile.length() > 0) {
                val requestFile: RequestBody =
                    compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())

                val imagePart = MultipartBody.Part.createFormData(
                    "noticeImages",  // 서버에서 기대하는 필드 이름
                    compressedFile.name, // 파일 이름
                    requestFile
                )

                compressedImages.add(imagePart)
            } else {
                Log.e("ImageProcessing", "압축된 파일이 존재하지 않거나 비어 있습니다.")
            }
        }

        return compressedImages
    }

    fun hideKeyboard() {
        val currentFocusView = currentFocus
        if (currentFocusView != null) {
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocusView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun initView() {
        binding.run {
            scrollView.setOnTouchListener { v, event ->
                hideKeyboard()
                false
            }

            toolbar.run {
                buttonClose.setOnClickListener {
                    finish()
                }
                textViewTitle.text = "공지사항 작성"
            }
        }
    }
}