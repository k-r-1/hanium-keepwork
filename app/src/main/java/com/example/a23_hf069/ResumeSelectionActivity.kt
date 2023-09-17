package com.example.a23_hf069

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ResumeSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_selection)

        // 기본 툴바 숨기기
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        val closeButton = findViewById<ImageButton>(R.id.backButton)
        closeButton.setOnClickListener {
            finish()
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // RecyclerView 어댑터 설정 (어댑터 클래스를 만들어야 함)
        val adapter = ResumeSelectionAdapter() // MyAdapter는 RecyclerView.Adapter를 상속한 사용자 정의 어댑터 클래스입니다.
        recyclerView.adapter = adapter

        // 3개의 항목 추가
        adapter.addItem("성실한 인재, 000입니다.")
        adapter.addItem("책임감 있는 인재, 000입니다.")
        adapter.addItem("긍정적인 마인드의 소유자, 000입니다.")
    }
}