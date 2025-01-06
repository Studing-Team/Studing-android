package com.team.studing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.team.studing.UI.Notification.NotificationNoticeDetailFragment
import com.team.studing.ViewModel.NoticeViewModel
import com.team.studing.databinding.ActivityNotificationNoticeBinding

class NotificationNoticeActivity : AppCompatActivity() {

    lateinit var binding: ActivityNotificationNoticeBinding
    lateinit var viewModel: NoticeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationNoticeBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[NoticeViewModel::class.java]

        Log.d("##", "notification activity")

        handleIntent(intent)

        setContentView(binding.root)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        // Intent 데이터 처리
        val type = intent.getStringExtra("type")
        val noticeId = intent.getStringExtra("noticeId")
        Log.d("##", "Type: $type, Notice ID: $noticeId")

        if (type != null) {
            Log.d("NotificationNoticeActivity", "Type: $type, Notice ID: $noticeId")
            // 알림 데이터에 따른 UI 로직 처리
            viewModel.getNoticeDetail(this@NotificationNoticeActivity, noticeId?.toIntOrNull()!!)

            val nextFragment = NotificationNoticeDetailFragment()

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView_notification, nextFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}