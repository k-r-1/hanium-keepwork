package com.example.a23_hf069

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class JobManagementPostFragment : Fragment() {

    private var IP_ADDRESS = "54.180.186.168"
    private val recyclerViewAdapter = JobPostAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_job_manangement_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recylcerviewJobPost)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = recyclerViewAdapter

        fetchJobPosts()
    }

    private fun fetchJobPosts() {
        val client = OkHttpClient()
        val phpUrl = "http://$IP_ADDRESS/android_recruit_post.php"
        val requestBody = FormBody.Builder()
            .add("company_email", "keepwork@naver.com")
            .build()
        val request = Request.Builder()
            .url(phpUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                val jobPosts = parseJobPosts(responseData)

                GlobalScope.launch(Dispatchers.Main) {
                    recyclerViewAdapter.setJobPosts(jobPosts)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
        })
    }

    private fun parseJobPosts(json: String?): List<JobPost> {
        val jobPosts = mutableListOf<JobPost>()
        try {
            val jsonArray = JSONArray(json)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val title = jsonObject.getString("recruitTitle")
                val startDate = jsonObject.getString("recruitStart")
                val endDate = jsonObject.getString("recruitEnd")
                jobPosts.add(JobPost(title, startDate, endDate))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jobPosts
    }
}

data class JobPost(val title: String, val startDate: String, val endDate: String)

class JobPostAdapter : RecyclerView.Adapter<JobPostAdapter.ViewHolder>() {
    private val jobPosts = mutableListOf<JobPost>()

    fun setJobPosts(posts: List<JobPost>) {
        jobPosts.clear()
        jobPosts.addAll(posts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.jobmanagement_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobPost = jobPosts[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvJobManagementTitle).text = jobPost.title
            findViewById<TextView>(R.id.tvJobManagentStartDate).text = jobPost.startDate
            findViewById<TextView>(R.id.tvJobManagementEndDate).text = jobPost.endDate
            // Set click listeners and other UI updates as needed
        }
    }

    override fun getItemCount(): Int {
        return jobPosts.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
