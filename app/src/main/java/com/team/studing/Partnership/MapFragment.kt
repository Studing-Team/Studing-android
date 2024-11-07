package com.team.studing.Partnership

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.naver.maps.map.NaverMapSdk
import com.team.studing.MainActivity
import com.team.studing.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    lateinit var binding: FragmentMapBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        initView()

        binding.run {
            NaverMapSdk.getInstance(mainActivity).client =
                NaverMapSdk.NaverCloudPlatformClient("${com.team.studing.BuildConfig.MAP_API_KEY}")
        }

        return binding.root
    }

    fun initView() {
        binding.run {
            buttonMapBack.bringToFront()
            layoutStore.bringToFront()
            layoutLocation.bringToFront()
        }
        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)
    }

}