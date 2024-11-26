package com.team.studing.UI.Home.Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.API.response.Home.ScrapNotice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MainUtil.splitString
import com.team.studing.databinding.RowScrapNoticeBinding

class ScrapNoticeListAdapter(
    private var activity: MainActivity,
    private var scrapNotices: List<ScrapNotice>
) :
    RecyclerView.Adapter<ScrapNoticeListAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newScrapNotices: List<ScrapNotice>) {
        scrapNotices = newScrapNotices
        notifyDataSetChanged()
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowScrapNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.writerType.run {
            text = splitString(scrapNotices[position].affiliation)?.first
            when (splitString(scrapNotices[position].affiliation)?.second) {
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

        holder.title.text = scrapNotices[position].title
        holder.content.text = scrapNotices[position].content
        holder.date.text = scrapNotices[position].createdAt
    }

    override fun getItemCount() = if (scrapNotices.size >= 5) {
        5
    } else {
        scrapNotices.size
    }

    inner class ViewHolder(val binding: RowScrapNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.textViewNoticeScrapTitle
        val content = binding.textViewNoticeScrapContent
        val date = binding.textViewNoticeScrapDate
        val writerType = binding.textViewStudentCouncilType

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