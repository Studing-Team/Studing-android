package com.team.studing.UI.Notice.Adapter

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.UnreadNotice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Notice.FirstEventState
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.RowNoticeDetailBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class UnreadNoticePagerAdapter(
    private var activity: MainActivity,
    private var notices: List<UnreadNotice>
) :
    RecyclerView.Adapter<UnreadNoticePagerAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    private val viewModel: NoticeViewModel by lazy {
        ViewModelProvider(activity)[NoticeViewModel::class.java]
    }

    var firstEventStatus = FirstEventState.BEFORE

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newNotices: List<UnreadNotice>, state: FirstEventState?) {
        notices = newNotices
        if (state != null) {
            firstEventStatus = state
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowNoticeDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tag.run {
            text = notices[position].tag!!
            when (notices[position].tag!!) {
                "공지" -> {
                    setBackgroundResource(R.drawable.background_notice_type_chip_primary20)
                    setTextColor(resources.getColor(R.color.primary_50))
                }

                "이벤트" -> {
                    setBackgroundResource(R.drawable.background_notice_type_chip_red)
                    setTextColor(resources.getColor(R.color.red))
                }

                "선착순" -> {
                    setBackgroundResource(R.drawable.background_notice_type_chip_red)
                    setTextColor(resources.getColor(R.color.red))
                }
            }
        }

        holder.title.text = notices[position].title
        holder.content.text = notices[position].content
        holder.likeCount.text = notices[position].likeCount.toString()
        holder.saveCount.text = notices[position].saveCount.toString()
        holder.readCount.text = notices[position].readCount.toString()
        holder.date.text = notices[position].createdAt

        if (notices[position].startTime != null && notices[position].endTime != null) {
            holder.layoutEventTime.visibility = View.VISIBLE
            holder.startTime.text =
                changDateToString(notices[position].startTime!!)
            holder.endTime.text = changDateToString(notices[position].endTime!!)
        } else {
            holder.layoutEventTime.visibility = View.GONE
        }

        if (notices[position].isFirstComeNotice == true) {
            // 선착순 이벤트인 경우
            holder.buttonJoinEvent.visibility = View.VISIBLE
            holder.timeTitle.text = "선착순 이벤트 안내"
            checkNoticeTime(
                notices[position].startTime!!,
                notices[position].endTime!!,
                position
            )
            holder.eventNum.text = "인원 ${notices[position].firstComeNumber}명"
        } else {
            holder.buttonJoinEvent.visibility = View.GONE
            holder.timeTitle.text = "공지사항 안내"
        }

        // 선착순 이벤트 버튼 UI 업데이트
        holder.buttonJoinEvent.run {
            text = firstEventStatus.text
            backgroundTintList =
                ContextCompat.getColorStateList(activity, firstEventStatus.colorId)
            isEnabled = firstEventStatus.isEnabled

            setOnClickListener {
                when (firstEventStatus) {
                    FirstEventState.ACTIVE -> {
                        // 선착순 이벤트 참여
                        viewModel.joinFirstEvent(activity, notices[position].id, "unread")
                        notices[position].isFirstComeApplied = true
                        firstEventStatus = FirstEventState.COMPLETE
                        updateList(notices, firstEventStatus)
                    }

                    FirstEventState.COMPLETE -> {
                        // 내 순위 확인
                        viewModel.getFirstEventResult(activity, notices[position].id)
                    }

                    else -> {}
                }
            }
        }

        if (notices[position].saveCheck) {
            holder.saveCheck.setImageResource(R.drawable.ic_scrap_selected)
        } else {
            holder.saveCheck.setImageResource(R.drawable.ic_scrap_black30)
        }
        if (notices[position].likeCheck) {
            holder.likeCheck.setImageResource(R.drawable.ic_like_red)
        } else {
            holder.likeCheck.setImageResource(R.drawable.ic_like_black30)
        }

        holder.studentCouncilName.text = notices[position].affilitionName
        Glide.with(activity).load(notices[position].logoImage)
            .into(holder.logoImage)

        if (notices[position].images.isNullOrEmpty()) {
            holder.noticeImage.visibility = View.GONE
            holder.dotsIndicator.visibility = View.GONE
        } else {
            holder.noticeImage.visibility = View.VISIBLE
            if (notices[position].images.size == 1) {
                holder.dotsIndicator.visibility = View.GONE
                holder.noticeImage.isUserInputEnabled = false // 슬라이드 동작 비활성화
            } else {
                holder.dotsIndicator.visibility = View.VISIBLE
                holder.noticeImage.isUserInputEnabled = true // 슬라이드 동작 활성화
            }
            Log.d("##", "viewpager : ${notices[position].images}")
            val imageAdapter = UnreadNoticeImageAdapter(activity, notices[position].images)
            holder.noticeImage.adapter = imageAdapter
            holder.dotsIndicator.attachTo(holder.noticeImage)
        }
    }

    override fun getItemCount() = notices.size

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkNoticeTime(startTime: String, endTime: String, position: Int) {
        // 날짜 및 시간 설정
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        // LocalDateTime으로 변환
        val startLocalTime = LocalDateTime.parse(startTime, formatter)
        val endLocalTime = LocalDateTime.parse(endTime, formatter)
        val currentTime = LocalDateTime.now()

        // 현재 시간이 시작-종료 범위 내에 있는지 확인
        val isWithinTimeRange =
            currentTime.isAfter(startLocalTime) && currentTime.isBefore(endLocalTime)

        Log.d("##", "$startLocalTime , $endLocalTime , $currentTime : $isWithinTimeRange")

        // 현재 시간이 범위 내에 있는지 확인하여 버튼 상태 결정
        firstEventStatus =
            if (notices[position].isFirstComeApplied == true) {
                FirstEventState.COMPLETE
            } else {
                if (currentTime.isAfter(startLocalTime) && currentTime.isBefore(endLocalTime)) {
                    FirstEventState.ACTIVE
                } else if (currentTime.isBefore(startLocalTime)) {
                    FirstEventState.BEFORE
                } else {
                    FirstEventState.FINISH
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun changDateToString(time: String): String? {
        // LocalDateTime으로 변환
        val localDateTime = LocalDateTime.parse(time)

        // 변환할 포맷 설정
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 HH시 mm분", Locale.KOREAN)

        // LocalDateTime을 문자열로 변환
        val formattedTime = localDateTime.format(formatter)

        return formattedTime
    }


    inner class ViewHolder(val binding: RowNoticeDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.textViewNoticeTitle
        val content = binding.textViewNoticeContent
        val date = binding.textViewDate
        val likeCount = binding.textViewLikeNum
        val saveCount = binding.textViewScrapNum
        val readCount = binding.textViewShowNum
        val saveCheck = binding.imageViewScrap
        val likeCheck = binding.imageViewLike
        val noticeImage = binding.viewPager
        val logoImage = binding.imageViewStudentCouncil
        val studentCouncilName = binding.textViewStudentCouncil
        val dotsIndicator = binding.dotsIndicatorNotice
        val tag = binding.chipNoticeType

        val eventNum = binding.textViewEventNum
        val timeTitle = binding.textViewEventTimeTitle
        val startTime = binding.textViewEventStartTimeValue
        val endTime = binding.textViewEventEndTimeValue
        val buttonJoinEvent = binding.buttonFirstEvent
        val layoutEventTime = binding.layoutEventTime
    }
}