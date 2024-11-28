package com.team.studing.UI.Partnership

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.studing.API.response.PartnerShip.Partner
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Partnership.Adapter.PartnerShipCategoryAdapter
import com.team.studing.UI.Partnership.Adapter.PartnerShipListAdapter
import com.team.studing.Utils.MyApplication
import com.team.studing.ViewModel.PartnerShipViewModel
import com.team.studing.databinding.FragmentPartnershipBinding

class PartnershipFragment : Fragment() {

    private lateinit var binding: FragmentPartnershipBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var viewModel: PartnerShipViewModel

    private lateinit var partnerShipCategoryAdapter: PartnerShipCategoryAdapter
    private lateinit var partnerShipListAdapter: PartnerShipListAdapter

    private var getPartnerShipList: List<Partner> = emptyList()

    private var partnerShipCategoryNameList = mutableListOf<String>()
    private val partnerShipImageList = listOf(
        null,
        R.drawable.ic_category_restaurant,
        R.drawable.ic_category_coffee,
        R.drawable.ic_category_drink,
        R.drawable.ic_category_exercise,
        R.drawable.ic_category_hospital,
        R.drawable.ic_category_culture
    )

    private var categoryPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPartnershipBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        viewModel = ViewModelProvider(this)[PartnerShipViewModel::class.java]

        partnerShipCategoryNameList =
            resources.getStringArray(R.array.partnership_category_name).toMutableList()

        binding.run {

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initView()
        initAdapters()
        observeViewModel()
    }

    private fun initAdapters() {
        // 카테고리 어댑터 초기화
        partnerShipCategoryAdapter = PartnerShipCategoryAdapter(
            mainActivity,
            partnerShipCategoryNameList,
            partnerShipImageList
        ).apply {
            itemClickListener = object : PartnerShipCategoryAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    categoryPosition = position
                    viewModel.getPartnerShipInfo(
                        mainActivity,
                        partnerShipCategoryNameList[categoryPosition]
                    )
                }
            }
        }

        // 파트너십 리스트 어댑터 초기화
        partnerShipListAdapter = PartnerShipListAdapter(
            mainActivity,
            getPartnerShipList, // 초기값으로 빈 리스트 전달
            partnerShipCategoryNameList,
            partnerShipImageList
        ).apply {
            itemClickListener = object : PartnerShipListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    MyApplication.selectedPartnerShip = getPartnerShipList[position]
                    Log.d("##", "${MyApplication.selectedPartnerShip}")
                    mainActivity.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView_main, MapFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        // RecyclerView에 어댑터 연결
        binding.recyclerViewCategory.apply {
            adapter = partnerShipCategoryAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        }

        binding.recyclerViewStore.apply {
            adapter = partnerShipListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun observeViewModel() {
        viewModel.run {
            partners.observe(viewLifecycleOwner) {
                getPartnerShipList = it
                partnerShipListAdapter.updateList(getPartnerShipList) // 데이터 업데이트
            }
        }
    }

    private fun initView() {
        viewModel.getPartnerShipInfo(mainActivity, "전체")

        mainActivity.hideBottomNavigation(false)
        mainActivity.hideWriteNoticeButton(true)
    }
}
