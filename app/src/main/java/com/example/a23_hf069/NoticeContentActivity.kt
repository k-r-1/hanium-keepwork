package com.example.a23_hf069

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class NoticeContentActivity : AppCompatActivity() {
    private var IP_ADDRESS = "54.180.113.179" // Replace with your IP address.
    private var noticeListNum: Int = -1
    private lateinit var noticeTitle: TextView
    private lateinit var noticeContent: TextView
    private lateinit var noticeDate: TextView

    private lateinit var backButton_click: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_notice_content)

        // Get user ID
        noticeListNum = intent.getIntExtra("noticeListNum", -1)

        noticeTitle = findViewById(R.id.notice_title)
        noticeContent = findViewById(R.id.notice_content)
        noticeDate = findViewById(R.id.notice_date)

        backButton_click = findViewById(R.id.backButton_click)

        // 공지사항 아이템 데이터 불러오기
        getNoticeItemData(noticeListNum)

        backButton_click.setOnClickListener {
            onBackPressed()
        }
    }

    // 공지사항 아이템 데이터 불러오기
    private fun getNoticeItemData(noticeListNum: Int) {
        val url =
            "http://$IP_ADDRESS/android_notice_content.php?notice_listnum=$noticeListNum" // 데이터를 불러올 PHP 스크립트의 주소

        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .get() // GET 방식으로 요청 변경
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle request failure
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val noticeData = response.body?.string()
                // 공지사항 아이템 데이터를 파싱하여 UI 업데이트
                runOnUiThread {
                    handleNoticeItemData(noticeData)
                }
            }
        })
    }

    // 공지사항 아이템 데이터를 처리하는 함수 추가
    private fun handleNoticeItemData(responseData: String?) {
        try {
            val jsonObject = JSONObject(responseData) // JSONObject로 파싱

            // 공지사항 아이템 데이터가 존재하는 경우에만 UI 업데이트
            if (jsonObject.length() > 0) {
                // 여기서 공지사항 아이템 데이터를 파싱하여 UI에 표시하는 작업을 수행하면 됩니다.
                // 예를 들어, 다음과 같이 각 EditText에 데이터를 설정할 수 있습니다.
                noticeTitle.setText(jsonObject.optString("noticeTitle", ""))
                noticeContent.setText(jsonObject.optString("noticeContent", ""))
                noticeDate.setText(jsonObject.optString("noticeDate", ""))
            }
        } catch (e: JSONException) {
            // JSON 파싱 오류 처리
            e.printStackTrace()
        }
    }
}
