package com.example.a23_hf069

import JobPosting
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class JobPostingAdapter(
    private val jobPostingList: List<JobPosting>,
    private val companyData: List<C_MemberModel> = emptyList() // 기본 값 설정
) : RecyclerView.Adapter<JobPostingAdapter.JobPostingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobPostingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobmanagement_item, parent, false)
        return JobPostingViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobPostingViewHolder, position: Int) {
        val jobPosting = jobPostingList[position]

        // jobmanagement_item.xml 뷰에 데이터를 바인딩합니다.
        holder.jobTitleTextView.text = jobPosting.job_title
        holder.endDateTextView.text = "~${jobPosting.job_deadline}"
        holder.tvJobExperienceRequirede.text = jobPosting.job_experience_required
        holder.tvJobEducationRequirede.text = jobPosting.job_education_required

        // C_MemberModel에서 필요한 데이터를 가져와서 사용합니다.
        val companyInfo = companyData.find { it.company_name == jobPosting.company_name }
        if (companyInfo != null) {
            holder.tvJobCompanyName.text = companyInfo.company_name
            holder.tvJobAdress.text = companyInfo.company_address

        }

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

        // 리스트 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            // JobPostingDetailActivity로 이동하고 선택한 항목의 정보를 전달합니다.
            val context = holder.itemView.context
            val intent = Intent(context, JobPostingDetailActivity::class.java)

            // 선택한 항목의 정보를 인텐트에 추가합니다.
            intent.putExtra("jobPosting", jobPosting)
            intent.putExtra("companyName", companyInfo?.company_name) // 회사 이름을 추가

            // Activity를 시작합니다.
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return jobPostingList.size
    }

    inner class JobPostingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJobCompanyName: TextView = itemView.findViewById(R.id.tvJobCompanyName)
        val tvJobExperienceRequirede: TextView = itemView.findViewById(R.id.tvJobExperienceRequirede)
        val tvJobEducationRequirede: TextView = itemView.findViewById(R.id.tvJobEducationRequirede)
        val tvJobAdress: TextView = itemView.findViewById(R.id.tvJobAdress)

        val jobTitleTextView: TextView = itemView.findViewById(R.id.tvJobManagementTitle)
        val endDateTextView: TextView = itemView.findViewById(R.id.tvJobManagementEndDate)
        val modifyButton: Button = itemView.findViewById(R.id.btnJobManagement_modify)
        val repostButton: Button = itemView.findViewById(R.id.btnJobManagement_repost)
        val endButton: Button = itemView.findViewById(R.id.btnJobManagement_end)
    }
}
