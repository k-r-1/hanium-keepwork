package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class WantedListFragment : Fragment() {
    lateinit var filter: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wanted_list, container, false)

        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(WantedRequestingFragment(), "즉시지원")
        adapter.addFragment(WantedWorkNetFragment(), "워크넷 채용공고")

        val viewPager = view.findViewById<ViewPager>(R.id.viewpager01)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tablayout01)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filter = view.findViewById<Button>(R.id.conditionButton)
        filter.setOnClickListener() {
            val fragment1 = WantedFilteringFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment1)
                .addToBackStack(null)
                .commit()
        }
    }

}