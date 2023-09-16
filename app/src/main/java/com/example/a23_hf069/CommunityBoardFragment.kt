package com.example.a23_hf069

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommunityBoardFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommunityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_community_board, container, false)

        val community = listOf(
            CommunityBoard("한결소래빌리지 사회복지사 모집 공고", "한결 소래빌리지", "인천광역시 남동구"),
            CommunityBoard("안성시지체장애인협회 직원 모집", "한국지체장애인협회안성시지회", "경기도 안성시")
        )

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = CommunityAdapter(community)
        recyclerView.adapter = adapter

        return view
    }

}