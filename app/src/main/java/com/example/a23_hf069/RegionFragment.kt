package com.example.a23_hf069

import android.content.Context
import android.content.Intent
import android.graphics.Region
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.fragment_wanted_work_net.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class RegionFragment : Fragment() {
    private lateinit var regionList: List<Region>
    private lateinit var listView1: ListView
    private lateinit var listView2: ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_region, container, false)

        // UI 요소 초기화
        listView1 = view.findViewById(R.id.lv_superRegion)
        listView2 = view.findViewById(R.id.lv_middleRegion)


        // API 호출
        fetchJobData()

        return view
    }

    private fun fetchJobData() {
        val url =
            "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=1"
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
            // 지역 대분류 리스트 (시/도)
            val superRegionList = mutableListOf<String>()
            // 지역 중분류 리스트 (시/군/구)
            val middleRegionList = mutableListOf<String>()
            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()
            xpp.setInput(inputStream, null)

            var eventType = xpp.eventType
            var regionCd: String? = null // 지역 분류 코드
            var regionNm: String? = null // 지역 이름
            var superCd: String? = null // 지역 대분류 코드


            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (xpp.name) {
                            "regionCd" -> regionCd = xpp.nextText()
                            "regionNm" -> regionNm = xpp.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (xpp.name == "cmcdRegion") {
                            regionCd?.let { c ->
                                regionNm?.let { n ->
                                    regionList.add(
                                        Region(
                                            c, n
                                        )
                                    )
                                }
                            }
                            regionCd = null
                            regionNm = null

                        }
                    }
                }
                eventType = xpp.next()
            }

            return regionList
        }

        override fun onPostExecute(result: List<Region>) {
            regionList = result
            showJobList()
        }
    }

    private fun showJobList() {
        val adapter = CustomAdapter(this@WantedWorkNetFragment, jobList)
        jobListView.adapter = adapter

        jobListView.setOnItemClickListener { _, _, position, _ ->
            val job = jobList[position]
            val intent = JobDetailActivity.newIntent(requireContext(), job)
            startActivity(intent)
        }
    }
}

class CustomAdapter(private val fragment: Fragment, private val jobList: List<Job>) :
    ArrayAdapter<Job>(fragment.requireActivity(), R.layout.job_item, jobList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.job_item, parent, false)

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

class JobDetailActivity : AppCompatActivity() {

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

        fun newIntent(context: Context, job: Job): Intent {
            return Intent(context, JobDetailActivity::class.java).apply {
                putExtra(JOB_EXTRA, job)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_detail)

        // 기본 툴바 숨기기
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.hide()
        }

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

data class Job(
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
    val jobsCd: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(company)
        parcel.writeString(title)
        parcel.writeString(salTpNm)
        parcel.writeString(sal)
        parcel.writeString(region)
        parcel.writeString(holidayTpNm)
        parcel.writeString(minEdubg)
        parcel.writeString(career)
        parcel.writeString(closeDt)
        parcel.writeString(wantedMobileInfoUrl)
        parcel.writeString(jobsCd)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Job> {
        override fun createFromParcel(parcel: Parcel): Job {
            return Job(parcel)
        }

        override fun newArray(size: Int): Array<Job?> {
            return arrayOfNulls(size)
        }
    }
}
