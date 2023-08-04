package com.example.a23_hf069

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import java.io.IOException

class NoticeActivity : AppCompatActivity() {
    // 서버의 IP 주소를 저장할 변수
    private var IP_ADDRESS = "13.209.65.106"

    private lateinit var backButton_notice: ImageButton

    // 데이터를 담을 리스트
    private var dataList: List<NoticeItem> = emptyList()

    // 공지사항 목록을 표시할 RecyclerView 변수
    private lateinit var recyclerViewNotice: RecyclerView

    // 어댑터를 RecyclerView에 설정
    private lateinit var dataAdapterNotice: RecyclerView.Adapter<NoticeActivity.ViewHolder>

    data class NoticeItem(val noticeListNum: Int, val noticeTitle: String, val noticeDate: String)
    data class DataListContainer(val noticeList: List<NoticeItem>)

    // View holder class for DataAdapter
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: Button = itemView.findViewById(R.id.tvNotice_title)
        val dateTextView: TextView = itemView.findViewById(R.id.tvNotice_date)
    }

    // 어댑터 클래스
    inner class DataAdapterNotice : RecyclerView.Adapter<ViewHolder>() {
        // Inflate the layout for each item and return a ViewHolder
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.notice_item, parent, false)
            return ViewHolder(itemView)

        }

        // Bind data to each item in the RecyclerView
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = dataList[position]
            holder.titleTextView.setText(item.noticeTitle)
            holder.dateTextView.text = item.noticeDate

            // 공지사항 제목 버튼 클릭 리스너 설정
            holder.titleTextView.setOnClickListener {
                val intent = Intent(holder.itemView.context, NoticeContentActivity::class.java)
                intent.putExtra("noticeListNum", item.noticeListNum)
                holder.itemView.context.startActivity(intent)
            }
        }

        // Get the number of items in the list
        override fun getItemCount(): Int {
            return dataList.size
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_notice)

        // RecyclerView 초기화 후 어댑터 설정
        recyclerViewNotice = findViewById(R.id.recyclerviewNotice)
        recyclerViewNotice.layoutManager = LinearLayoutManager(this)

        // 어댑터 생성 및 RecyclerView에 설정
        dataAdapterNotice = DataAdapterNotice()
        recyclerViewNotice.adapter = dataAdapterNotice

        // 사용자의 작성중 이력서 개수와 작성완료 이력서 개수를 업데이트
        fetchDataFromServer()

        backButton_notice = findViewById(R.id.backButton)

        backButton_notice.setOnClickListener {
            finish()
        }
    }

    // 서버로부터 데이터를 가져오는 메서드
    private fun fetchDataFromServer() {
        // 서버로 사용자 아이디를 전송하여 이력서 데이터를 가져오도록 요청
        val phpUrl = "http://$IP_ADDRESS/android_notice.php"
        val requestBody = FormBody.Builder()
            .build()
        val request = Request.Builder()
            .url(phpUrl)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // 서버로부터 응답을 받았을 때 호출되는 콜백 메서드
                val responseData = response.body?.string()
                Log.d("ServerResponse", responseData ?: "No response data")
                if (responseData != null) {
                    try {
                        // JSON 파싱을 위해 Gson 객체 생성
                        val gson = Gson()

                        // 서버 응답 데이터를 담는 컨테이너 클래스로 파싱
                        val dataListContainer =
                            gson.fromJson(responseData, DataListContainer::class.java)

                        // UI 업데이트는 메인 스레드에서 실행되어야 함
                        runOnUiThread {
                            // 가져온 공지사항 데이터 리스트를 어댑터에 설정하여 RecyclerView 업데이트
                            dataList = dataListContainer?.noticeList?.reversed() ?: emptyList() // 데이터를 역순으로 설정
                            dataAdapterNotice.notifyDataSetChanged()
                        }
                    } catch (e: JSONException) {
                        // JSON 파싱 오류 발생한 경우
                        Log.e("JSONParsingError", "Invalid JSON format: $responseData")
                    }
                } else {
                    // responseData가 null인 경우에 대한 처리를 여기에 추가
                    Log.e("ServerResponse", "Response data is null")
                    Toast.makeText(this@NoticeActivity, "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                e.printStackTrace()
            }
        })
    }
}
