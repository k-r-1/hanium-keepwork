package com.example.a23_hf069

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
    private var IP_ADDRESS = "3.34.190.0" // 본인 IP주소를 넣으세요.
    private lateinit var userId: String // 사용자 아이디

    private lateinit var editTextAcademic: EditText
    private lateinit var editTextCareer: EditText
    private lateinit var editTextIntroduction: EditText
    private lateinit var editTextCertificate: EditText
    private lateinit var editTextEducation: EditText
    private lateinit var editTextDesire: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_resume, container, false)

        // 사용자 아이디 받아오기
        if (arguments != null) {
            userId = arguments?.getString("userId", "") ?: ""
        }

        val textID = view.findViewById<TextView>(R.id.tvID)
        textID.text = userId

        editTextAcademic = view.findViewById(R.id.edtAcademic)
        editTextCareer = view.findViewById(R.id.edtCareer)
        editTextIntroduction = view.findViewById(R.id.edtIntroduction)
        editTextCertificate = view.findViewById(R.id.edtCertificate)
        editTextEducation = view.findViewById(R.id.edtEducation)
        editTextDesire = view.findViewById(R.id.edtDesire)
        buttonSubmit = view.findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val personal_id = userId
            val resume_academic = editTextAcademic.text.toString()
            val resume_career = editTextCareer.text.toString()
            val resume_introduction = editTextIntroduction.text.toString()
            val resume_certificate = editTextCertificate.text.toString()
            val resume_learning = editTextEducation.text.toString()
            val resume_desire = editTextDesire.text.toString()

            sendResumeData(personal_id, resume_academic, resume_career, resume_introduction, resume_certificate, resume_learning, resume_desire)
        }

        return view
    }

    private fun sendResumeData(personal_id: String, resume_academic: String, resume_career: String, resume_introduction: String, resume_certificate: String, resume_learning: String, resume_desire: String) {
        val url = "http://$IP_ADDRESS/android_resume_php.php" // PHP 스크립트가 호스팅된 서버의 URL

        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("personal_id", personal_id) // 아이디
            .add("resume_academic", resume_academic) // 학력
            .add("resume_career", resume_career) // 경력
            .add("resume_introduction", resume_introduction) // 자기소개
            .add("resume_certificate", resume_certificate) // 자격사항
            .add("resume_learning", resume_learning) // 교육사항
            .add("resume_desire", resume_desire) // 희망 근무 직군
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // 요청 성공 처리
                val responseData = response.body?.string()
            }
        })
    }
}
