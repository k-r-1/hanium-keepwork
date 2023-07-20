package com.example.a23_hf069

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate


class WantedFilteringFragment : Fragment() {
    private lateinit var jobList: List<Job>
    private lateinit var jobListView: ListView

    lateinit var regioncl_btn: Button
    lateinit var jobcl_btn: Button
    lateinit var tv_jobcl_selected: TextView
    lateinit var tv_regioncl_selected: TextView

    lateinit var edu_btn1: Button
    lateinit var edu_btn2: Button
    lateinit var edu_btn3: Button
    lateinit var edu_btn4: Button
    lateinit var edu_btn5: Button
    lateinit var edu_btn6: Button

    lateinit var career_btn1: Button
    lateinit var career_btn2: Button
    lateinit var career_btn3: Button

    lateinit var closeDt_btn1: Button
    lateinit var closeDt_btn2: Button
    lateinit var closeDt_btn3: Button
    lateinit var closeDt_btn4: Button
    lateinit var closeDt_btn5: Button
    lateinit var closeDt_btn6: Button

    private var selectedEducation: Int = 0  // 0: 전체, 1: 초등학교, 2: 중학교, ...
    private var selectedCareer: Int = 0  // 0: 전체, 1: 신입, 2: 경력
    private var selectedCloseDate: Int = 0  // 0: 전체, 1: 1일 이내, 2: 3일 이내, ...

    private val selectedEducationList: MutableSet<Int> = mutableSetOf()
    private val selectedCareerList: MutableSet<Int> = mutableSetOf()
    private val selectedCloseDateList: MutableSet<Int> = mutableSetOf()

    lateinit var complete_btn1: Button //완료버튼

    // 화면 띄우기
    override fun onCreateView( // onCreateView 함수 오버라이드
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_wanted_filtering, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //지역 선택
        regioncl_btn = view.findViewById<Button>(R.id.regioncl_btn)
        //직종 선택
        jobcl_btn = view.findViewById<Button>(R.id.jobcl_btn)

        jobcl_btn.setOnClickListener {
            val jobSelectionFragment = JobWorkNetSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, jobSelectionFragment)
                .addToBackStack(null)
                .commit()
        }

