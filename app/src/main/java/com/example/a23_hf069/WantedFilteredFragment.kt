package com.example.a23_hf069

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment

class WantedFilteredFragment : Fragment() {
    // WantedfilteredFragment
    lateinit var filter: Button
    private lateinit var jobListView: ListView
    private lateinit var jobList: List<Job>
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private var currentPage = 1

    // 화면 띄우기
    override fun onCreateView( // onCreateView 함수 오버라이드 해줌
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_wanted_work_net, container, false)

        // UI 요소 초기화
        jobListView = view.findViewById(R.id.jobListView)
        prevButton = view.findViewById(R.id.prevButton)
        nextButton = view.findViewById(R.id.nextButton)

        // 이전 페이지 버튼 클릭 이벤트 처리
        prevButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                //fetchJobData()
            }
        }

        // 다음 페이지 버튼 클릭 이벤트 처리
        nextButton.setOnClickListener {
            currentPage += 1
            //fetchJobData()
        }

        return view
    }
}