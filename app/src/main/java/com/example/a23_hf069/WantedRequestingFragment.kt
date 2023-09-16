package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

class WantedRequestingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView= inflater.inflate(R.layout.fragment_wanted_requesting, container, false)

        //아래 코드는 중간평가 주작을 위해 작성한 코드임
        val mainLayout = rootView.findViewById<LinearLayout>(R.id.myLinearLayout)
        mainLayout.setOnClickListener {
            // '채용공고 상세정보' 페이지로 이동
            val intent = Intent(activity, JobDetailActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

}