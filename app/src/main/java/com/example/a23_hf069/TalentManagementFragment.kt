package com.example.a23_hf069

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TalentManagementFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_talent_management, container, false)
        val closeButton = rootView.findViewById<ImageButton>(R.id.backBtn)
        closeButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // ViewPager2 어댑터 연결
        viewPager = rootView.findViewById(R.id.ViewPager2)
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연동
        tabLayout = rootView.findViewById<TabLayout>(R.id.tablayout01)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "전체"
                1 -> "검토중"
                2 -> "합격"
                3 -> "불합격"
                else -> null
            }
        }.attach()



        return rootView
    }

}