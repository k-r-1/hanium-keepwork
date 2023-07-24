package com.example.a23_hf069

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ResumeChangeActivity : AppCompatActivity() {
    private var IP_ADDRESS = "54.180.120.162" // Replace with your IP address.
    private var userId: String = "" // User ID
    private lateinit var editResumeTitle: EditText
    private lateinit var editTextAcademic: EditText
    private lateinit var editTextCareer: EditText
    private lateinit var editTextIntroduction: EditText
    private lateinit var editTextCertificate: EditText
    private lateinit var editTextEducation: EditText
    private lateinit var editTextDesire: EditText
    private lateinit var buttonSubmit1: Button
    private lateinit var buttonSubmit2: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchResumeDataFromServer()
        setContentView(R.layout.activity_resume_change)

// Get user ID
        userId = intent.getStringExtra("userId") ?: ""

        val textID = findViewById<TextView>(R.id.tvchID)
        textID.text = userId

        editTextAcademic = findViewById(R.id.edtchAcademic)
        editResumeTitle = findViewById(R.id.edtchTitle)
        editTextCareer = findViewById(R.id.edtchCareer)
        editTextIntroduction = findViewById(R.id.edtchIntroduction)
        editTextCertificate = findViewById(R.id.edtchCertificate)
        editTextEducation = findViewById(R.id.edtchEducation)
        editTextDesire = findViewById(R.id.edtchDesire)
        buttonSubmit1 = findViewById(R.id.buttonchSubmit_temporary)
        buttonSubmit2 = findViewById(R.id.buttonchSubmit_complete)

        buttonSubmit1.setOnClickListener { // 임시 저장
            val personal_id = userId
            val resume_title = editResumeTitle.text.toString()
            val resume_academic = editTextAcademic.text.toString()
            val resume_career = editTextCareer.text.toString()
            val resume_introduction = editTextIntroduction.text.toString()
            val resume_certificate = editTextCertificate.text.toString()
            val resume_learning = editTextEducation.text.toString()
            val resume_desire = editTextDesire.text.toString()
            val resume_complete = "작성 중"

            sendResumeData(
                personal_id,
                resume_title,
                resume_academic,
                resume_career,
                resume_introduction,
                resume_certificate,
                resume_learning,
                resume_desire,
                resume_complete
            )

            Toast.makeText(this, "이력서가 임시저장되었습니다", Toast.LENGTH_SHORT).show()
        }

        buttonSubmit2.setOnClickListener { // 작성완료
            val personal_id = userId
            val resume_title = editResumeTitle.text.toString()
            val resume_academic = editTextAcademic.text.toString()
            val resume_career = editTextCareer.text.toString()
            val resume_introduction = editTextIntroduction.text.toString()
            val resume_certificate = editTextCertificate.text.toString()
            val resume_learning = editTextEducation.text.toString()
            val resume_desire = editTextDesire.text.toString()
            val resume_complete = "작성 완료"

            sendResumeData(
                personal_id,
                resume_title,
                resume_academic,
                resume_career,
                resume_introduction,
                resume_certificate,
                resume_learning,
                resume_desire,
                resume_complete
            )

            Toast.makeText(this, "이력서가 작성완료되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    // 이력서 정보를 불러오기 위한 메서드
    private fun fetchResumeDataFromServer() {
        val url = "http://$IP_ADDRESS/android_resume_change.php" // 이력서 정보를 가져올 서버 주소

        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("personal_id", userId) // 사용자 ID를 서버에 전달
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                // 요청 성공 시 호출되는 콜백 메서드
                val responseData = response.body?.string()
                // responseData를 파싱하여 이력서 정보를 화면에 표시
                if (responseData != null) {
                    runOnUiThread {
                        displayResumeData(responseData)
                    }
                }
            }
        })
    }

    // 서버로부터 받은 이력서 정보를 화면에 표시
    private fun displayResumeData(responseData: String) {
        val gson = Gson()
        val resumeData = gson.fromJson(responseData, ResumeData::class.java)

        // resumeData가 null인 경우 처리
        if (resumeData == null) {
            Toast.makeText(this, "이력서 정보를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 서버로부터 받은 이력서 정보를 EditText 등의 뷰에 표시
        editResumeTitle.setText(resumeData.resume_title)
        editTextAcademic.setText(resumeData.resume_academic)
        editTextCareer.setText(resumeData.resume_career)
        editTextIntroduction.setText(resumeData.resume_introduction)
        editTextCertificate.setText(resumeData.resume_certificate)
        editTextEducation.setText(resumeData.resume_learning)
        editTextDesire.setText(resumeData.resume_desire)

        // 이력서 작성 상태에 따라 버튼 텍스트 변경
        if (resumeData.resume_complete == "작성 중") {
            buttonSubmit1.text = "임시저장"
        } else {
            buttonSubmit2.text = "작성완료"
        }
    }

    private fun sendResumeData(
        personal_id: String,
        resume_title: String,
        resume_academic: String,
        resume_career: String,
        resume_introduction: String,
        resume_certificate: String,
        resume_learning: String,
        resume_desire: String,
        resume_complete: String
    ) {
        val url = "http://$IP_ADDRESS/android_resume_write_php.php" // URL of the hosting server with PHP script

        val client = OkHttpClient()

        val formBody = FormBody.Builder()
            .add("personal_id", personal_id) // ID
            .add("resume_title", resume_title) // Title
            .add("resume_academic", resume_academic) // Education
            .add("resume_career", resume_career) // Career
            .add("resume_introduction", resume_introduction) // Introduction
            .add("resume_certificate", resume_certificate) // Certification
            .add("resume_learning", resume_learning) // Education history
            .add("resume_desire", resume_desire) // Desired job position
            .add("resume_complete", resume_complete)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
// Handle request failure
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
// Handle request success
                val responseData = response.body?.string()
            }
        })
    }
}