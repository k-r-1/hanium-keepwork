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
            val wantedWorkNetFragment = WantedWorkNetFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedWorkNetFragment)
                .addToBackStack(null)
                .commit()
        }
    }

  /*  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filter = view.findViewById<Button>(R.id.conditionButton)
        filter.setOnClickListener() {
             //조건검색 버튼을 클릭하면 fragment_wanted_list로 전환
//            val filteringFragment = WantedFilteringFragment()
//            requireActivity().supportFragmentManager.beginTransaction() // 프래그먼트 간 전환 수행
//                .replace(R.id.fl_container, filteringFragment) // 프래그먼트 교체
//                .addToBackStack(null) // 이전 프래그먼트로 돌아가기
//                .commit()
            //test
            val intent = Intent(getActivity(), WantedTest::class.java)
            startActivity(intent)
        }
    }*/
}