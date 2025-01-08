package com.team.studing.UI.Notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.team.studing.MainActivity
import com.team.studing.R
import com.team.studing.UI.Home.HomeFragment
import com.team.studing.Utils.GlobalApplication.Companion.amplitude
import com.team.studing.databinding.FragmentUnreadNoticeFinishBinding

class UnreadNoticeFinishFragment : Fragment() {

    lateinit var binding: FragmentUnreadNoticeFinishBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUnreadNoticeFinishBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        binding.run {
            buttonFinish.setOnClickListener {
                amplitude.track("click_complete_unread")

                mainActivity.supportFragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )

                val nextFragment = HomeFragment()

                val transaction = mainActivity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView_main, nextFragment)
                transaction.commit()
                true
            }
        }


        return binding.root
    }
}