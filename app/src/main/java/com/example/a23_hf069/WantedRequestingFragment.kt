package com.example.a23_hf069

import JobPosting
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WantedRequestingFragment : Fragment() {
    private lateinit var binding: WantedRequestingFragment
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobRequestingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wanted_requesting, container, false)

        // RecyclerView 초기화
        recyclerView = rootView.findViewById(R.id.recyclerviewJobRequesting)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = JobRequestingAdapter()
        recyclerView.adapter = adapter

        // Retrofit을 사용하여 Job 데이터 가져오기
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInterface.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(RetrofitInterface::class.java)
        service.getJobPostings().enqueue(object : Callback<List<JobPosting>> {
            override fun onResponse(
                call: Call<List<JobPosting>>,
                response: Response<List<JobPosting>>
            ) {
                if (response.isSuccessful) {
                    val jobPostings = response.body()
                    jobPostings?.let {
                        adapter.setJobPostings(it)
                    }
                } else {
                    // API 요청이 실패한 경우 처리
                }
            }

            override fun onFailure(call: Call<List<JobPosting>>, t: Throwable) {
                // API 요청이 실패한 경우 처리
            }
        })

        return rootView
    }
}