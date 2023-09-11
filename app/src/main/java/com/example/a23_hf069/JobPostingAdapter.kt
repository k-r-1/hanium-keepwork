package com.example.a23_hf069

import JobPosting
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobPostingAdapter(private val jobPostingList: List<JobPosting>) :
    RecyclerView.Adapter<JobPostingAdapter.JobPostingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPostingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobmanagement_item, parent, false)
        return JobPostingViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobPostingViewHolder, position: Int) {
        val jobPosting = jobPostingList[position]

        // jobmanagement_item.xml 뷰에 데이터를 바인딩합니다.
        holder.jobTitleTextView.text = jobPosting.job_title
        holder.startDateTextView.text = jobPosting.job_deadline // 날짜 형식을 맞추어야 할 수 있습니다.
        holder.endDateTextView.text = "마감일: ${jobPosting.job_deadline}"

        // 버튼 클릭 이벤트를 처리합니다.
        holder.modifyButton.setOnClickListener {
            // 수정 처리를 여기에 추가하세요.
        }

        holder.repostButton.setOnClickListener {
            // 재등록 처리를 여기에 추가하세요.
        }

        holder.endButton.setOnClickListener {
            // 마감 처리를 여기에 추가하세요.
        }
    }

    override fun getItemCount(): Int {
        return jobPostingList.size
    }

    inner class JobPostingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jobTitleTextView: TextView = itemView.findViewById(R.id.tvJobManagementTitle)
        val startDateTextView: TextView = itemView.findViewById(R.id.tvJobManagentStartDate)
        val endDateTextView: TextView = itemView.findViewById(R.id.tvJobManagementEndDate)
        val modifyButton: Button = itemView.findViewById(R.id.btnJobManagement_modify)
        val repostButton: Button = itemView.findViewById(R.id.btnJobManagement_repost)
        val endButton: Button = itemView.findViewById(R.id.btnJobManagement_end)
    }
}
