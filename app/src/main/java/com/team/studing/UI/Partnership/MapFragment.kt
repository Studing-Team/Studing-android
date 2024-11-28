package com.team.studing.UI.Partnership

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapSdk
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.Utils.MyApplication
import com.team.studing.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentMapBinding
    lateinit var mainActivity: MainActivity

    private lateinit var mapView: MapView
    private lateinit var naverMap: NaverMap


    private val CURRENT_LOCATION_CODE = 200
    private val LOCATION_PERMISSTION_REQUEST_CODE: Int = 1000
    private lateinit var locationSource: FusedLocationSource // 위치를 반환하는 구현체

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initView()

        binding.run {
            NaverMapSdk.getInstance(mainActivity).client =
                NaverMapSdk.NaverCloudPlatformClient("${com.team.studing.BuildConfig.MAP_API_KEY}")

            // 버튼 클릭 시 현재 위치로 이동
            buttonMyLocation.setOnClickListener {
                checkLocationPermission()
            }

            buttonMapBack.setOnClickListener {
                fragmentManager?.popBackStack()
            }
        }

        locationSource = FusedLocationSource(this, LOCATION_PERMISSTION_REQUEST_CODE)

        return binding.root
    }

    private fun moveToCurrentLocation() {
        val lastLocation = locationSource.lastLocation
        if (lastLocation != null) {
            // 위치가 유효할 때 카메라 이동
            val cameraUpdate = CameraUpdate.scrollAndZoomTo(
                LatLng(lastLocation.latitude, lastLocation.longitude),
                18.0 // 적절한 줌 레벨
            ).animate(CameraAnimation.Easing)
            naverMap.moveCamera(cameraUpdate)
        } else {
            // 위치 요청이 아직 활성화되지 않은 경우 강제 요청
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
            Log.d("MapFragment", "현재 위치 정보를 가져올 수 없어 추적 모드 활성화")
        }
    }


    private fun checkLocationPermission() {
        if (!locationSource.isActivated) {
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSTION_REQUEST_CODE
            )
        } else {
            moveToCurrentLocation() // 권한이 이미 부여된 경우
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSTION_REQUEST_CODE) {
            if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
                if (locationSource.isActivated) {
                    Log.d("MapFragment", "위치 권한 승인됨")
                    moveToCurrentLocation() // 권한 승인 후 위치 이동
                } else {
                    Log.e("MapFragment", "위치 권한 거부됨")
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    fun checkCategoryPosition(categoryName: String): Int {
        val index = partnerShipCategoryNameList.indexOf(categoryName)

        Log.d("##", "category : ${categoryName}")
        Log.d("##", "index: ${index}")

        return index
    }

    fun initView() {
        binding.run {
            buttonMapBack.bringToFront()
            layoutStore.bringToFront()
            layoutLocation.bringToFront()

            partnerShipCategoryNameList =
                resources.getStringArray(R.array.partnership_category_name).toMutableList()

            textViewLocation.text = MyApplication.selectedPartnerShip?.partnerAddress
            textViewStoreName.text = MyApplication.selectedPartnerShip?.partnerName
            textViewStoreCategory.text = MyApplication.selectedPartnerShip?.category
            textViewStoreDescription.text = MyApplication.selectedPartnerShip?.partnerDescription
            Glide.with(mainActivity).load(MyApplication.selectedPartnerShip?.partnerLogo)
                .into(imageViewStore)
            imageViewCategory.setImageResource(
                partnerShipImageList[checkCategoryPosition(
                    MyApplication.selectedPartnerShip?.category!!
                )]!!
            )
        }
        mainActivity.hideBottomNavigation(true)
        mainActivity.hideWriteNoticeButton(true)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(map: NaverMap) {
        Log.d("##", "onMapReady")
        naverMap = map

        // 위치 소스 연결
        naverMap.locationSource = locationSource

        // 확대 축소 범위 설정
        naverMap.maxZoom = 20.0
        naverMap.minZoom = 10.0

        // 마커 위치 설정
        val latitude = MyApplication.selectedPartnerShip?.latitude!!
        val longitude = MyApplication.selectedPartnerShip?.longitude!!
        val marker = Marker()

        marker.position = LatLng(latitude, longitude)
        marker.icon = OverlayImage.fromResource(R.drawable.ic_marker)
        marker.map = naverMap

        // 초기 위치 및 줌 레벨 설정
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(
            LatLng(latitude, longitude),
            18.0 // 초기 줌 레벨
        ).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)

        // 지도 옵션 설정
        naverMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BUILDING, true)
        naverMap.isIndoorEnabled = true

        // 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.None
    }
}