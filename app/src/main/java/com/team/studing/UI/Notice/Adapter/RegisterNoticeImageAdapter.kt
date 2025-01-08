package com.team.studing.UI.Notice.Adapter

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.team.studing.databinding.RowRegisterNoticeImageBinding

class RegisterNoticeImageAdapter(
    var activity: Activity,
    private var noticeImages: MutableList<Uri>
) :
    RecyclerView.Adapter<RegisterNoticeImageAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newNoticeImages: MutableList<Uri>) {
        noticeImages = newNoticeImages
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < noticeImages.size) {
            noticeImages.removeAt(position)
            notifyItemRemoved(position) // 삭제된 아이템만 갱신
            notifyItemRangeChanged(position, noticeImages.size) // 나머지 아이템의 위치 갱신
        }
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowRegisterNoticeImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        Log.d("##", "notice image : ${noticeImages.size}")
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(activity)
            .load(noticeImages[position])
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(holder.image)
    }

    override fun getItemCount() = noticeImages.size

    inner class ViewHolder(val binding: RowRegisterNoticeImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val image = binding.imageViewNotice
        val buttonDelete = binding.buttonDelete

        init {
            binding.buttonDelete.setOnClickListener {
                // 아이템 삭제
                itemClickListener?.onItemClick(adapterPosition)

                // 클릭 리스너 호출
                onItemClickListener?.invoke(position)

                true
            }
        }
    }
}