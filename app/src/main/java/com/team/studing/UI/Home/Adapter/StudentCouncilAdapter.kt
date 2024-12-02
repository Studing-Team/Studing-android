package com.team.studing.UI.Home.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.databinding.RowStudentCouncilBinding

class StudentCouncilAdapter(
    private var activity: MainActivity,
    private var nameList: List<String>,
    private var logoList: List<String>,
    private var unreadList: List<String>
) :
    RecyclerView.Adapter<StudentCouncilAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(
        newNameList: List<String>,
        newLogoList: List<String>,
        newUnreadList: List<String>
    ) {
        nameList = newNameList
        logoList = newLogoList
        unreadList = newUnreadList
        notifyDataSetChanged()
    }

    fun updateSelectedPosition(position: Int) {
        val previousPosition = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousPosition) // 이전 선택 상태 갱신
        notifyItemChanged(selectedPosition) // 현재 선택 상태 갱신
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowStudentCouncilBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (nameList[position] != "noStudentCouncil") {
            holder.name.text = nameList[position]
            if (logoList[position] == "default") {
                holder.logo.setImageResource(R.drawable.ic_student_council_default)
            } else if (logoList[position] == "whole_default") {
                holder.logo.setImageResource(R.drawable.ic_student_council_whole)
            } else {
                Glide.with(activity).load(logoList[position]).into(holder.logo)
            }
        }

        // 안읽은 공지사항 체크
        if (unreadList.any { it == nameList[position] }) {
            holder.badge.visibility = View.VISIBLE
        } else {
            holder.badge.visibility = View.INVISIBLE
        }

        // 배경 설정
        if (position == selectedPosition) {
            holder.layoutStudentCouncil.setBackgroundResource(R.drawable.background_student_council_selected)
        } else {
            holder.layoutStudentCouncil.setBackgroundResource(R.drawable.background_student_council_unselected)
        }
    }

    override fun getItemCount() = nameList.size

    inner class ViewHolder(val binding: RowStudentCouncilBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val name = binding.textViewStudentCouncilName
        val logo = binding.imageViewStudentCouncil
        val layoutStudentCouncil = binding.layoutStudentCouncil
        val badge = binding.imageViewBadge

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