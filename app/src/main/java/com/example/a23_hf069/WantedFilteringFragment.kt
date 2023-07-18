package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult


class WantedFilteringFragment : Fragment()  {
    lateinit var region_btn: Button
    // 화면 띄우기
    override fun onCreateView( // onCreateView 함수 오버라이드 해줌
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View?
    {
        return inflater.inflate(R.layout.fragment_wanted_filtering, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        region_btn = view.findViewById<Button>(R.id.region_btn)
        region_btn.setOnClickListener {
            val regionFragment = RegionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, regionFragment)
                .addToBackStack(null)
                .commit()
        }
    }



}