package com.example.a23_hf069

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.a23_hf069.databinding.FragmentJobSelectionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import org.xml.sax.InputSource
import java.io.IOException
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class JobSelectionFragment : Fragment() {

    private lateinit var binding: FragmentJobSelectionBinding
    private lateinit var jobAdapter: ArrayAdapter<String>
    private lateinit var jobList: MutableList<String>

    private val baseUrl =
        "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=2"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobSelectionBinding.inflate(inflater, container, false)
        val rootView = binding.root

        // View 초기화
        val searchEditText = binding.tvSelectJob
        val jobListView = binding.lvJobs
        val selectedJobTextView = binding.tvSelectedJob
        val jobSelectButton = binding.btnJobSelectComplete

        // ListView 초기화
        jobList = mutableListOf()
        jobAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, jobList)
        jobListView.adapter = jobAdapter

        // 직업 목록 API 호출 및 결과 처리
        fetchJobList()

        // EditText에서 검색어 입력 시 이벤트 처리
        searchEditText.setOnEditorActionListener { _, _, _ ->
            val searchText = searchEditText.text.toString()
            filterJobList(searchText)
            true
        }

        // ListView에서 아이템 선택 시 이벤트 처리
        jobListView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedJob = jobAdapter.getItem(position)
                selectedJobTextView.text = selectedJob
            }

        // drawableRight(검색 아이콘) 클릭 시 검색 이벤트 처리
        searchEditText.setOnTouchListener { _, event ->
            val drawableRight = 2 // Index of the drawableRight icon
            if (event.action == MotionEvent.ACTION_UP &&
                event.rawX >= (searchEditText.right - searchEditText.compoundDrawables[drawableRight].bounds.width())
            ) {
                val searchText = searchEditText.text.toString()
                filterJobList(searchText)
                true
            } else {
                false
            }
        }

        // btn_job_select_complete 버튼 클릭 시 이벤트 처리
        jobSelectButton.setOnClickListener {
            val selectedJob = selectedJobTextView.text.toString()

            // 선택된 직종 정보를 WantedFilteringFragment로 전달
            val wantedFilteringFragment = WantedFilteringFragment()
            val args = Bundle()
            args.putString("selectedJob", selectedJob)
            wantedFilteringFragment.arguments = args

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fl_container, wantedFilteringFragment)
                .addToBackStack(null)
                .commit()
        }

        return rootView
    }

    private fun fetchJobList() {
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
                    parseJobList(xmlString)
                } else {
                    showErrorToast()
                }
            }
        })
    }

    private fun parseJobList(xmlString: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Default) {
                val xmlDoc = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(InputSource(StringReader(xmlString)))
                xmlDoc.documentElement.normalize()

                val jobNodeList = xmlDoc.getElementsByTagName("jobsNm")
                for (i in 0 until jobNodeList.length) {
                    val jobName = jobNodeList.item(i).textContent
                    jobList.add(jobName)
                }
            }

            jobAdapter.notifyDataSetChanged()
        }
    }

    private fun filterJobList(searchText: String) {
        jobAdapter.filter.filter(searchText)
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), "Failed to fetch job list.", Toast.LENGTH_SHORT).show()
    }
}
