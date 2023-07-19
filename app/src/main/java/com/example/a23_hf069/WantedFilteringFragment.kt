package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.fragment_wanted_filtering.*
import kotlinx.android.synthetic.main.fragment_wanted_filtering.view.*


class WantedFilteringFragment : Fragment()  {
    lateinit var region_btn: Button

    lateinit var edu_btn1 : Button
    lateinit var edu_btn2 : Button
    lateinit var edu_btn3 : Button
    lateinit var edu_btn4 : Button
    lateinit var edu_btn5 : Button
    lateinit var edu_btn6 : Button

    lateinit var career_btn1 : Button
    lateinit var career_btn2 : Button
    lateinit var career_btn3 : Button

    lateinit var closeDt_btn1 : Button
    lateinit var closeDt_btn2 : Button
    lateinit var closeDt_btn3 : Button
    lateinit var closeDt_btn4 : Button
    lateinit var closeDt_btn5 : Button
    lateinit var closeDt_btn6 : Button

    private var selectedEducation: Int = 0  // 0: 전체, 1: 초등학교, 2: 중학교, ...
    private var selectedCareer: Int = 0  // 0: 전체, 1: 신입, 2: 경력, ...
    private var selectedCloseDate: Int = 0  // 0: 전체, 1: 1일 이내, 2: 3일 이내, ...

    private val selectedEducationList: MutableSet<Int> = mutableSetOf()
    private val selectedCareerList: MutableSet<Int> = mutableSetOf()
    private val selectedCloseDateList: MutableSet<Int> = mutableSetOf()

    lateinit var complete_btn1 : Button //완료버튼

    // 화면 띄우기
    override fun onCreateView( // onCreateView 함수 오버라이드 해줌
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_wanted_filtering, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //지역
        region_btn = view.findViewById<Button>(R.id.region_btn)

        //학력 체크
        edu_btn1 = view.findViewById<Button>(R.id.cb_e_1) //전체
        edu_btn2 = view.findViewById<Button>(R.id.cb_e_2) //초등학교
        edu_btn3 = view.findViewById<Button>(R.id.cb_e_3)  //중학교
        edu_btn4 = view.findViewById<Button>(R.id.cb_e_4)  //고등학교
        edu_btn5 = view.findViewById<Button>(R.id.cb_e_5)  //대학(2년제)
        edu_btn6 = view.findViewById<Button>(R.id.cb_e_6)  //대학(4년제)

        //경력 체크
        career_btn1 = view.findViewById<Button>(R.id.cb_c_1)  //전체
        career_btn2 = view.findViewById<Button>(R.id.cb_c_2)  //신입
        career_btn3 = view.findViewById<Button>(R.id.cb_c_3) //경력

        //마감일 체크
        closeDt_btn1 = view.findViewById<Button>(R.id.cb_d_1)
        closeDt_btn2 = view.findViewById<Button>(R.id.cb_d_2)
        closeDt_btn3 = view.findViewById<Button>(R.id.cb_d_3)
        closeDt_btn4 = view.findViewById<Button>(R.id.cb_d_4)
        closeDt_btn5 = view.findViewById<Button>(R.id.cb_d_5)
        closeDt_btn6 = view.findViewById<Button>(R.id.cb_d_6)


       /* region_btn.setOnClickListener {
            val regionFragment = RegionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, regionFragment)
                .addToBackStack(null)
                .commit()
        }
*/
        // 학력 버튼 클릭 리스너
        edu_btn1.setOnClickListener { SelectedEducation(0) }  // 전체
        edu_btn2.setOnClickListener { SelectedEducation(1) }  // 초등학교
        edu_btn3.setOnClickListener { SelectedEducation(2) }  // 중학교
        edu_btn4.setOnClickListener { SelectedEducation(3) }  // 고등학교
        edu_btn5.setOnClickListener { SelectedEducation(4) }  // 대학(2년제)
        edu_btn6.setOnClickListener { SelectedEducation(5) }  // 대학(4년제)

        // 경력 버튼 클릭 리스너
        career_btn1.setOnClickListener { SelectedCareer(0) }  // 전체
        career_btn2.setOnClickListener { SelectedCareer(1) }  // 신입
        career_btn3.setOnClickListener { SelectedCareer(2) }  // 경력

        // 마감일 버튼 클릭 리스너
        closeDt_btn1.setOnClickListener { SelectedCloseDate(0) }  // 전체
        closeDt_btn2.setOnClickListener { SelectedCloseDate(1) }  // 1일 이내
        closeDt_btn3.setOnClickListener { SelectedCloseDate(2) }  // 3일 이내
        closeDt_btn4.setOnClickListener { SelectedCloseDate(3) }  // 7일 이내
        closeDt_btn5.setOnClickListener { SelectedCloseDate(4) }  // 14일 이내
        closeDt_btn6.setOnClickListener { SelectedCloseDate(5) }  // 30일 이내

    //완료버튼 누르면 필터링된 공고를 WantedFilteredFragment로 전환
        complete_btn1 = view.findViewById<Button>(R.id.complete_btn1)
        complete_btn1.setOnClickListener {
            val wantedFilteredFragment = WantedFilteredFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedFilteredFragment)
                .addToBackStack(null)
                .commit()
        } //만약 조건선택이 완료되지 않은 채 완료버튼을 눌렀다면? ->
    }

    private fun SelectedEducation(selected: Int) {
        if (selectedEducation == 0 || selected > selectedEducation) {
            selectedEducation = selected
            // 1번이나 2번이 선택되었을 때(혹은 둘다), 0번과 같은 결과를 출력(초졸, 중졸은 학력무관과 마찬가지)
            if ((selectedEducation == 1 || selectedEducation == 2) && selectedEducation != 0) {
                // 0번과 같은 결과


            }
        }
        filterItems()
    }

    private fun SelectedCareer(selected: Int) {
        if (selectedCareer == 0 || selected > selectedCareer) {
            selectedCareer = selected
        }
        filterItems()
    }

    private fun SelectedCloseDate(selected: Int) {
        if (selectedCloseDate == 0 || selected > selectedCloseDate) {
            selectedCloseDate = selected
        }
        filterItems()
    }

    private fun filterItems() {
        // 선택한 조건에 따라 필터링된 항목을 리스트에 저장(중복값을 찾기위해)
        val filteredEducationList = if (selectedEducationList.isEmpty()) listOf(0) else selectedEducationList
        val filteredCareerList = if (selectedCareerList.isEmpty()) listOf(0) else selectedCareerList
        val filteredCloseDateList = if (selectedCloseDateList.isEmpty()) listOf(0) else selectedCloseDateList

        // 세 리스트에서 중복되는 값들을 찾습니다.
        val intersectedItems = filteredEducationList.intersect(filteredCareerList).intersect(filteredCloseDateList)

        for (item in intersectedItems) {
            println(item)
            // 필요한 처리를 수행합니다.
        }
    }



    }