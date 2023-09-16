package com.example.a23_hf069

import JobPosting
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WantedRequestingFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JobRequestingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_wanted_requesting, container, false)

        recyclerView = rootView.findViewById(R.id.recyclerviewJobRequesting)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = JobRequestingAdapter()

        // Retrofit을 사용하여 Job 데이터를 가져옵니다.
        val retrofit = Retrofit.Builder()
            .baseUrl(RetrofitInterface.API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitInterface = retrofit.create(RetrofitInterface::class.java)
        val call = retrofitInterface.getJobPostings()

        call.enqueue(object : Callback<List<JobPosting>> {
            override fun onResponse(
                call: Call<List<JobPosting>>,
                response: Response<List<JobPosting>>
            ) {
                if (response.isSuccessful) {
                    val jobPostings = response.body()
                    if (jobPostings != null) {
                        adapter.setData(jobPostings)
                        recyclerView.adapter = adapter
                    }
                }
            }

            override fun onFailure(call: Call<List<JobPosting>>, t: Throwable) {
                // 실패 처리 로직을 여기에 추가하세요.
            }
        })

        return rootView
    }
}
