package com.example.a23_hf069

import JobPosting
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView

class JobRequestingDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_requesting_detail)

        // 기본 툴바 숨기기
        supportActionBar?.hide()

        val closeButton = findViewById<ImageButton>(R.id.backButton)
        closeButton.setOnClickListener {
            finish()
        }

      /*  // JobPostingDetailActivity에서 데이터 추출
        val intent = intent
        val jobPosting = intent.getParcelableExtra<JobPosting>("jobPosting")
        val companyName = intent.getStringExtra("companyName")

        // companyName을 사용하여 필요한 작업을 수행*/

        // XML 레이아웃의 TextView 등을 찾아서 데이터 표시
        val company = findViewById<TextView>(R.id.company)
        val jobTitleTextView = findViewById<TextView>(R.id.tvJobTitle)
        val experienceRequiredTextView = findViewById<TextView>(R.id.job_experience_required)
        val educationRequiredTextView = findViewById<TextView>(R.id.job_education_required)
        val periodTextView = findViewById<TextView>(R.id.job_period)
        val daysOfWeekTextView = findViewById<TextView>(R.id.job_days_of_week)
        val workingHoursTextView = findViewById<TextView>(R.id.job_working_hours)
        val salaryTextView = findViewById<TextView>(R.id.job_salary)
        val positionTextView = findViewById<TextView>(R.id.job_position)
        val categoryTextView = findViewById<TextView>(R.id.job_category) // 직군
        val requirementsTextView = findViewById<TextView>(R.id.job_requirements)
        val contactNumberTextView = findViewById<TextView>(R.id.job_contact_number)
        val emailTextView = findViewById<TextView>(R.id.job_email)
        val deadlineTextView = findViewById<TextView>(R.id.job_deadline)
        val companyNameLabel = findViewById<TextView>(R.id.companyNameLabel)
        val next_button = findViewById<Button>(R.id.next_button)

        // 가져온 데이터를 레이아웃에 표시
        company.text = ""
        jobTitleTextView.text = "프론트엔드 (Front-end) 개발자 채용"
        experienceRequiredTextView.text = "경력"
        educationRequiredTextView.text = "학사 이상"
        periodTextView.text = "-"
        daysOfWeekTextView.text = "주5일(월~금)"
        workingHoursTextView.text = "09:00-18:00"
        salaryTextView.text = "회사내규에 따름"
        positionTextView.text = "대리급"
        categoryTextView.text = "개발 직무"
        requirementsTextView.text = "정보처리기사 우대"
        contactNumberTextView.text = "010-1234-5678"
        emailTextView.text = "hanium@email.com"
        deadlineTextView.text = "20231013"
        companyNameLabel.text = "company"

        next_button.setOnClickListener {
            // ResumeSelectionActivity로 이동하는 Intent 생성
            val intent = Intent(this, ResumeSelectionActivity::class.java)
            startActivity(intent)
        }


    }
}