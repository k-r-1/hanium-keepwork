package com.example.a23_hf069

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar

class CorporateHomeFragment : Fragment() {

    private lateinit var notificationButton: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 툴바에서 notificationButton을 찾아 클릭 리스너를 설정
        notificationButton = view.findViewById(R.id.notifications)
        notificationButton.setOnClickListener {
            // notificationButton을 클릭하면 알림 화면 CorporateHomeNotificationFragment로 전환
            val CorporateHomeNotificationFragment = CorporateHomeNotificationFragment()
            FragmentManagerHelper.replaceFragment(
                requireActivity().supportFragmentManager,
                R.id.fl_container,
                CorporateHomeNotificationFragment
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_corporate_home, container, false)
    }


}