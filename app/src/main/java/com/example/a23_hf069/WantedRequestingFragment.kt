package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WantedRequestingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImmediateJobAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView= inflater.inflate(R.layout.fragment_wanted_requesting, container, false)

        //임시코드
//        val mainLayout = rootView.findViewById<LinearLayout>(R.id.myLinearLayout)
//        mainLayout.setOnClickListener {
//            // '채용공고 상세정보' 페이지로 이동
//            val intent = Intent(activity, JobDetailActivity::class.java)
//            startActivity(intent)
//        }

        val jobs = listOf(
            ImmediateJob("한결소래빌리지 사회복지사 모집 공고", "한결 소래빌리지", "인천광역시 남동구"),
            ImmediateJob("안성시지체장애인협회 직원 모집", "한국지체장애인협회안성시지회", "경기도 안성시")
        )

        recyclerView = rootView.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ImmediateJobAdapter(jobs)
        recyclerView.adapter = adapter

        return rootView
    }

}