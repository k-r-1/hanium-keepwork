package com.example.a23_hf069

import android.os.Bundle
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
import org.json.JSONObject

class RegionFragment : Fragment() {
    // 지역 대분류
    private val superRegionList = listOf(
        "서울특별시",
        "부산광역시",
        "대구광역시",
        "인천광역시",
        "광주광역시",
        "대전광역시",
        "울산광역시",
        "세종특별자치시",
        "경기도",
        "강원도",
        "충청북도",
        "충청남도",
        "전라북도",
        "전라남도",
        "경상북도",
        "경상남도",
        "제주특별자치도"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Fragment의 레이아웃 인플레이션
        val rootView = inflater.inflate(R.layout.fragment_region, container, false)

        // ListView를 찾고, 어댑터 설정
        val listView: ListView = rootView.findViewById(R.id.lv_superRegion)
        val adapter = ArrayAdapter(requireContext(), R.layout.filtering_list_item, superRegionList)
        listView.adapter = adapter

        // 각 버튼 클릭 시 이벤트 처리
        listView.setOnItemClickListener { _, _, position, _ ->
            val item = superRegionList[position]
            val regionCode = item.substringAfter(": ").trim()
            // TODO: 버튼 클릭 시 해당 지역 코드를 사용하여 채용공고 필터링 로직 추가
            // 예시: Toast 메시지로 해당 지역 코드 출력
            Toast.makeText(requireContext(), "선택한 지역 코드: $regionCode", Toast.LENGTH_SHORT).show()
        }

        return rootView
    }


}