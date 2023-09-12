package com.example.a23_hf069

import JobPosting
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class JobPostingDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_posting_detail)

      /*  // Intent에서 채용공고 데이터를 가져옴
        val jobPosting = intent.getParcelableExtra<JobPosting>("jobPosting")

        // XML 레이아웃의 TextView 등을 찾아서 데이터 표시
        val jobTitleTextView = findViewById<TextView>(R.id.tvJobTitle)
        val experienceRequiredTextView = findViewById<TextView>(R.id.tvExperienceRequired)
        val educationRequiredTextView = findViewById<TextView>(R.id.tvEducationRequired)
        val periodTextView = findViewById<TextView>(R.id.tvPeriod)
        val daysOfWeekTextView = findViewById<TextView>(R.id.tvDaysOfWeek)
        val workingHoursTextView = findViewById<TextView>(R.id.tvWorkingHours)
        val salaryTextView = findViewById<TextView>(R.id.tvSalary)
        val positionTextView = findViewById<TextView>(R.id.tvPosition)
        val categoryTextView = findViewById<TextView>(R.id.tvCategory)
        val requirementsTextView = findViewById<TextView>(R.id.tvRequirements)
        val contactNumberTextView = findViewById<TextView>(R.id.tvContactNumber)
        val emailTextView = findViewById<TextView>(R.id.tvEmail)
        val deadlineTextView = findViewById<TextView>(R.id.tvDeadline)

        // 가져온 데이터를 레이아웃에 표시
        jobTitleTextView.text = jobPosting?.job_title
        experienceRequiredTextView.text = jobPosting?.job_experience_required
        educationRequiredTextView.text = jobPosting?.job_education_required
        periodTextView.text = jobPosting?.job_period
        daysOfWeekTextView.text = jobPosting?.job_days_of_week
        workingHoursTextView.text = jobPosting?.job_working_hours
        salaryTextView.text = jobPosting?.job_salary
        positionTextView.text = jobPosting?.job_position
        categoryTextView.text = jobPosting?.job_category
        requirementsTextView.text = jobPosting?.job_requirements
        contactNumberTextView.text = jobPosting?.job_contact_number
        emailTextView.text = jobPosting?.job_email
        deadlineTextView.text = jobPosting?.job_deadline*/
    }
}