package com.example.a23_hf069

import android.os.Bundle
import android.util.Xml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import okhttp3.OkHttpClient
import org.json.JSONObject
import okhttp3.*
import org.json.JSONException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.StringReader
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class RegionFragment1 : Fragment() {

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

        // 지역 정보 리스트에 담기
        fetchSRegionNames()
        //fetchMRegionNames()

        // ListView를 찾고, 어댑터 설정
        val listView1: ListView = rootView.findViewById(R.id.lv_superRegion)
        val listView2: ListView = rootView.findViewById(R.id.lv_middleRegion)
        val adapter1 = ArrayAdapter(requireContext(), R.layout.filtering_list_item, superRegionList)
        val adapter2 = ArrayAdapter(requireContext(), R.layout.filtering_list_item, middleRegionList)
        listView1.adapter = adapter1
        listView2.adapter = adapter2


        return rootView
    }

    private fun fetchSRegionNames() {
        val params = listOf("returnType" to "XML", "target" to "CMCD", "authKey" to apiKey, "dtlGb" to "1")

        val request = Request.Builder()
            .url("$regionUrl?${params.joinToString("&")}")
            .get()
            .header("Authorization", "Bearer $apiKey")
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    println("API 응답 데이터: $responseData")
                    try {
                        val superRegionList = parseXmlResponse(responseData)
                        // superRegionList에 모든 근로청소재지 소재지(시/도)명 정보가 담김
                        // TODO: 원하는 작업 수행
                        // UI 업데이트를 위해 사용할 경우, UI 스레드에서 처리
                        requireActivity().runOnUiThread {
                            updateUIWithRegionData(superRegionList)
                        }
                    } catch (e: XmlPullParserException) {
                        println(e.message)
                    }
                } else {
                    println("서버 응답 실패: ${response.code}")
                }
            }
        })
    }

    private fun parseXmlResponse(xmlData: String): List<String> {
        superRegionList = mutableListOf<String>()
        try {
            val parser = Xml.newPullParser()
            parser.setInput(StringReader(xmlData))

            var eventType = parser.eventType
            var currentRegionName: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val tagName = parser.name

                        if (tagName == "regionNm") {
                            currentRegionName = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        val tagName = parser.name

                        if (tagName == "regionNm" && currentRegionName != null) {
                            superRegionList.add(currentRegionName)
                            currentRegionName = null
                        }
                    }
                }

                eventType = parser.next()
            }
        } catch (e: XmlPullParserException) {
            println("${e.message}")
        } catch (e: IOException) {
            println("${e.message}")
        }

        return superRegionList
    }


    private fun updateUIWithRegionData(superRegionList: List<String>) {
        val listView1: ListView = requireView().findViewById(R.id.lv_superRegion)
        val adapter1 = ArrayAdapter(requireContext(), R.layout.filtering_list_item, superRegionList)
        listView1.adapter = adapter1

        // 시/도 버튼 클릭 시 이벤트 처리
        listView1.setOnItemClickListener { _, _, position, _ ->
            val item = superRegionList[position]
            // TODO: 버튼 클릭 시 해당 지역 코드를 사용하여 채용공고 필터링 로직 추가
        }
    }

//    private fun fetchMRegionNames() {
//        val workplaceUrl = "http://openapi.work.go.kr/opi/opi/opia/dhsOpenEmpInfoAPI.do"
//        val params = listOf("returnType" to "JSON", "authKey" to apiKey, "callTp" to "L")
//
//        FuelManager.instance.get(workplaceUrl, params)
//            .authentication().bearer(apiKey)
//            .responseString { _, _, result ->
//                when (result) {
//                    is Result.Success -> {
//                        val responseData = result.get()
//                        val jsonObject = JSONObject(responseData)
//                        val workplaces = jsonObject.getJSONArray("dhsOpenEmpInfo")
//                        middleRegionList = mutableListOf<String>()
//
//                        for (i in 0 until workplaces.length()) {
//                            val workplace = workplaces.getJSONObject(i)
//                            //val workplaceName = workplace.getString("wkpl_nm")
//                            val jurisdictionLv2 = workplace.getString("wkpl_juris_lv2_nm")
//                            middleRegionList.add("$jurisdictionLv2")
//                        }
//
//                        // workplaceList에 모든 근로청소재지 소재지(시/군/구)명 정보가 담김
//                        // TODO: 원하는 작업 수행
//                    }
//                    is Result.Failure -> {
//                        println("근로청소재지 소재지(시/군/구)명 정보를 가져오는데 실패하였습니다.")
//                    }
//                }
//            }
//    }


}