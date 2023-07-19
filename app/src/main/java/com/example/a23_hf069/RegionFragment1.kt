package com.example.a23_hf069

import android.os.Bundle
import android.util.Xml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.StringReader
import okhttp3.Request
import java.net.URL

class RegionFragment1 : Fragment() {
    private lateinit var oneDepthTextView : TextView
    private lateinit var twoDepthTextView : TextView

    // API 인증키
    val apiKey = "WNLJYZLM2VZXTT2TZA9XR2VR1HK"
    val regionUrl = "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=1"

    // 지역 대분류 리스트 (시/도)
    private var superRegionList = mutableListOf<String>()
    // 지역 중분류 리스트 (시/군/구)
    private var middleRegionList = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_region, container, false)
        // UI 업데이트 코드
        oneDepthTextView = rootView.findViewById<TextView>(R.id.oneDepthTextView)
        twoDepthTextView = rootView.findViewById<TextView>(R.id.twoDepthTextView)
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apitest()
        // 지역 정보 리스트에 담기
        //fetchSRegionNames()
        //fetchMRegionNames()

    }

    data class TwoDepth(
        val regionCd: String,
        val regionNm: String,
        val superCd: String
    )

    data class OneDepth(
        var regionCd: String,
        var regionNm: String,
        var twoDepth: List<TwoDepth>? = null
    )

    data class CmcdRegion(
        val total: Int,
        val codeName: String,
        val oneDepth: List<OneDepth>
    )

    fun apitest() {
        GlobalScope.launch(Dispatchers.IO) {
            val url =
                "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=1"

            val client = OkHttpClient()

            val request = Request.Builder()
                .url(url)
                .build()

            try {
                val response: Response = client.newCall(request).execute()

                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                val xmlData = response.body?.string()
                if (xmlData != null) {
                    val cmcdRegion = parseCmcdRegionFromXml(xmlData)

                    // 결과를 메인 스레드로 전달하여 UI 업데이트
                    launch(Dispatchers.Main) {

                        val oneDepthNames = cmcdRegion.oneDepth.map { it.regionNm }
                        oneDepthTextView.text = "\n${oneDepthNames.joinToString("\n")}"

                        val twoDepthNames = cmcdRegion.oneDepth.flatMap { it.twoDepth?.map { twoDepth -> twoDepth.regionNm } ?: emptyList() }
                        twoDepthTextView.text = "\n${twoDepthNames.joinToString("\n")}"
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun parseCmcdRegionFromXml(xmlData: String): CmcdRegion {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(StringReader(xmlData))

        var total = 0
        var codeName = ""
        val oneDepthList = mutableListOf<OneDepth>()
        var currentOneDepth: OneDepth? = null
        var currentTwoDepthList: MutableList<TwoDepth>? = null

        try {
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (parser.name) {
                            "total" -> total = parser.nextText().toInt()
                            "codeName" -> codeName = parser.nextText()
                            "oneDepth" -> currentOneDepth = parseOneDepth(parser)
                            "twoDepth" -> currentTwoDepthList = mutableListOf()
                            "regionCd" -> {
                                var regionCd = parser.nextText()
                                currentOneDepth?.regionCd = regionCd
                            }
                            "regionNm" -> {
                                var regionNm = parser.nextText()
                                currentOneDepth?.regionNm = regionNm
                            }
                            "superCd" -> {
                                var superCd = parser.nextText()
                                currentTwoDepthList?.add(
                                    TwoDepth(
                                        regionCd = currentOneDepth?.regionCd ?: "",
                                        regionNm = currentOneDepth?.regionNm ?: "",
                                        superCd = superCd
                                    )
                                )
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        when (parser.name) {
                            "oneDepth" -> {
                                currentOneDepth?.twoDepth = currentTwoDepthList
                                if (currentOneDepth != null) { // Check if currentOneDepth is not null
                                    oneDepthList.add(currentOneDepth)
                                }
                                currentOneDepth = null
                                currentTwoDepthList = null
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }

        return CmcdRegion(total, codeName, oneDepthList)
    }

    private fun parseOneDepth(parser: XmlPullParser): OneDepth {
        val regionCd = parser.getAttributeValue(null, "regionCd") ?: ""
        val regionNm = parser.getAttributeValue(null, "regionNm") ?: ""
        return OneDepth(regionCd, regionNm)
    }


//    private fun fetchSRegionNames() {
//        val params = listOf("returnType" to "XML", "target" to "CMCD", "authKey" to apiKey, "dtlGb" to "1")
//
//        val request = Request.Builder()
//            .url("$regionUrl?${params.joinToString("&")}")
//            .get()
//            .header("Authorization", "Bearer $apiKey")
//            .build()
//
//        val client = OkHttpClient()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                println(e.message)
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val responseData = response.body?.string()
//                if (response.isSuccessful && responseData != null) {
//                    println("API 응답 데이터: $responseData")
//                    try {
//                        val superRegionList = parseXmlResponse(responseData)
//                        // superRegionList에 모든 근로청소재지 소재지(시/도)명 정보가 담김
//                        // TODO: 원하는 작업 수행
//                        // UI 업데이트를 위해 사용할 경우, UI 스레드에서 처리
//                        requireActivity().runOnUiThread {
//                            updateUIWithRegionData(superRegionList)
//                        }
//                    } catch (e: XmlPullParserException) {
//                        println(e.message)
//                    }
//                } else {
//                    println("서버 응답 실패: ${response.code}")
//                }
//            }
//        })
//    }
//
//    private fun parseXmlResponse(xmlData: String): List<String> {
//        superRegionList = mutableListOf<String>()
//        try {
//            val parser = Xml.newPullParser()
//            parser.setInput(StringReader(xmlData))
//
//            var eventType = parser.eventType
//            var currentRegionName: String? = null
//
//            while (eventType != XmlPullParser.END_DOCUMENT) {
//                when (eventType) {
//                    XmlPullParser.START_TAG -> {
//                        val tagName = parser.name
//
//                        if (tagName == "regionNm") {
//                            currentRegionName = parser.nextText()
//                        }
//                    }
//                    XmlPullParser.END_TAG -> {
//                        val tagName = parser.name
//
//                        if (tagName == "regionNm" && currentRegionName != null) {
//                            superRegionList.add(currentRegionName)
//                            currentRegionName = null
//                        }
//                    }
//                }
//
//                eventType = parser.next()
//            }
//        } catch (e: XmlPullParserException) {
//            println("${e.message}")
//        } catch (e: IOException) {
//            println("${e.message}")
//        }
//
//        return superRegionList
//    }
//
//
//    private fun updateUIWithRegionData(superRegionList: List<String>) {
//        val listView1: ListView = requireView().findViewById(R.id.lv_superRegion)
//        val adapter1 = ArrayAdapter(requireContext(), R.layout.filtering_list_item, superRegionList)
//        listView1.adapter = adapter1
//
//        // 시/도 버튼 클릭 시 이벤트 처리
//        listView1.setOnItemClickListener { _, _, position, _ ->
//            val item = superRegionList[position]
//            // TODO: 버튼 클릭 시 해당 지역 코드를 사용하여 채용공고 필터링 로직 추가
//        }
//    }

//    private fun fetchMRegionNames() {
//    }


}