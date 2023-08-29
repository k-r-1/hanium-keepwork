package com.example.a23_hf069

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class JobManagementFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_job_management, container, false)

        val tabLayout: TabLayout = rootView.findViewById(R.id.tabLayout_jobManagement) // TabLayout ID
        val viewPager: ViewPager2 = rootView.findViewById(R.id.viewPager_jobManagement) // ViewPager2 ID

        val fragmentList = listOf(
            JobManagementEndFragment(),
            JobManagementSaveFragment(),
            JobManagementEndFragment()
        )

        val adapter = TabPagerAdapter(fragmentList, requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "게재"
                1 -> tab.text = "임시저장"
                2 -> tab.text = "마감"
                else -> tab.text = ""
            }
        }.attach()

        return rootView
    }
}
