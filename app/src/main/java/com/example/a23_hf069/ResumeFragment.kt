import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a23_hf069.R
import com.example.a23_hf069.ResumeWriteActivity
import com.google.gson.Gson
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class ResumeFragment : Fragment() {
    private var IP_ADDRESS = "15.164.49.206"
    private lateinit var userId: String

    private lateinit var tvResume_temporary_count: TextView
    private lateinit var tvResume_complete_count:TextView
    private lateinit var buttonAddResume: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataAdapter: DataAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_resume, container, false)

        if (arguments != null) {
            userId = arguments?.getString("userId", "") ?: ""
        }

        val textID = view.findViewById<TextView>(R.id.tvID1)
        textID.text = userId

        recyclerView = view.findViewById(R.id.recyclerviewResume)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // RecyclerView 초기화 후 빈 어댑터 설정
        dataAdapter = DataAdapter(emptyList())
        recyclerView.adapter = dataAdapter

        tvResume_temporary_count = view.findViewById(R.id.tvResume_temporary_count)
        tvResume_complete_count = view.findViewById(R.id.tvResume_complete_count)

        // 서버로 사용자 아이디를 전송하여 이력서 데이터를 가져오도록 요청
        val phpUrl = "http://$IP_ADDRESS/android_resume_php.php"
        val requestBody = FormBody.Builder()
            .add("personal_id", userId)
            .build()
        val request = Request.Builder()
            .url(phpUrl)
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                Log.d("ServerResponse", responseData ?: "No response data")
                if (responseData != null) {
                    try {
                        val jsonObject = JSONObject(responseData)
                        // JSON 파싱 성공한 경우
                        // 응답 데이터 처리
                        val gson = Gson()
                        val dataListContainer = gson.fromJson(responseData, DataListContainer::class.java)

                        // UI 업데이트는 메인 스레드에서 실행되어야 함
                        requireActivity().runOnUiThread {
                            val dataList = dataListContainer?.resumeList
                            if (dataList != null) {
                                // RecyclerView에 어댑터 설정
                                dataAdapter.setData(dataList)

                                // 이력서 개수 설정
                                val cnt1 = dataListContainer.cnt1
                                val cnt2 = dataListContainer.cnt2

                                // cnt1, cnt2가 null인지 여부를 확인하여 설정
                                if (cnt1 != null && cnt2 != null) {
                                    tvResume_temporary_count.text = cnt1.toString()
                                    tvResume_complete_count.text = cnt2.toString()
                                } else {
                                    // cnt1 또는 cnt2가 null인 경우에 대한 처리를 여기에 추가
                                    Toast.makeText(view?.context, "이력서 개수를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                                }

                            } else {
                                // dataList가 null인 경우에 대한 처리를 여기에 추가
                                Toast.makeText(view?.context, "서버로부터 이력서 데이터를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (e: JSONException) {
                        // JSON 파싱 오류 발생한 경우
                        Log.e("JSONParsingError", "Invalid JSON format: $responseData")
                    }
                } else {
                    // responseData가 null인 경우에 대한 처리를 여기에 추가
                    Log.e("ServerResponse", "Response data is null")
                    Toast.makeText(view?.context, "서버로부터 응답이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // 요청 실패 처리
                e.printStackTrace()
            }
        })

        buttonAddResume = view.findViewById<Button>(R.id.btnAddResume)
        buttonAddResume.setOnClickListener {
            val intent = Intent(requireContext(), ResumeWriteActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        return view
    }

    data class ResumeData(val resumeTitle: String, val writeStatus: String)

    data class DataListContainer(
        val resumeList: List<ResumeData> = emptyList(),
        val cnt1: Int = 0,
        val cnt2: Int = 0
    )

    class DataAdapter(private var dataList: List<ResumeData>) : RecyclerView.Adapter<DataAdapter.ViewHolder>() {

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

        // 외부에서 데이터를 설정할 수 있도록 setData() 함수 추가
        fun setData(newDataList: List<ResumeData>) {
            dataList = newDataList
            notifyDataSetChanged()
        }
    }
}
