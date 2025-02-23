package com.team.studing.UI.Notice.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.API.response.Home.RankingItem
import com.team.studing.R
import com.team.studing.databinding.RowFirstEventResultBinding

class FirstEventResultAdapter(
    private var rankings: List<RankingItem>
) :
    RecyclerView.Adapter<FirstEventResultAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = -1

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newRankings: List<RankingItem>, position: Int?) {
        rankings = newRankings
        if (position == null) {
            selectedPosition = -1
        } else {
            selectedPosition = position - 1
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
            RowFirstEventResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ranking.text = rankings[position].orderNumber.toString()
        holder.time.text = rankings[position].applyDateTime
        holder.studentNum.text = rankings[position].maskedStudentNumber


        if (selectedPosition != -1 && position == selectedPosition) {
            holder.layoutMyRanking.setBackgroundResource(R.color.red_5)
            holder.ranking.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.red
                )
            )
            holder.time.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.red))
            holder.studentNum.setTextColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.red
                )
            )
        }
    }

    override fun getItemCount() = rankings.size


    inner class ViewHolder(val binding: RowFirstEventResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ranking = binding.textViewFirstEventNum
        val time = binding.textViewFirstEventTime
        val studentNum = binding.textViewFirstEventStudentNum
        val layoutMyRanking = binding.root
    }
}