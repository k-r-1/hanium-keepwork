package com.example.a23_hf069

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

class MypageFragment : Fragment() {
    lateinit var member_info : LinearLayout
    lateinit var notificationButton : Button
    lateinit var faqButton : Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mypage, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        member_info = view.findViewById<LinearLayout>(R.id.member_info_edit_btn)
        member_info.setOnClickListener(){// 회원정보 수정 프래그먼트로 전환
            val fragment_info = MemberInfoEditFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment_info) // 프래그먼트 교체
                .addToBackStack(null) // 이전 프래그먼트로 돌아가기
                .commit()
        }
        notificationButton = view.findViewById<Button>(R.id.notificationButton)
        notificationButton.setOnClickListener(){// 알림 설정 프래그먼트로 전환
            val fragment_notification_settings = NotificationSettingsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment_notification_settings) // 프래그먼트 교체
                .addToBackStack(null) // 이전 프래그먼트로 돌아가기
                .commit()
        }
        faqButton = view.findViewById<Button>(R.id.faqButton)
        faqButton.setOnClickListener(){// 자주 묻는 질문 프래그먼트로 전환
            val fragment_faq = FAQFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, fragment_faq) // 프래그먼트 교체
                .addToBackStack(null) // 이전 프래그먼트로 돌아가기
                .commit()
        }
    }
}