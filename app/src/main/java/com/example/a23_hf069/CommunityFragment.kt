package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class CommunityFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        // 레이아웃에서 버튼을 찾습니다.
        val button: Button = view.findViewById(R.id.button)

        // 버튼에 클릭 리스너를 추가합니다.
        button.setOnClickListener {
            // SaeilSearchActivity로 이동하는 코드를 작성합니다.
            //val intent = Intent(activity, SaeilSearchActivity::class.java)
            //startActivity(intent)
        }

        return view
    }
}