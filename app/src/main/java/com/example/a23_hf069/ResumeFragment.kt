package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import okhttp3.*
import java.io.IOException

class ResumeFragment : Fragment() {
    private var IP_ADDRESS = "13.125.179.180" // 본인 IP주소를 넣으세요.
    private lateinit var userId: String // 사용자 아이디

    private lateinit var buttonSubmit: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_resume, container, false)

        // 사용자 아이디 받아오기
        if (arguments != null) {
            userId = arguments?.getString("userId", "") ?: ""
        }

        val textID = view.findViewById<TextView>(R.id.tvID1)
        textID.text = userId

        buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val intent = Intent(requireContext(), ResumeWriteActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        return view
    }
}
