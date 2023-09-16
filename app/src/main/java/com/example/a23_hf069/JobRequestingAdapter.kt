package com.example.a23_hf069

import JobPosting
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobRequestingAdapter : RecyclerView.Adapter<JobRequestingAdapter.JobViewHolder>() {
    private var jobPostings: List<JobPosting> = emptyList()

    fun setData(data: List<JobPosting>) {
        jobPostings = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_requesting_item, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val job = jobPostings[position]
        // ViewHolder의 뷰에 데이터를 바인딩하는 코드를 추가하세요.
    }

    override fun getItemCount(): Int {
        return jobPostings.size
    }

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ViewHolder의 뷰 요소를 초기화하는 코드를 추가하세요.
    }
}