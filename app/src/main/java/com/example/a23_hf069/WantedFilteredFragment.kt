package com.example.a23_hf069

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class WantedFilteredFragment : Fragment() {
    private lateinit var jobListView: ListView
    private lateinit var jobList: List<Job>
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button
    private var currentPage = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wanted_work_net, container, false)

        // UI 요소 초기화
        jobListView = view.findViewById(R.id.jobListView)
        prevButton = view.findViewById(R.id.prevButton)
        nextButton = view.findViewById(R.id.nextButton)

        // 이전 페이지 버튼 클릭 이벤트 처리
        prevButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                fetchJobData()
            }
        }

        // 다음 페이지 버튼 클릭 이벤트 처리
        nextButton.setOnClickListener {
            currentPage += 1
            fetchJobData()
        }

        // WantedFilteringFragment.kt에서 argument를 넘겨받아 필터링된 데이터 출력
        //fetchJobData()

        return view
    }

    private fun fetchJobData() {
        val url =
            "http://openapi.work.go.kr/opi/opi/opia/wantedApi.do?authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&callTp=L&returnType=XML&startPage=$currentPage&display=10"
        FetchJobData().execute(url)
    }

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

        override fun onPostExecute(result: List<Job>) {
            jobList = result
            showJobList()
        }
    }

    private fun showJobList() {
        val adapter = CustomAdapter2(requireActivity(), jobList)
        jobListView.adapter = adapter

        jobListView.setOnItemClickListener { _, _, position, _ ->
            val job = jobList[position]
            val intent = JobDetailActivity2.newIntent(requireContext(), job)
            startActivity(intent)
        }
    }
}

class CustomAdapter2(private val context2: Context, private val jobList: List<Job>) :
    ArrayAdapter<Job>(context2, R.layout.job_item, jobList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context2).inflate(R.layout.job_item, parent, false)

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

class JobDetailActivity2 : AppCompatActivity() {
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
            return Intent(context2, JobDetailActivity2::class.java).apply {
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