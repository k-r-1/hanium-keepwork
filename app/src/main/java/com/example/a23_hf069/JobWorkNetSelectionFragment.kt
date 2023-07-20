package com.example.a23_hf069

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import com.example.a23_hf069.databinding.FragmentJobWorkNetSelectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.xml.sax.InputSource
import java.io.IOException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class JobWorkNetSelectionFragment : Fragment() {

    private lateinit var binding: FragmentJobWorkNetSelectionBinding
    private lateinit var jobAdapter: ArrayAdapter<String>
    private lateinit var jobList: MutableList<String> // 직업 목록을 담을 리스트
    private lateinit var selectedJobList: MutableList<String> // 여러 개의 직종을 저장할 리스트

    // 직업 목록을 불러오는 API의 기본 URL을 설정
    private val baseUrl =
        "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=2"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobWorkNetSelectionBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // View 초기화
        val searchEditText = binding.tvSelectJob
        val jobListView = binding.lvJobs
        val jobSelectButton = binding.btnJobSelectComplete
        selectedJobList = mutableListOf()

        // ListView 초기화
        jobList = mutableListOf()
        jobAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, jobList)
        jobListView.adapter = jobAdapter

        fetchJobList() // 직업 목록 API 호출 및 결과 처리 함수를 호출

        // EditText에서 검색어 입력 시 이벤트 처리
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val searchText = searchEditText.text.toString() // EditText에 입력된 텍스트를 가져와서 문자열로 변환
            filterJobList(searchText) // 가져온 검색어를 사용하여 직업 목록을 필터링하고, 결과를 ListView에 반영
            true // "완료" 버튼을 눌렀을 때만 필터링이 수행되고, 다른 동작으로 전환되지 않도록 함
        }

        // ListView에서 아이템 선택 시 이벤트 처리
        jobListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedJob = jobAdapter.getItem(position) // 클릭된 아이템의 위치(position)을 기반으로 해당 아이템의 직업명을 가져옴
                if (selectedJob != null) {
                    // 선택된 직종이 리스트에 이미 포함되어 있지 않은 경우에만 추가
                    if (!selectedJobList.contains(selectedJob)) {
                        selectedJobList.add(selectedJob)
                        updateSelectedJobTextView() // 선택된 직종 목록을 보여주는 TextView를 업데이트
                    }
                }
            }

        // drawableRight(검색 아이콘) 클릭 시 검색 이벤트 처리
        searchEditText.setOnTouchListener { _, event ->
            val drawableRight = 2 // 검색 아이콘의 위치를 나타내는 인덱스
            if (event.action == MotionEvent.ACTION_UP &&
                event.rawX >= (searchEditText.right - searchEditText.compoundDrawables[drawableRight].bounds.width())
            ) {
                val searchText = searchEditText.text.toString()
                filterJobList(searchText) // 가져온 검색어를 이용하여 직업 목록을 필터링하고 업데이트
                true // 이벤트 처리가 끝났음을
            } else {
                false// 이벤트 처리가 끝나지 않았음
            }
        }

        // btn_job_select_complete 버튼 클릭 시 이벤트 처리
        jobSelectButton.setOnClickListener {
            // 선택된 직종들을 쉼표와 줄바꿈 문자로 구분하여 문자열로 만듦
            val selectedJobs = selectedJobList.joinToString(", \n")

            // 선택된 직종 정보를 WantedFilteringFragment로 전달
            val wantedFilteringFragment = WantedFilteringFragment()

            // 선택된 직종 정보를 담을 Bundle을 생성하고, "selectedJob"이라는 키로 문자열을 설정하여 Bundle에 추가
            val args = Bundle()
            args.putString("selectedJob", selectedJobs)
            wantedFilteringFragment.arguments = args // WantedFilteringFragment 객체에 args라는 Bundle을 설정

            // 현재 프래그먼트가 속한 액티비티의 supportFragmentManager를 이용하여 프래그먼트 트랜잭션을 시작
            // 프래그먼트 트랜잭션은 프래그먼트의 추가, 삭제, 대체 등을 수행
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedFilteringFragment) // WantedFilteringFragment로 프래그먼트를 대체(Replace)
                .addToBackStack(null) // 백스택에 이 프래그먼트를 추가
                .commit() // 트랜잭션을 커밋하여 변경사항을 적용
        }

        return rootView
    }

    // TextView 업데이트 함수 추가
    private fun updateSelectedJobTextView() {
        val selectedJobs = selectedJobList.joinToString(", \n") // selectedJobList의 모든 항목을 하나의 문자열로 합침
        binding.tvSelectedJob.text = selectedJobs // selectedJobs에서 만들어진 문자열을 tvSelectedJob의 텍스트로 설정하여 선택된 직종들을 화면에 표시
    }

    // 직업 목록을 서버로부터 가져오는 함수
    private fun fetchJobList() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(baseUrl)
            .build()

        // 비동기적으로 API 요청을 수행하고 결과를 처리하는 콜백 등록
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // 네트워크 요청 실패 시 에러 토스트 메시지를 보여줌
                showErrorToast()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    // 응답이 성공적으로 도착한 경우, XML 형태의 데이터를 문자열로 변환하여 직업 목록 파싱
                    val xmlString = response.body?.string()
                    parseJobList(xmlString)
                } else {
                    // 응답이 실패한 경우 에러 토스트 메시지를 보여줌
                    showErrorToast()
                }
            }
        })
    }

    // XML 형태의 데이터를 파싱하여 직업 목록을 갱신하는 함수
    private fun parseJobList(xmlString: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            // 백그라운드 스레드에서 XML 파싱
            withContext(Dispatchers.Default) {
                // XML 파서를 이용하여 문자열로 받은 XML 데이터를 파싱
                val xmlDoc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(InputSource(StringReader(xmlString)))
                xmlDoc.documentElement.normalize()

                // XML에서 "jobsNm" 태그를 찾아서 직업 이름을 추출하여 직업 목록인 jobList에 추가
                val jobNodeList = xmlDoc.getElementsByTagName("jobsNm")
                for (i in 0 until jobNodeList.length) {
                    val jobName = jobNodeList.item(i).textContent
                    jobList.add(jobName)
                }
            }

            // 파싱 결과를 어댑터에 알려서 리스트뷰를 갱신
            jobAdapter.notifyDataSetChanged()
        }
    }

    // 직업 목록을 필터링하는 함수
    private fun filterJobList(searchText: String) {
        // ArrayAdapter에서 제공하는 filter를 이용하여 입력된 검색어를 포함하는 직종만 표시
        jobAdapter.filter.filter(searchText)
    }

    // API 요청 실패 시 에러 메시지를 보여주는 함수
    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Failed to fetch job list.", Toast.LENGTH_SHORT).show()
    }
}
