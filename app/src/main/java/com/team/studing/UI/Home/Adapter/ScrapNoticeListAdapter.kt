package com.team.studing.UI.Home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.API.response.Home.ScrapNotice
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.databinding.RowScrapNoticeListBinding

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
            RowScrapNoticeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = scrapNotices[position].title
        holder.date.text = scrapNotices[position].createdAt

        if (scrapNotices[position].image!!.isNotEmpty()) {
            Glide.with(activity).load(scrapNotices[position].image)
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.img_notice_list_default)
        }
    }

    override fun getItemCount() = scrapNotices.size

    inner class ViewHolder(val binding: RowScrapNoticeListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.textViewNoticeTitle
        val date = binding.textViewNoticeDate
        val image = binding.imageViewNotice

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