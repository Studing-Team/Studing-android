package com.team.studing.UI.Partnership.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.databinding.RowCategoryBinding

class PartnerShipCategoryAdapter(
    private var activity: MainActivity,
    private var categoryList: List<String>,
    private var categoryImageList: List<Int?>
) : RecyclerView.Adapter<PartnerShipCategoryAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(
        newNameList: List<String>
    ) {
        categoryList = newNameList
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // 배경 설정
        if (position == selectedPosition) {
            if (position == 0) {
                holder.categoryChip.setBackgroundResource(R.drawable.background_category_chip_whole)
            } else {
                holder.categoryChip.setBackgroundResource(R.drawable.background_category_chip_selected)
            }
        } else {
            holder.categoryChip.setBackgroundResource(R.drawable.background_category_chip_default)
        }

        holder.name.text = categoryList[position].toString()
        if (categoryImageList[position] != null) {
            holder.logo.visibility = View.VISIBLE
            holder.logo.setImageResource(categoryImageList[position]!!)
        } else {
            holder.logo.visibility = View.GONE
        }
    }

    override fun getItemCount() = categoryList.size

    inner class ViewHolder(val binding: RowCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.textViewCategory
        val logo = binding.imageViewCategory
        val categoryChip = binding.buttonCategory

        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(adapterPosition)

                val previousPosition = selectedPosition
                selectedPosition = position

                // 이전 선택된 항목과 현재 선택된 항목을 갱신
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                true
            }
        }
    }
}