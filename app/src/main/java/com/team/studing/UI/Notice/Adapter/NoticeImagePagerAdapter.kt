package com.team.studing.UI.Notice.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.databinding.RowNoticeImageBinding

class NoticeImagePagerAdapter(
    private var activity: Activity,
    private var images: List<String>?
) :
    RecyclerView.Adapter<NoticeImagePagerAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newImages: List<String>?) {
        images = newImages
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowNoticeImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(activity).load(images?.get(position))
            .into(holder.noticeImage)
    }

    override fun getItemCount() = images?.size!!

    inner class ViewHolder(val binding: RowNoticeImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val noticeImage = binding.imageViewNoticeDetail
    }
}