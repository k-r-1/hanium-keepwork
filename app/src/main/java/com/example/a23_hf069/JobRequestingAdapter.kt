package com.example.a23_hf069

import JobPosting
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobRequestingAdapter : RecyclerView.Adapter<JobRequestingAdapter.JobViewHolder>() {
    private var jobPostings: List<JobPosting> = emptyList()

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        // 다른 필드들을 추가하세요

        fun bind(jobPosting: JobPosting) {
            titleTextView.text = jobPosting.job_title
            // 다른 필드들을 설정하세요
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.job_requesting_item, parent, false)
        return JobViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val jobPosting = jobPostings[position]
        holder.bind(jobPosting)
    }

    override fun getItemCount(): Int {
        return jobPostings.size
    }

    fun setJobPostings(jobPostings: List<JobPosting>) {
        this.jobPostings = jobPostings
        notifyDataSetChanged()
    }
}