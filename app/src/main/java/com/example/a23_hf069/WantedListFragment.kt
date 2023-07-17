package com.example.a23_hf069

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_wanted_work_net_search.*

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

        searchContent.setOnTouchListener { _, _ ->
            val wantedWorkNetSearchFragment = WantedWorkNetSearchFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedWorkNetSearchFragment)
                .addToBackStack(null)
                .commit()
            true
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}