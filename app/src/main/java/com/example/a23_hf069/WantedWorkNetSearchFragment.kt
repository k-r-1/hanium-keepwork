package com.example.a23_hf069

import android.os.AsyncTask
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class WantedWorkNetSearchFragment : Fragment() {
    private lateinit var searchContent: EditText
    private lateinit var searchButton: Button
    private lateinit var searchListView: ListView
    private lateinit var jobList: List<Job>
    private var currentPage = 1
    private lateinit var prevButton: Button
    private lateinit var nextButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_wanted_work_net_search, container, false)

        searchContent = rootView.findViewById(R.id.searchContent)
        searchListView = rootView.findViewById(R.id.searchListView)
        prevButton = rootView.findViewById(R.id.prevButton)
        nextButton = rootView.findViewById(R.id.nextButton)

        searchContent.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                fetchJobDataWithSearch()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        val closeButton = rootView.findViewById<ImageButton>(R.id.backButton)
        closeButton.setOnClickListener {
            requireActivity().onBackPressed()
        }


        prevButton.visibility = View.GONE
        nextButton.visibility = View.GONE

        // 이전 페이지 버튼 클릭 이벤트 처리
        prevButton.setOnClickListener {
            if (currentPage > 1) {
                currentPage -= 1
                fetchJobDataWithSearch()
            }
        }

        // 다음 페이지 버튼 클릭 이벤트 처리
        nextButton.setOnClickListener {
            currentPage += 1
            fetchJobDataWithSearch()
        }


        return rootView
    }

    private fun fetchJobDataWithSearch() {
        val searchKeyword = searchContent.text.toString().trim()

        if (searchKeyword.isNotEmpty()) {
            val encodedKeyword = URLEncoder.encode(searchKeyword, "UTF-8")
            val url = "http://openapi.work.go.kr/opi/opi/opia/wantedApi.do?authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&callTp=L&returnType=XML&startPage=$currentPage&display=10&keyword=$encodedKeyword"
            FetchJobData().execute(url)
        } else {
            // 검색어가 비어있을 때 처리할 내용
        }
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
        val adapter = CustomAdapter(this, jobList)
        searchListView.adapter = adapter

        if (jobList.isNotEmpty()) {
            prevButton.visibility = View.VISIBLE
            nextButton.visibility = View.VISIBLE
        } else {
            prevButton.visibility = View.GONE
            nextButton.visibility = View.GONE
        }

        searchListView.setOnItemClickListener { _, _, position, _ ->
            val job = jobList[position]
            val intent = JobDetailActivity.newIntent(requireContext(), job)
            startActivity(intent)
        }
    }

}
