package com.example.a23_hf069

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.StringReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class WantedTest : AppCompatActivity() {

    // 워크넷 API 키
    private val apiKey = "WNLJYZLM2VZXTT2TZA9XR2VR1HK"

    // API 호출 주소
    private val apiAddress = "http://openapi.work.go.kr/opi/opi/opia/wantedApi.do"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wanted_test)

        // 채용공고 불러오기
        val keyword = "안드로이드" // 검색 키워드 (필요한 경우 수정 가능)
        GetJobPostingsTask().execute(keyword)
    }

    inner class GetJobPostingsTask : AsyncTask<String, Void, List<JobPosting>>() {
        override fun doInBackground(vararg params: String): List<JobPosting> {
            val keyword = params[0]
            val query = "?apiKey=$apiKey&keyword=$keyword"

            try {
                val result = downloadContents("$apiAddress$query")
                return parseJobPostings(result)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return emptyList()
        }

        override fun onPostExecute(jobPostings: List<JobPosting>) {
            // 채용공고 리스트를 사용하여 화면에 표시하거나 필요한 처리를 수행합니다.
            for (jobPosting in jobPostings) {
                Log.d("JobPosting", "Company: ${jobPosting.company}, Title: ${jobPosting.title}")
            }
        }
    }

    data class JobPosting(
        var company: String,
        var title: String,
        var salTpNm: String,
        var sal: String,
        var region: String,
        var minEdubg: String,
        var career: String,
        var closeDt: String,
        var wantedMobileInfoUrl: String,
        var basicAddr: String
    )

    private fun downloadContents(address: String): String {
        val conn: HttpURLConnection?
        var stream: InputStream? = null
        var result = ""

        try {
            val url = URL(address)
            conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = 6000
            conn.connectTimeout = 6000
            conn.requestMethod = "GET"
            conn.doInput = true

            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                stream = conn.inputStream
                result = readStreamToString(stream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            stream?.close()
        }

        return result
    }

    private fun readStreamToString(stream: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(stream))
        val stringBuilder = StringBuilder()
        var line: String?

        try {
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bufferedReader.close()
        }

        return stringBuilder.toString()
    }

    private fun parseJobPostings(xml: String): List<JobPosting> {
        val jobPostings = mutableListOf<JobPosting>()
        var parser: XmlPullParser? = null

        try {
            val factory = XmlPullParserFactory.newInstance()
            parser = factory.newPullParser()
            parser.setInput(StringReader(xml))

            var eventType = parser.eventType
            var jobPosting: JobPosting? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "wanted" -> jobPosting = JobPosting("", "", "", "", "", "", "", "", "", "")
                            "company" -> jobPosting?.company = parser.nextText()
                            "title" -> jobPosting?.title = parser.nextText()
                            "salTpNm" -> jobPosting?.salTpNm = parser.nextText()
                            "sal" -> jobPosting?.sal = parser.nextText()
                            "region" -> jobPosting?.region = parser.nextText()
                            "minEdubg" -> jobPosting?.minEdubg = parser.nextText()
                            "career" -> jobPosting?.career = parser.nextText()
                            "closeDt" -> jobPosting?.closeDt = parser.nextText()
                            "wantedMobileInfoUrl" -> jobPosting?.wantedMobileInfoUrl = parser.nextText()
                            "basicAddr" -> jobPosting?.basicAddr = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "wanted" && jobPosting != null) {
                            jobPostings.add(jobPosting)
                            jobPosting = null
                        }
                    }
                }

                eventType = parser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return jobPostings
    }
}
