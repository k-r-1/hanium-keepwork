package com.example.a23_hf069

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil.setContentView
import com.example.a23_hf069.databinding.ActivityResumeEducationBinding
import com.example.a23_hf069.databinding.FragmentJobPostingBinding
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Calendar

class JobPostingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var calendar = Calendar.getInstance()
    private var postingYear = calendar.get(Calendar.YEAR)
    private var postingMonth = calendar.get(Calendar.MONTH)
    private var postingDay = calendar.get(Calendar.DAY_OF_MONTH)
    lateinit var postingCalButton : Button
    lateinit var edtpostingYear : EditText
    lateinit var edtpostingMonth : EditText
    lateinit var edtpostingDay : EditText
    lateinit var binding : FragmentJobPostingBinding
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

    private val apiService: RetrofitInterface= retrofit.create(RetrofitInterface::class.java)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ...

        // 뒤로 가기 버튼을 처리하기 위한 코드
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                // 백 스택에 추가하지 않고 JobManagementFragment로 이동
                val jobManagementFragment = JobManagementFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, jobManagementFragment)
                    .commit()
                true
            } else {
                false
            }
        }

        // 뒤로가기 버튼 처리
        val backButton: ImageButton = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            // 백 스택에 추가하지 않고 JobManagementFragment로 이동
            FragmentManagerHelper.replaceFragment(
                requireActivity().supportFragmentManager,
                R.id.fl_container,
                JobManagementFragment(),
                addToBackStack = false
            )
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentJobPostingBinding.inflate(inflater, container, false)
        val view = binding.root


        titleEditText = view.findViewById(R.id.titleEditText) // 공고 제목
        periodEditText = view.findViewById(R.id.periodEditText) // 기간
        dayEditText = view.findViewById(R.id.dayEditText) // 요일
        timeEditText = view.findViewById(R.id.timeEditText) // 시간
        payEditText = view.findViewById(R.id.payEditText) // 급여
        positionEditText = view.findViewById(R.id.positionEditText) // 직책
        detailedEditText = view.findViewById(R.id.detailedEditText) // 상세 요강
        contactEditText = view.findViewById(R.id.contactEditText) // 인사 담당자 연락처
        emailEditText = view.findViewById(R.id.emailEditText) // 이메일
        registerButton = view.findViewById(R.id.registerButton) // 등록 버튼


        postingCalButton = view.findViewById(R.id.posting_calendar)
        edtpostingYear = view.findViewById(R.id.edt_posting_year)
        edtpostingMonth = view.findViewById(R.id.edt_posting_month)
        edtpostingDay = view.findViewById(R.id.edt_posting_day)

        /*// 등록 버튼 클릭 이벤트 처리
        registerButton.setOnClickListener {
            // 사용자가 입력한 값 가져오기
            val jobTitle = titleEditText.text.toString().trim() // 공고 제목
            val jobExperienceRequired = careerSpinner.selectedItem.toString() // 경력
            val jobEducationRequired = educationSpinner.selectedItem.toString() // 학력
            val jobPeriod = periodEditText.text.toString().trim() // 기간
            val jobDaysOfWeek = dayEditText.text.toString().trim() // 요일
            val jobWorkingHours = timeEditText.text.toString().trim() // 시간
            val jobSalary = payEditText.text.toString().trim() // 급여
            val jobPosition = positionEditText.text.toString().trim() // 직책
            val jobCategory = occupationEditText.text.toString().trim() // 직군
            val jobRequirements = detailedEditText.text.toString().trim() // 상세 요강
            val jobContactNumber = contactEditText.text.toString().trim() // 인사 담당자 연럭처
            val jobEmail = emailEditText.text.toString().trim() // 인사 담당자 이메일
            val jobDeadline = "${edtpostingYear.text}${edtpostingMonth.text}${edtpostingDay.text}".trim() // 공고 마감일

            // 데이터를 포함하는 JobPosting 객체를 생성합니다.
            val memberModel = JobPosting(jobTitle, jobExperienceRequired, jobEducationRequired, jobPeriod, jobDaysOfWeek, jobWorkingHours, jobSalary, jobPosition, jobCategory, jobRequirements, jobContactNumber, jobEmail, jobDeadline)


            // Retrofit을 사용하여 서버에 사용자 데이터를 보냅니다.


        }*/


        postingCalButton.setOnClickListener{
            val datePickerDialog1 = DatePickerDialog(requireContext(), { _, year, month, day ->
                edtpostingYear.setText(year.toString())
                edtpostingMonth.setText((month + 1).toString())
                edtpostingDay.setText(day.toString())
            }, postingYear, postingMonth, postingDay)
            datePickerDialog1.show()
        }


        val careerList = listOf(
            "경력무관",
            "신입",
            "경력",
        )

        val adapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, careerList)
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.careerSpinner.adapter = adapter1

        val educationList = listOf(
            "학력무관",
            "고등학교 졸업 이상",
            "전문학사 이상",
            "학사 이상",
            "석사 이상",
            "박사 이상"
        )

        val adapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, educationList)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.educationSpinner.adapter = adapter2

        return view
    }

}