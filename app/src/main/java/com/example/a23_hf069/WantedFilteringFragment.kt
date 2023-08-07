package com.example.a23_hf069


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.a23_hf069.*
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.StringReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class WantedFilteringFragment : Fragment() {
    private val baseUrl =
        "http://openapi.work.go.kr/opi/opi/opia/wantedApi.do?authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&callTp=L&returnType=XML&display=100"
    //완료 버튼
    lateinit var complete_btn: Button
    //지역,직종
    lateinit var regioncl_btn: Button
    lateinit var jobcl_btn: Button
    lateinit var tv_jobcl_selected: TextView
    lateinit var tv_regioncl_selected: TextView
    //학력 -> 체크박스가 아닌 라디오 버튼으로 바꾸기
    lateinit var cbAllEdu: CheckBox // 학력무관
    lateinit var cbHighEdu: CheckBox // 고졸
    lateinit var cbUniv2: CheckBox // 대졸(2~3년)
    lateinit var cbUniv4: CheckBox // 대졸(4년)
    //경력 > 체크박스가 아닌 라디오 버튼으로 바꾸기
    lateinit var cbAllCareer : CheckBox // 경력무관
    lateinit var cbFresh : CheckBox // 신입
    lateinit var cbExperienced : CheckBox // 경력
    //마감일 > 체크박스가 아닌 라디오 버튼으로 바꾸기
    lateinit var cbToday : CheckBox//오늘까지
    lateinit var cbTomorrow : CheckBox//내일까지
    lateinit var cb7days : CheckBox//일주일 이내
    lateinit var cb30days : CheckBox//한달 이내
    lateinit var cb60days : CheckBox//두달 이내

    private lateinit var wantedList: List<Wanted>
    private val sharedSelectionViewModel: SharedSelectionViewModel by activityViewModels() // 필터링된 리스트를 전달하는 viewModel 객체 생성
    lateinit var selectedJob : String
    lateinit var selectedRegion : String

    // 필터링 키워드
    private var keywordRegion = ""
    private var keywordJob = ""
    private var keywordEdu = ""
    private var keywordCareer= ""
    private var keywordCloseDt = ""



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wanted_filtering, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //완료 버튼 초기화
        complete_btn = view.findViewById<Button>(R.id.complete_btn1)
        //지역 선택 초기화
        regioncl_btn = view.findViewById<Button>(R.id.regioncl_btn)
        //직종 선택 초기화
        jobcl_btn = view.findViewById<Button>(R.id.jobcl_btn)

        // 선택된 지역 정보를 나타낼 TextView 초기화
        tv_regioncl_selected = view.findViewById(R.id.tv_regioncl_selected)

        // 선택된 직종 정보를 나타낼 TextView 초기화
        tv_jobcl_selected = view.findViewById(R.id.tv_jobcl_selected)

        // ViewModel에서 선택된 지역 정보를 가져와서 TextView에 설정
        selectedRegion = sharedSelectionViewModel.selectedRegion.toString()
        tv_regioncl_selected.text = selectedRegion

        // ViewModel에서 선택된 직종 정보를 가져와서 TextView에 설정
        selectedJob = sharedSelectionViewModel.selectedJob.toString()
        tv_jobcl_selected.text = selectedJob


        // CheckBox 변수들을 초기화
        cbAllCareer = view.findViewById(R.id.cb_c_1)
        cbFresh = view.findViewById(R.id.cb_c_2)
        cbExperienced = view.findViewById(R.id.cb_c_3)

        cbAllEdu = view.findViewById(R.id.cb_e_1)
        cbHighEdu = view.findViewById(R.id.cb_e_4)
        cbUniv2 = view.findViewById(R.id.cb_e_5)
        cbUniv4 = view.findViewById(R.id.cb_e_6)

        cbToday = view.findViewById(R.id.cb_d_2)
        cbTomorrow = view.findViewById(R.id.cb_d_3)
        cb7days = view.findViewById(R.id.cb_d_4)
        cb30days = view.findViewById(R.id.cb_d_5)
        cb60days = view.findViewById(R.id.cb_d_6)

        // 완료 버튼이 눌렸을 때 지역,직종 변수 및 학력,경력,마감일 체크박스 확인 -> 선택된 조건에 해당하는 공고목록 가져와서 UI에 업데이트
        complete_btn.setOnClickListener {


            // 선택한 지역이 있을 경우 키워드에 해당 지역이름 넣기
            if(selectedRegion != ""){
                keywordRegion = selectedRegion
            }
            // 선택한 직종이 있을 경우 필터링하기

            // 학력 중 선택한 체크박스가 있을 경우 필터링하기
            if(cbAllEdu.isChecked){
                keywordEdu = "학력무관"
            }
            else if(cbHighEdu.isChecked){
                keywordEdu = "고졸"
            }
            else if(cbUniv2.isChecked){
                keywordEdu = "대졸(2~3년)"
            }
            else if(cbUniv4.isChecked){
                keywordEdu = "대졸4년"
            }

            // 경력 중 선택한 체크박스가 있을 경우 필터링하기
            if(cbAllCareer.isChecked){
                keywordCareer = "관계없음"
            }
            else if(cbFresh.isChecked){
                keywordCareer = "신입"
            }
            else if(cbExperienced.isChecked){
                keywordCareer = "경력"
            }

            // 마감일 중 선택한 체크박스가 있을 경우 필터링하기
            if(cbToday.isChecked){
                keywordCloseDt = "today"
            }
            else if(cbTomorrow.isChecked){
                keywordCloseDt = "tomorrow"
            }
            else if(cb7days.isChecked){
                keywordCloseDt = "7days"
            }
            else if(cb30days.isChecked){
                keywordCloseDt = "30days"
            }
            else if(cb60days.isChecked){
                keywordCloseDt = "60days"
            }

            //채용정보 불러오기
            fetchWantedList()

            // 선택된 조건들을 반영한 sharedSelectionViewModel 속 리스트들을 반영한 리스트뷰 화면으로 전환
            val wantedResultFragment = WantedResultFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedResultFragment)
                .addToBackStack(null)
                .commit()
        } // complete_btn 리스너 종료

        // 지역선택 버튼 눌렸을 때 지역선택 화면으로 전환
        regioncl_btn.setOnClickListener {
            val regionSelectionFragment = RegionSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, regionSelectionFragment)
                .addToBackStack(null)
                .commit()
        }

        // 직종선택 버튼 눌렸을 때 직종선택 화면으로 전환
        jobcl_btn.setOnClickListener {
            val jobSelectionFragment = JobWorkNetSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, jobSelectionFragment)
                .addToBackStack(null)
                .commit()
        }

    }

    // 키워드에 해당하는 채용공고 가져와서 sharedSelectionViewModel의 리스트에 저장
    private fun fetchWantedList(page: Int = 1){
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("$baseUrl&startPage=$page")
            .build()
        var result: List<Wanted> = emptyList()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.printStackTrace())
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val xmlString = response.body?.string() // url에 있는 모든 글자 다가져오기
                    result = parseXmlResponse(xmlString) // parsing한 후 리스트화 하기
                    wantedList = result

                    val filteredList = wantedList.filter { // 리스트 필터링하기 -> 모든 조건을 동시에 만족하는 채용공고 가져오기
                        it.region == keywordRegion && it.minEdubg == keywordEdu
                                && it.career == keywordCareer
                    }
                    //출력해서 확인하기
                    for(i in filteredList){
                        println("${i.title}")
                        println("${i.minEdubg}")
                        println("${i.career}")
                        println("${i.closeDt}")
                        println("${i.region}")
                        println("------------------------------")

                    }
                    //UI 업데이트할 수 있도록 뷰모델에 업데이트
                    sharedSelectionViewModel.updateFilteredList(filteredList)
                }// if 응답이 성공적일때

                else {
                    showErrorToast()
                } //if 응답 실패일때
            } //onResponse 함수 종료
        }) //callback 종료

        } // fetchWantedList 함수 종료

    data class Wanted(
        val company: String,
        val title: String,
        val salTpNm: String?,
        val sal: String?,
        val region: String?,
        val holidayTpNm: String?,
        val minEdubg: String?,
        val career: String?,
        val closeDt: String?,
        val wantedMobileInfoUrl: String?,
        val jobsCd: String?,
        val infoSvc: String?
    )

    private fun parseXmlResponse(xmlResponse: String?): List<Wanted> {
        val wantedList = mutableListOf<Wanted>()
        val factory = XmlPullParserFactory.newInstance()
        val xpp = factory.newPullParser()
        xpp.setInput(StringReader(xmlResponse))

        var eventType = xpp.eventType
        var company: String? = null // 회사명
        var title: String? = null // 채용제목
        var salTpNm: String? = null // 임금형태
        var sal: String? = null // 급여
        var region: String? = null // 근무지역
        var holidayTpNm: String? = null // 근무형태
        var minEdubg: String? = null // 최소학력
        var career: String? = null // 경력
        var closeDt: String? = null // 마감일자
        var wantedMobileInfoUrl: String? = null // 워크넷 모바일 채용정보 URL
        var jobsCd: String? = null // 직종코드
        var infoSvc: String? = null // 정보제공처

        while (eventType != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when (xpp.name) {
                        "company" -> company = xpp.nextText()
                        "title" -> title = xpp.nextText()
                        "salTpNm" -> salTpNm = xpp.nextText()
                        "sal" -> sal = xpp.nextText()
                        "region" -> region = xpp.nextText()
                        "holidayTpNm" -> holidayTpNm = xpp.nextText()
                        "minEdubg" -> minEdubg = xpp.nextText()
                        "career" -> career = xpp.nextText()
                        "closeDt" -> closeDt = xpp.nextText()
                        "wantedMobileInfoUrl" -> wantedMobileInfoUrl = xpp.nextText()
                        "jobsCd" -> jobsCd = xpp.nextText()
                        "infoSvc" -> infoSvc = xpp.nextText()
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (xpp.name == "wanted") {
                        company?.let { c ->
                            title?.let { t ->
                                wantedList.add(
                                    Wanted(
                                        c, t, salTpNm, sal, region, holidayTpNm,
                                        minEdubg, career, closeDt, wantedMobileInfoUrl, jobsCd, infoSvc
                                    )
                                )
                            }
                        }
                        company = null
                        title = null
                        salTpNm = null
                        sal = null
                        region = null
                        holidayTpNm = null
                        minEdubg = null
                        career = null
                        closeDt = null
                        wantedMobileInfoUrl = null
                        jobsCd = null
                        infoSvc = null
                    }
                }
            }
            eventType = xpp.next()
        } // while문 종료
        return wantedList
    }


    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Failed to fetch wanted list.", Toast.LENGTH_SHORT).show()
    }
}