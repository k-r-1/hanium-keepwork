package com.example.a23_hf069

import JobPosting
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JobManagementPostFragment : Fragment() {

    // 사용자 ID를 저장할 변수
    private lateinit var userCompanyId: String

    companion object {
        private const val ARG_USER_COMPANY_ID = "userCompanyId"

        fun newInstance(userCompanyId: String): JobManagementPostFragment {
            val fragment = JobManagementPostFragment()
            val args = Bundle()
            args.putString(ARG_USER_COMPANY_ID, userCompanyId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobPostingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Arguments에서 userCompanyId 값을 읽어와서 변수에 할당합니다.
        arguments?.let {
            userCompanyId = it.getString(ARG_USER_COMPANY_ID, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_manangement_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recylcerviewJobPost)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = JobPostingAdapter(emptyList()) // 빈 목록으로 초기화
        recyclerView.adapter = adapter

        // 서버에서 작업 게시 데이터 가져오기
        fetchDataFromServer()
    }

    private fun fetchDataFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInterface.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitInterface::class.java)

        // userCompanyId를 사용하여 C_MemberModel을 가져옵니다.
        val companyCall = service.getCorporateData(userCompanyId)
        companyCall.enqueue(object : Callback<List<C_MemberModel>> {
            override fun onResponse(
                call: Call<List<C_MemberModel>>,
                response: Response<List<C_MemberModel>>
            ) {
                if (response.isSuccessful) {
                    val companyData = response.body() ?: emptyList()
                    // C_MemberModel 데이터를 가져왔습니다.

                    // 이제 작업 게시 데이터를 가져옵니다.
                    val jobPostingCall = service.getJobPostingData(userCompanyId)
                    jobPostingCall.enqueue(object : Callback<List<JobPosting>> {
                        override fun onResponse(
                            call: Call<List<JobPosting>>,
                            response: Response<List<JobPosting>>
                        ) {
                            if (response.isSuccessful) {
                                val jobPostings = response.body() ?: emptyList()

                                // C_MemberModel 데이터와 작업 게시 데이터를 adapter에 넘겨줍니다.
                                adapter = JobPostingAdapter(jobPostings, companyData)
                                recyclerView.adapter = adapter
                            } else {
                                // 오류 처리
                            }
                        }

                        override fun onFailure(call: Call<List<JobPosting>>, t: Throwable) {
                            // 네트워크 오류 처리
                        }
                    })
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<List<C_MemberModel>>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }
}
