package com.team.studing.UI.Register

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.INPUT_METHOD_SERVICE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.R
import com.team.studing.RegisterNoticeActivity
import com.team.studing.UI.Notice.Adapter.RegisterNoticeImageAdapter
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.FragmentRegisterNoticeBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RegisterNoticeFragment : Fragment() {

    lateinit var binding: FragmentRegisterNoticeBinding
    lateinit var registerNoticeActivity: RegisterNoticeActivity
    lateinit var viewModel: NoticeViewModel

    private var selectedImages = mutableListOf<Uri>() // Set으로 중복 방지
    private lateinit var noticeImageAdapter: RegisterNoticeImageAdapter

    var noticeTag = ""
    var isWriteTime = false

    var isEdit = false
    var noticeId = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRegisterNoticeBinding.inflate(layoutInflater)
        registerNoticeActivity = activity as RegisterNoticeActivity
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

                    checkEnabled()

                } else {
                    // uri 리스트에 값이 없을 경우
                    Log.d("##", "No media selected")
                }
            }

        binding.run {
            selectDateTime()

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

            editTextStartDate.addTextChangedListener {
                checkEnabled()
            }

            editTextStartTime.addTextChangedListener {
                checkEnabled()
            }

            editTextEndDate.addTextChangedListener {
                checkEnabled()
            }

            editTextEndTime.addTextChangedListener {
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

            buttonCheckTime.setOnClickListener {
                checkTime()
                checkEnabled()
            }

            buttonRegister.setOnClickListener {
                if (isEdit) {
                    // 공지사항 수정
                    if (isWriteTime) {
                        viewModel.editNotice(
                            registerNoticeActivity,
                            noticeId,
                            editTextNoticeTitle.text.toString(),
                            editTextNoticeContent.text.toString(),
                            noticeTag,
                            changeTimeFormat(
                                editTextStartDate.text.toString(),
                                editTextStartTime.text.toString()
                            ).toString(),
                            changeTimeFormat(
                                editTextEndDate.text.toString(),
                                editTextEndTime.text.toString()
                            ).toString(),
                            null
                        )
                    } else {
                        viewModel.editNotice(
                            registerNoticeActivity,
                            noticeId,
                            editTextNoticeTitle.text.toString(),
                            editTextNoticeContent.text.toString(),
                            noticeTag,
                            null,
                            null,
                            null
                        )
                    }
                } else {
                    // 공지사항 등록
                    amplitude.track("click_upload_notice")

                    if (isWriteTime) {
                        viewModel.registerNotice(
                            registerNoticeActivity,
                            editTextNoticeTitle.text.toString(),
                            editTextNoticeContent.text.toString(),
                            noticeTag,
                            isWriteTime,
                            changeTimeFormat(
                                editTextStartDate.text.toString(),
                                editTextStartTime.text.toString()
                            ).toString(),
                            changeTimeFormat(
                                editTextEndDate.text.toString(),
                                editTextEndTime.text.toString()
                            ).toString(),
                            firstEventNum = null
                        )
                    } else {
                        viewModel.registerNotice(
                            registerNoticeActivity,
                            editTextNoticeTitle.text.toString(),
                            editTextNoticeContent.text.toString(),
                            noticeTag,
                            isWriteTime,
                            null,
                            null,
                            firstEventNum = null
                        )
                    }
                }
            }
        }

        return binding.root
    }


    private fun initRecyclerView() {
        noticeImageAdapter =
            RegisterNoticeImageAdapter(
                registerNoticeActivity,
                selectedImages.toMutableList()
            ).apply {
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun changeTimeFormat(date: String, time: String): LocalDateTime? {
        // 한국어 날짜 포맷 정의
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.KOREAN)

        // 날짜 문자열을 LocalDate로 변환
        val date = LocalDate.parse(date, dateFormatter)

        // 시간 문자열을 LocalTime으로 변환
        val time = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME)

        // LocalDate와 LocalTime을 합쳐서 LocalDateTime 생성
        val dateTime = LocalDateTime.of(date, time)

        println("LocalDateTime: $dateTime")

        return dateTime
    }

    fun checkTime() {
        binding.run {
            isWriteTime = !isWriteTime
            if (isWriteTime) {
                layoutCheckTime.visibility = View.VISIBLE
                imageViewCheckboxTime.setImageResource(R.drawable.ic_checkbox_checked)
            } else {
                layoutCheckTime.visibility = View.GONE
                imageViewCheckboxTime.setImageResource(R.drawable.ic_checkbox_unchecked)
            }
        }
    }

    fun selectDateTime() {
        binding.run {
            editTextStartDate.setOnClickListener {
                val dateBottomsheet = DateBottomSheetFragment()

                dateBottomsheet.setDateBottomSheetInterface(object : DateBottomSheetInterface {
                    override fun onDateClickCompleteButton(date: String) {
                        editTextStartDate.setText(date)
                    }
                })

                dateBottomsheet.show(
                    registerNoticeActivity.supportFragmentManager,
                    "DateBottomsheet"
                )
            }

            editTextStartTime.setOnClickListener {
                val timeBottomsheet = TimeBottomSheetFragment()

                timeBottomsheet.setTimeBottomSheetInterface(object : TimeBottomSheetInterface {
                    override fun onTimeClickCompleteButton(time: String) {
                        editTextStartTime.setText(time)
                    }
                })

                timeBottomsheet.show(
                    registerNoticeActivity.supportFragmentManager,
                    "TimeBottomsheet"
                )
            }

            editTextEndDate.setOnClickListener {
                val dateBottomsheet = DateBottomSheetFragment()

                dateBottomsheet.setDateBottomSheetInterface(object : DateBottomSheetInterface {
                    override fun onDateClickCompleteButton(date: String) {
                        editTextEndDate.setText(date)
                    }
                })

                dateBottomsheet.show(
                    registerNoticeActivity.supportFragmentManager,
                    "DateBottomsheet"
                )
            }

            editTextEndTime.setOnClickListener {
                val timeBottomsheet = TimeBottomSheetFragment()

                timeBottomsheet.setTimeBottomSheetInterface(object : TimeBottomSheetInterface {
                    override fun onTimeClickCompleteButton(time: String) {
                        editTextEndTime.setText(time)
                    }
                })

                timeBottomsheet.show(
                    registerNoticeActivity.supportFragmentManager,
                    "TimeBottomsheet"
                )
            }
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
            var timeSelected =
                editTextStartDate.text.isNotEmpty() && editTextStartTime.text.isNotEmpty() && editTextEndDate.text.isNotEmpty() && editTextEndTime.text.isNotEmpty()
            if (isWriteTime) {
                buttonRegister.isEnabled =
                    editTextNoticeTitle.text.isNotEmpty() && editTextNoticeContent.text.isNotEmpty() && noticeTag != "" && timeSelected
            } else {
                buttonRegister.isEnabled =
                    editTextNoticeTitle.text.isNotEmpty() && editTextNoticeContent.text.isNotEmpty() && noticeTag != ""
            }
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
            val contentResolver = registerNoticeActivity.contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)

            val resizedBitmap =
                Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

            val tempFile =
                File.createTempFile("resized_image", ".jpg", registerNoticeActivity.cacheDir)

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
        val currentFocusView = registerNoticeActivity.currentFocus
        if (currentFocusView != null) {
            val inputManager =
                registerNoticeActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                currentFocusView.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        binding.run {
            scrollView.setOnTouchListener { v, event ->
                hideKeyboard()
                false
            }

            isEdit = arguments?.getBoolean("isEdit", false) == true

            if (isEdit == true) {
                // 전달받은 데이터 읽기
                arguments?.let { bundle ->
                    noticeId = bundle.getInt("id", 0)
                    val title = bundle.getString("title", "")
                    val content = bundle.getString("content", "")
                    val images = bundle.getStringArrayList("image")
                    var startDateTime = bundle.getString("startTime", null)
                    var endDateTime = bundle.getString("endTime", null)
                    noticeTag = bundle.getString("tag", "공지")

                    val uri = mutableListOf<Uri>()
                    if (images?.size != 0) {
                        for (i in 0 until images?.size!!) {
                            uri.add(Uri.parse(images[i]))
                            Log.d("##", "images : ${Uri.parse(images[i])}")
                        }
                        selectedImages = uri
                        Log.d("##", "images : ${selectedImages}")
                    }
                    MyApplication.noticeImages = processAndCompressImages(selectedImages)
                    binding.textViewImageNum.text = "${selectedImages.size}/10"
                    initRecyclerView()

                    editTextNoticeTitle.setText(title)
                    editTextNoticeContent.setText(content)
                    if (startDateTime != null && endDateTime != null) {
                        isWriteTime = true
                        var (startDate, startTime) = splitDateTimeString(startDateTime)
                        var (endDate, endTime) = splitDateTimeString(endDateTime)

                        editTextStartDate.setText(startDate)
                        editTextStartTime.setText(startTime)
                        editTextEndDate.setText(endDate)
                        editTextEndTime.setText(endTime)

                        layoutCheckTime.visibility = View.VISIBLE
                        imageViewCheckboxTime.setImageResource(R.drawable.ic_checkbox_checked)
                    } else {
                        layoutCheckTime.visibility = View.GONE
                    }

                    checkTag()

                    buttonRegister.isEnabled = false

                    toolbar.textViewTitle.text = "공지사항 수정"
                    buttonRegister.text = "수정하기"
                }
            } else {
                toolbar.textViewTitle.text = "공지사항 작성"
                layoutCheckTime.visibility = View.GONE
            }

            toolbar.run {
                buttonClose.setOnClickListener {
                    registerNoticeActivity.finish()
                }
            }
        }
    }

    // 문자열을 받아 날짜와 시간을 분리하는 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun splitDateTimeString(dateTimeStr: String): Pair<String, String> {
        // 문자열을 LocalDateTime으로 파싱
        val dateTime = LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

        // 날짜 포맷 (2025년 2월 5일)
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN)
        val dateString = dateTime.toLocalDate().format(dateFormatter)

        // 시간 포맷 (13:00)
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.KOREAN)
        val timeString = dateTime.toLocalTime().format(timeFormatter)

        return Pair(dateString, timeString)
    }
}