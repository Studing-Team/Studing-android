package com.team.studing.UI.Partnership.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.studing.API.response.PartnerShip.Partner
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.databinding.RowPartnershipBinding

class PartnerShipListAdapter(
    private var activity: MainActivity,
    private var partners: List<Partner>,
    private var categoryNameList: List<String>,
    private var categoryImageList: List<Int?>
) :
    RecyclerView.Adapter<PartnerShipListAdapter.ViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null
    private var context: Context? = null
    private var selectedPosition: Int = 0

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun updateList(newNotices: List<Partner>) {
        partners = newNotices
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int) {}
    }

    var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            RowPartnershipBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.storeName.text = partners[position].partnerName
        holder.storeDescription.text = partners[position].partnerDescription
        holder.storeAddress.text = partners[position].partnerAddress
        holder.storeCategoryName.text = partners[position].category
        holder.storeCategoryImage.setImageResource(categoryImageList[checkCategoryPosition(partners[position].category)]!!)
        holder.partnershipPromotion.run {
            text = partners[position].partnerContent
            visibility = View.GONE
        }
        holder.buttonShowPromotion.visibility = View.VISIBLE
        holder.buttonShowMap.visibility = View.GONE
        holder.textViewPromotionIntro.visibility = View.GONE
        holder.layoutCardPartnerShip.setBackgroundResource(R.drawable.background_partnership_card)

        Glide.with(activity).load(partners[position].partnerLogo)
            .into(holder.storeImage)
    }

    override fun getItemCount() = partners.size

    inner class ViewHolder(val binding: RowPartnershipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val storeName = binding.textViewStoreName
        val storeDescription = binding.textViewStoreDescription
        val storeAddress = binding.textViewLocation
        val storeCategoryName = binding.textViewStoreCategory
        val storeCategoryImage = binding.imageViewCategory
        val partnershipPromotion = binding.textViewPromotion
        val storeImage = binding.imageViewStore
        val buttonShowPromotion = binding.buttonShowPromotion
        val buttonShowMap = binding.layoutMapButton
        val textViewPromotionIntro = binding.textViewPromotionIntro
        val layoutCardPartnerShip = binding.layoutCardPartnership

        init {
            binding.root.setOnClickListener {
                amplitude.track("click_view_content_affiliate")

                binding.run {
                    textViewPromotion.visibility = View.VISIBLE
                    textViewPromotionIntro.visibility = View.VISIBLE
                    layoutMapButton.visibility = View.VISIBLE
                    buttonShowPromotion.visibility = View.GONE
                    layoutCardPartnership.setBackgroundResource(R.drawable.background_partnership_selected)
                }
            }
            binding.buttonMap.setOnClickListener {
                amplitude.track("click_view_location_affiliate")

                itemClickListener?.onItemClick(adapterPosition)

                // 클릭 리스너 호출
                onItemClickListener?.invoke(position)
            }
            binding.buttonClose.setOnClickListener {
                binding.run {
                    textViewPromotion.visibility = View.GONE
                    textViewPromotionIntro.visibility = View.GONE
                    layoutMapButton.visibility = View.GONE
                    buttonShowPromotion.visibility = View.VISIBLE
                    layoutCardPartnerShip.setBackgroundResource(R.drawable.background_partnership_card)
                }
            }
        }
    }

    fun checkCategoryPosition(categoryName: String): Int {
        val index = categoryNameList.indexOf(categoryName)

        Log.d("##", "index: ${index}")

        return index
    }
}