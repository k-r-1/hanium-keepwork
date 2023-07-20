package com.example.a23_hf069

import android.os.Bundle
import android.util.Xml
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a23_hf069.databinding.FragmentRegionSelectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.io.StringReader
import org.xmlpull.v1.XmlPullParser


class RegionSelectionFragment : Fragment() {

    private lateinit var binding: FragmentRegionSelectionBinding
    private lateinit var regionAdapter1: ArrayAdapter<String>
    private lateinit var regionAdapter2: ArrayAdapter<String>
    private val regionList1: MutableList<String> = mutableListOf()
    private val regionList2: MutableList<String> = mutableListOf()
    private var selectedOneDepthRegion: String? = null// 선택한 oneDepth 지역명을 저장할 변수
    private lateinit var regionListView1: ListView
    private lateinit var regionListView2: ListView
    private val baseUrl =
        "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegionSelectionBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // View 초기화
        val searchEditText = binding.tvSelectRegion
        regionListView1 = binding.lvSuperRegion
        regionListView2 = binding.lvMiddleRegion
        val selectedRegionTextView = binding.tvSelectedRegion
        val regionSelcetButton = binding.btnRegionSelectComplete

        // ListView 초기화
        regionAdapter1 = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, regionList1)
        regionAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, regionList2)
        regionListView1.adapter = regionAdapter1
        regionListView2.adapter = regionAdapter2

        // 지역 목록 API 호출하여 regionList1,2에 결과 담기
        fetchRegionList()

        // EditText에서 검색어 입력 시 이벤트 처리
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val searchText = searchEditText.text.toString()
            filterregionList(searchText)
            true
        }

        // regionListView1에서 아이템 선택 시 이벤트 처리
        regionListView1.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                selectedOneDepthRegion = regionAdapter1.getItem(position) ?: ""
                updateRegionListView2()
            }


        // drawableRight(검색 아이콘) 클릭 시 검색 이벤트 처리
        searchEditText.setOnTouchListener { _, event ->
            val drawableRight = 2 // Index of the drawableRight icon
            if (event.action == MotionEvent.ACTION_UP &&
                event.rawX >= (searchEditText.right - searchEditText.compoundDrawables[drawableRight].bounds.width())
            ) {
                val searchText = searchEditText.text.toString()
                filterregionList(searchText)
                true
            } else {
                false
            }
        }

        // btn_region_select_complete 버튼 클릭 시 이벤트 처리
        regionSelcetButton.setOnClickListener {
            val selectedRegion = selectedRegionTextView.text.toString()

            // 선택된 직종 정보를 WantedFilteringFragment로 전달
            val wantedFilteringFragment = WantedFilteringFragment()
            val args = Bundle()
            args.putString("selectedRegion", selectedRegion)
            wantedFilteringFragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedFilteringFragment)
                .addToBackStack(null)
                .commit()
        }

        return rootView
    }

    private fun fetchRegionList() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(baseUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                showErrorToast()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val xmlString = response.body?.string()
                    parseRegionList(xmlString)
                } else {
                    showErrorToast()
                }
            }
        })
    }

    private fun parseRegionList(xmlString: String?) {
        // 기존 데이터를 초기화합니다.

        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                val xmlPullParser: XmlPullParser = Xml.newPullParser()
                xmlPullParser.setInput(StringReader(xmlString))

                var eventType = xmlPullParser.eventType
                var isOneDepth = false
                var isTwoDepth = false
                var isregionNm = false
                var oneDepthRegionName = ""
                var twoDepthRegionName = ""

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            when (xmlPullParser.name) {
                                // 추가: oneDepth를 시작할 때 twoDepth를 종료합니다.
                                "oneDepth" -> {
                                    isOneDepth = true
                                    isTwoDepth = false

                                }
                                // 추가: twoDepth를 시작할 때 oneDepth를 종료합니다.
                                "twoDepth" -> {
                                    isOneDepth = false
                                    isTwoDepth = true
                                }
                                "regionNm" -> {
                                    isregionNm = true
                                }
                            }
                        }
                        XmlPullParser.TEXT -> {
                            if (isOneDepth && isregionNm && xmlPullParser.text.trim().isNotEmpty()) {
                                oneDepthRegionName = xmlPullParser.text.trim()
                            }
                            if (isTwoDepth && isregionNm && xmlPullParser.text.trim().isNotEmpty()) {
                                twoDepthRegionName = xmlPullParser.text.trim()
                            }
                        }
                        XmlPullParser.END_TAG -> {
                            when (xmlPullParser.name) {
                                "oneDepth" -> {
                                    if (oneDepthRegionName.isNotEmpty()) {
                                        regionList1.add(oneDepthRegionName)
                                        oneDepthRegionName = ""
                                        isOneDepth=false
                                    }
                                }
                                "twoDepth" -> {
                                    if (twoDepthRegionName.isNotEmpty()) {
                                        regionList2.add(twoDepthRegionName)
                                        twoDepthRegionName = ""
                                        isTwoDepth=false
                                    }
                                }
                                "regionNm" -> {
                                    isregionNm=false
                                }
                            }
                        }
                    }

                    eventType = xmlPullParser.next()
                }
            }

            regionAdapter1.notifyDataSetChanged()
        }
    }

    private fun updateRegionListView2() {
        val selectedRegion = selectedOneDepthRegion

        // regionListView1에서 아무것도 선택되지 않았을 때, regionAdapter2를 비우고 리턴합니다.
        if (selectedRegion == null) {
            regionAdapter2.clear()
            regionAdapter2.notifyDataSetChanged()
            return
        }

        // 선택한 oneDepth 지역명에 해당하는 twoDepth 지역명들을 필터링하여 가져오기
        val filteredTwoDepthRegions = regionList2.filter { region ->
            val regionWords = region.split(" ") // 띄어쓰기 등으로 문자열 분리
            val selectedWords = selectedRegion.split(" ")
            regionWords.firstOrNull() == selectedWords.firstOrNull() // 앞 단어 기준으로 포함 (ex) 경기 광주 -> 광주가 아닌 경기가 기준
        }.distinct()

        // regionAdapter2를 새로운 리스트로 갱신합니다. ****filter 한 뒤에 기존 apdater를 변경하면 regionList2에 filter 내용이 적용되어 이상해짐****
        regionAdapter2 = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, filteredTwoDepthRegions)
        regionListView2.adapter = regionAdapter2
    }

    private fun filterregionList(searchText: String) {
        regionAdapter1.filter.filter(searchText)

    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Failed to fetch region list.", Toast.LENGTH_SHORT).show()
    }
}