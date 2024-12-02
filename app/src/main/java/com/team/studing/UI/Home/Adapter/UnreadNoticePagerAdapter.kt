package com.team.studing.UI.Home.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.UnreadNotice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.databinding.RowNoticeDetailBinding

class UnreadNoticePagerAdapter(
    private var activity: MainActivity,
    private var notices: List<UnreadNotice>
) :
    RecyclerView.Adapter<UnreadNoticePagerAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newNotices: List<UnreadNotice>) {
        notices = newNotices
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
            }
        }

        holder.title.text = notices[position].title
        holder.content.text = notices[position].content
        holder.likeCount.text = notices[position].likeCount.toString()
        holder.saveCount.text = notices[position].saveCount.toString()
        holder.readCount.text = notices[position].readCount.toString()
        holder.date.text = notices[position].createdAt
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
    }
}