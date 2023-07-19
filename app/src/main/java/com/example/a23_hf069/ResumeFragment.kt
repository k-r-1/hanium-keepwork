package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException

class ResumeFragment : Fragment() {
    private var IP_ADDRESS = "54.180.82.123" // 본인 IP주소를 넣으세요.
    private lateinit var userId: String // 사용자 아이디

    private lateinit var buttonSubmit: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: DataAdapter
    private val dataList: MutableList<Data> = mutableListOf()  // DB에서 가져온 데이터 리스트

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_resume, container, false)

        // 사용자 아이디 받아오기
        if (arguments != null) {
            userId = arguments?.getString("userId", "") ?: ""
        }

        val textID = view.findViewById<TextView>(R.id.tvID1)
        textID.text = userId

        // RecyclerView 초기화
        recyclerView = view.findViewById(R.id.recyclerviewResume)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        dataAdapter = DataAdapter(dataList)
        recyclerView.adapter = dataAdapter

        // PHP 파일 URL
        val phpUrl = "http://$IP_ADDRESS/android_resume_php.php"

        // HTTP 요청 보내기
        val request = Request.Builder()
            .url(phpUrl)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    // 응답 데이터 처리
                    handleResponseData(responseData)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                e.printStackTrace()
            }
        })

        buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)
        buttonSubmit.setOnClickListener {
            val intent = Intent(requireContext(), ResumeWriteActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        return view
    }

    private fun handleResponseData(responseData: String) {
        try {
            // JSON 데이터 파싱
            val jsonArray = JSONArray(responseData)
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val resumeTitle = jsonObject.getString("resumeTitle")
                val writeStatus = jsonObject.getString("writeStatus")
                val data = Data(resumeTitle, writeStatus)
                dataList.add(data)
            }

            // RecyclerView 갱신
            dataAdapter.notifyDataSetChanged()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}

data class Data(val resumeTitle: String, val writeStatus: String)

class DataAdapter(private val dataList: List<Data>) :
    RecyclerView.Adapter<DataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.tvResumeTitle)
        val textViewStatus: TextView = itemView.findViewById(R.id.tvWriteStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.resume_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.textViewTitle.text = data.resumeTitle
        holder.textViewStatus.text = data.writeStatus
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
