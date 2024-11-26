package com.team.studing.UI.Home.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.Notice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.splitString
import com.team.studing.databinding.RowHomeNoticeBinding

class HomeNoticePagerAdapter(
    private var activity: MainActivity,
    private var notices: List<Notice>
) :
    RecyclerView.Adapter<HomeNoticePagerAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newNotices: List<Notice>) {
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
            RowHomeNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.writerType.run {
            text = splitString(notices[position].writerInfo!!)?.first
            when (splitString(notices[position].writerInfo!!)?.second) {
                "총학생회" -> {
                    setBackgroundResource(R.drawable.background_student_council_chip_primary10)
                    setTextColor(resources.getColor(R.color.primary_50))
                }

                "단과대" -> {
                    setBackgroundResource(R.drawable.background_student_council_chip_red5)
                    setTextColor(resources.getColor(R.color.red))
                }

                else -> {
                    setBackgroundResource(R.drawable.background_student_council_chip_mint)
                    setTextColor(Color.parseColor("#71BDC3"))
                }
            }
        }

        holder.title.text = notices[position].title
        holder.content.text = notices[position].content
        holder.likeCount.text = notices[position].noticeLike.toString()
        holder.saveCount.text = notices[position].saveCount.toString()
        holder.readCount.text = notices[position].viewCount.toString()
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

        Glide.with(activity).load(notices[position].image)
            .into(holder.noticeImage)
    }

    override fun getItemCount() = if (notices.size >= 5) {
        5
    } else {
        notices.size
    }

    inner class ViewHolder(val binding: RowHomeNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.textViewNoticeTitle
        val content = binding.textViewNoticeContent
        val date = binding.textViewNoticeDate
        val writerType = binding.textViewStudentCouncilType
        val likeCount = binding.textViewLikeNum
        val saveCount = binding.textViewScrapNum
        val readCount = binding.textViewShowNum
        val saveCheck = binding.imageViewScrap
        val likeCheck = binding.imageViewLike
        val noticeImage = binding.imageViewNotice

        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)

                // 클릭 리스너 호출
                onItemClickListener?.invoke(position)

                true
            }
        }
    }
}