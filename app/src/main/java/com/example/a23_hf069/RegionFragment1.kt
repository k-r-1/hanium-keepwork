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

        // oneDepthTextView 클릭 이벤트 리스너 설정
        oneDepthTextView.setOnClickListener {
            val selectedRegion = oneDepthTextView.text.toString().trim()
            val twoDepthNames = middleRegionList.filter { it.startsWith(selectedRegion) }
            // twoDepthNames에 해당 지역 대분류에 해당하는 지역 중분류 목록이 담겨 있으므로 twoDepthTextView에 표시
            twoDepthTextView.text = "\n${twoDepthNames.joinToString("\n")}"
        }
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
                                middleRegionList.add(regionNm) // 지역 중분류를 middleRegionList에 추가
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

}