        regioncl_btn.setOnClickListener {
            val regionSelectionFragment = RegionSelectionFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, regionSelectionFragment)
                .addToBackStack(null)
                .commit()
        }

        // 선택된 지역 정보를 나타낼 TextView 초기화
        tv_regioncl_selected = view.findViewById(R.id.tv_regioncl_selected)

        // RegionSelectionFragment에서 선택된 직종 정보를 가져와서 tv_regioncl_selected에 설정
        val selectedRegion = arguments?.getString("selectedRegion")
        tv_regioncl_selected.text = selectedRegion

        // 선택된 직종 정보를 나타낼 TextView 초기화
        tv_jobcl_selected = view.findViewById(R.id.tv_jobcl_selected)

        // JobSelectionFragment에서 선택된 직종 정보를 가져와서 tv_jobcl_selected에 설정
        val selectedJob = arguments?.getString("selectedJob")
        tv_jobcl_selected.text = selectedJob





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
        closeDt_btn2.setOnClickListener { SelectedCloseDate(1) }  // 오늘
        closeDt_btn3.setOnClickListener { SelectedCloseDate(2) }  // 내일
        closeDt_btn4.setOnClickListener { SelectedCloseDate(3) }  // 1주이내
        closeDt_btn5.setOnClickListener { SelectedCloseDate(4) }  // 30일 이내
        closeDt_btn6.setOnClickListener { SelectedCloseDate(5) }  // 30일 이상

        //완료버튼 누르면 필터링된 공고를 WantedFilteredFragment로 전환
        complete_btn1 = view.findViewById<Button>(R.id.complete_btn1)
        complete_btn1.setOnClickListener {
            val fetchJobData = FetchJobData()
            fetchJobData.execute("http://openapi.work.go.kr/opi/opi/opia/wantedApi.do?authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&callTp=L&returnType=XML&startPage=1&display=10")

        // 새로운 리스트를 생성하여 필터링된 항목을 저장
            val filteredJobList = fetchJobData.get().toMutableList()

        // WantedFilteredFragment로 전달
            val wantedFilteredFragment = WantedFilteredFragment()
            val args = Bundle()
            args.putParcelableArrayList("filteredJobList", ArrayList(filteredJobList))
            wantedFilteredFragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedFilteredFragment)
                .addToBackStack(null)
                .commit()
        } //만약 조건선택이 완료되지 않은 채 완료버튼을 눌렀다면? ->
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun SelectedEducation(selected: Int) {
        if (selectedEducation == 0 || selected > selectedEducation) {
            selectedEducation = selected
            // 1번이나 2번이 선택되었을 때(혹은 둘다), 0번과 같은 결과를 출력(초졸, 중졸은 학력무관과 마찬가지)
            if ((selectedEducation == 1 || selectedEducation == 2) && selectedEducation != 0) {
                // 0번과 같은 결과
                selectedEducationList.add(0)
            }
        }
        filterItems()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun SelectedCareer(selected: Int) {
        if (selectedCareer == 0 || selected > selectedCareer) {
            selectedCareer = selected
        }
        filterItems()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun SelectedCloseDate(selected: Int) {
        if (selectedCloseDate == 0 || selected > selectedCloseDate) {
            selectedCloseDate = selected
        }
        filterItems()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun filterItems() {
        // 선택한 조건에 따라 필터링된 항목을 리스트에 저장(중복값을 찾기위해)
        val filteredEducationList =
            if (selectedEducationList.isEmpty()) listOf(0) else selectedEducationList
        val filteredCareerList = if (selectedCareerList.isEmpty()) listOf(0) else selectedCareerList
        val filteredCloseDateList =
            if (selectedCloseDateList.isEmpty()) listOf(0) else selectedCloseDateList

        //각각의 리스트에 담긴 Int형의 데이터들을 String 타입으로 변환
        val convertedEducationList = filteredEducationList.map { value ->
            when (value) {
                0 -> "00"
                1 -> "01"
                2 -> "02"
                3 -> "03"
                4 -> "04"
                5 -> "05"
                else -> {}
            }
        }
        val convertedCareerList = filteredCareerList.map { value ->
            when (value) {
                0 -> "N"
                1 -> "E"
                2 -> "Z"

                else -> {}
            }
        }
    val convertedCloseDateList = filteredCloseDateList.map { value ->
        val today = LocalDate.now()
        val deadlineDate = when (value) {
           // 0 ->      //전체
            1 -> today // 오늘
            2 -> today.plusDays(1) // 내일
            3 -> today.plusDays(7) // 1주 이내
            4 -> today.plusMonths(1) // 한달 이내
            else -> {today.plusMonths(1) // 한달 이상
             }
        }
        deadlineDate.toString() // 날짜를 문자열로 변환하여 반환
        //xml파일의 <closeDt>마감일날짜 정보에서 오늘 날짜를 뺀 값
    }
}

    //워크넷 api 채용목록 xml파일 parsing하는 코드
    private inner class FetchJobData : AsyncTask<String, Void, List<Job>>() {
        override fun doInBackground(vararg urls: String): List<Job> {
            val urlString = urls[0]
            var result: List<Job> = emptyList()
            var connection: HttpURLConnection? = null

            try {
                val url = URL(urlString)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.readTimeout = 15 * 1000
                connection.connectTimeout = 15 * 1000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    result = parseXml(inputStream)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }

            return result
        }

        private fun parseXml(inputStream: InputStream): List<Job> {
            val jobList = mutableListOf<Job>()
            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()
            xpp.setInput(inputStream, null)

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
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (xpp.name == "wanted") {
                            company?.let { c ->
                                title?.let { t ->
                                    jobList.add(
                                        Job(
                                            c, t, salTpNm, sal, region, holidayTpNm,
                                            minEdubg, career, closeDt, wantedMobileInfoUrl, jobsCd
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
                        }
                    }
                }
                eventType = xpp.next()
            }

            return jobList
        }

        //각각의 리스트에서 추출된 값을 파싱된 xml파일과 대조 -> 중복 건 찾기
        @RequiresApi(Build.VERSION_CODES.O)
        private fun isJobMatched(job: Job): Boolean {
            val convertedEducationList = listOf(
                "00", "01", "02", "03", "04", "05"
            ) // 전체, 초등학교, 중학교, 고등학교, 대학(2년제), 대학(4년제)
            val convertedCareerList = listOf(
                "N", "E", "Z"
            ) // 전체, 신입, 경력
            val convertedCloseDateList = listOf(
                LocalDate.now().toString(), // 오늘
                LocalDate.now().plusDays(1).toString(), // 내일
                LocalDate.now().plusDays(7).toString(), // 1주 이내
                LocalDate.now().plusMonths(1).toString(), // 한달 이내
                LocalDate.now().plusMonths(1).toString() // 한달 이상
            )

            return convertedEducationList.contains(job.minEdubg) &&
                    convertedCareerList.contains(job.career) &&
                    convertedCloseDateList.contains(job.closeDt)
        }
        override fun onPostExecute(result: List<Job>) {
            jobList = result
            showJobList()
        }
    }

    private fun showJobList() {
        val adapter = CustomAdapter3(requireActivity(), jobList)
        jobListView.adapter = adapter

        jobListView.setOnItemClickListener { _, _, position, _ ->
            val job = jobList[position]
            val intent = JobDetailActivity3.newIntent(requireContext(), job)
            startActivity(intent)
        }
    }
}

class CustomAdapter3(private val context3: Context, private val jobList: List<Job>) :
    ArrayAdapter<Job>(context3, R.layout.job_item, jobList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context3).inflate(R.layout.job_item, parent, false)

        val job = jobList[position]

        val titleTextView: TextView = view.findViewById(R.id.titleTextView)
        val companyTextView: TextView = view.findViewById(R.id.companyTextView)
        val regionContTextView: TextView = view.findViewById(R.id.regionContTextView)

        titleTextView.text = job.title
        companyTextView.text = job.company
        regionContTextView.text = job.region

        return view
    }
}

class JobDetailActivity3 : AppCompatActivity() {
    private lateinit var backButton: ImageView
    private lateinit var company: TextView // 회사명
    private lateinit var title: TextView // 제목
    private lateinit var salTpNm: TextView // 임금형태
    private lateinit var sal: TextView // 급여
    private lateinit var region: TextView // 근무지역
    private lateinit var holidayTpNm: TextView // 근무형태
    private lateinit var minEdubg: TextView // 최소학력
    private lateinit var career: TextView // 경력
    private lateinit var closeDt: TextView // 마감일자
    private lateinit var wantedMobileInfoUrl: TextView // 워크넷 모바일 채용정보 URL
    private lateinit var jobsCd: TextView // 직종코드

    companion object {
        private const val JOB_EXTRA = "job"

        fun newIntent(context2: Context, job: Job): Intent {
            return Intent(context2, JobDetailActivity3::class.java).apply {
                putExtra(JOB_EXTRA, job)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        // 기본 툴바 숨기기
        supportActionBar?.hide()

        backButton = findViewById(R.id.backButton)
        company = findViewById(R.id.company)
        title = findViewById(R.id.title)
        salTpNm = findViewById(R.id.salTpNm)
        sal = findViewById(R.id.sal)
        region = findViewById(R.id.region)
        holidayTpNm = findViewById(R.id.holidayTpNm)
        minEdubg = findViewById(R.id.minEdubg)
        career = findViewById(R.id.career)
        closeDt = findViewById(R.id.closeDt)
        wantedMobileInfoUrl = findViewById(R.id.wantedMobileInfoUrl)
        jobsCd = findViewById(R.id.jobsCd)

        val job = intent.getParcelableExtra<Job>(JOB_EXTRA)

        company.text = job?.company
        title.text = job?.title
        salTpNm.text = job?.salTpNm
        sal.text = job?.sal
        region.text = job?.region
        holidayTpNm.text = job?.holidayTpNm
        minEdubg.text = job?.minEdubg
        career.text = job?.career
        closeDt.text = job?.closeDt
        wantedMobileInfoUrl.text = job?.wantedMobileInfoUrl
        jobsCd.text = job?.jobsCd

        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    }