package com.example.a23_hf069

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class JobPostingActivity : AppCompatActivity() {
    private var calendar = Calendar.getInstance()
    private var postingYear = calendar.get(Calendar.YEAR)
    private var postingMonth = calendar.get(Calendar.MONTH)
    private var postingDay = calendar.get(Calendar.DAY_OF_MONTH)
    lateinit var postingCalButton: Button
    lateinit var edtpostingYear: EditText
    lateinit var edtpostingMonth: EditText
    lateinit var edtpostingDay: EditText
    lateinit var careerSpinner: Spinner
    lateinit var educationSpinner: Spinner

    // 뷰 요소들을 선언
    private lateinit var titleEditText: TextInputEditText // 공고 제목
    private lateinit var periodEditText: TextInputEditText // 기간
    private lateinit var dayEditText: TextInputEditText // 요일
    private lateinit var timeEditText: TextInputEditText // 시간
    private lateinit var payEditText: TextInputEditText // 급여
    private lateinit var positionEditText: TextInputEditText // 직책
    private lateinit var occupationEditText: TextInputEditText // 직군
    private lateinit var detailedEditText: EditText // 상세 요강
    private lateinit var contactEditText: TextInputEditText // 인사 담당자 연락처
    private lateinit var emailEditText: TextInputEditText // 이메일
    private lateinit var registerButton: Button // 등록 버튼

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(RetrofitInterface.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService: RetrofitInterface = retrofit.create(RetrofitInterface::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_posting)

        // 기본 툴바 숨기기
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

        // backButton 클릭 시 뒤로가기 처리
        val backButton: ImageButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed() // 뒤로가기 처리
        }


        titleEditText = findViewById(R.id.titleEditText) // 공고 제목
        periodEditText = findViewById(R.id.periodEditText) // 기간
        dayEditText = findViewById(R.id.dayEditText) // 요일
        timeEditText = findViewById(R.id.timeEditText) // 시간
        payEditText = findViewById(R.id.payEditText) // 급여
        positionEditText = findViewById(R.id.positionEditText) // 직책
        detailedEditText = findViewById(R.id.detailedEditText) // 상세 요강
        contactEditText = findViewById(R.id.contactEditText) // 인사 담당자 연락처
        emailEditText = findViewById(R.id.emailEditText) // 이메일
        registerButton = findViewById(R.id.registerButton) // 등록 버튼

        postingCalButton = findViewById(R.id.posting_calendar)
        edtpostingYear = findViewById(R.id.edt_posting_year)
        edtpostingMonth = findViewById(R.id.edt_posting_month)
        edtpostingDay = findViewById(R.id.edt_posting_day)

        postingCalButton.setOnClickListener {
            val datePickerDialog1 = DatePickerDialog(
                this,
                { _, year, month, day ->
                    edtpostingYear.setText(year.toString())
                    edtpostingMonth.setText((month + 1).toString())
                    edtpostingDay.setText(day.toString())
                },
                postingYear,
                postingMonth,
                postingDay
            )
            datePickerDialog1.show()
        }

        val careerList = listOf(
            "경력무관",
            "신입",
            "경력",
        )

        val adapter1 =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, careerList)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        careerSpinner = findViewById(R.id.careerSpinner)
        careerSpinner.adapter = adapter1

        val educationList = listOf(
            "학력무관",
            "고등학교 졸업 이상",
            "전문학사 이상",
            "학사 이상",
            "석사 이상",
            "박사 이상"
        )

        val adapter2 =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, educationList)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        educationSpinner = findViewById(R.id.educationSpinner)
        educationSpinner.adapter = adapter2
    }
}
