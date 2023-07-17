//package com.example.a23_hf069
//
//import android.app.Activity
//import android.app.ProgressDialog
//import android.content.Context
//import android.content.Intent
//import android.net.ConnectivityManager
//import android.os.AsyncTask
//import android.os.Bundle
//import android.view.View
//import android.widget.AdapterView.OnItemClickListener
//import android.widget.ListView
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.a23_hf069.WantedListActivity
//import androidx.core.app.ActivityCompat.startActivityForResult
//import androidx.core.content.ContextCompat.getSystemService
//import androidx.core.content.ContextCompat.startActivity
//import androidx.databinding.DataBindingUtil.setContentView
//import kotlinx.coroutines.NonCancellable.cancel
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//import javax.net.ssl.HttpsURLConnection
//
//
//
//class WantedListActivity : AppCompatActivity(){
//    var lvList: ListView? = null
//    var apiAddress: String? = null
//
//    var adapter: WantedListAdapter? = null
//    var resultList: MutableList<Wanted> = mutableListOf()
//
//    var total: String? = null
//    var startpg: TextView? = null
//    var lastpg: TextView? = null
//    var p = 1
//    var lp = 1
//    var query = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_wanted_work_net)
//        lvList = findViewById<ListView>(R.id.lv_wanted) //채용공고목록
//        startpg = findViewById<TextView>(R.id.tv_wanted_startpg) //시작페이지
//        lastpg = findViewById<TextView>(R.id.tv_wanted_lastpg) //마지막페이지
//        adapter = WantedListAdapter(this, resultList.toMutableList())
//        lvList!!.adapter = adapter
//        apiAddress = getResources().getString(R.string.wanted_url)
//        // 워크넷 채용공고 리스트를 불러오는 작업을 여기에서 실행
//        val apiKey = "WNLJYZLM2VZXTT2TZA9XR2VR1HK"
//        val keyword = ""
//
//        // 채용공고 리스트를 불러오는 함수 호출
//        val jobPostings = getJobPostings(apiKey, keyword)
//        resultList.addAll(jobPostings)
//        adapter?.notifyDataSetChanged()
//
//        lvList!!.onItemClickListener =
//            OnItemClickListener { parent, view, position, id -> //채용공고 상세보기 페이지로 전환
//                val intent = Intent(this@WantedListActivity, WantedWorkNetFragment::class.java)
//                intent.putExtra("company", (resultList as ArrayList<Wanted>).get(position).getCompany())
//                intent.putExtra("title", (resultList as ArrayList<Wanted>).get(position).getTitle())
//                intent.putExtra("salTp", (resultList as ArrayList<Wanted>).get(position).getSalTpNm())
//                intent.putExtra("sal", (resultList as ArrayList<Wanted>).get(position).getSal())
//                intent.putExtra("region", (resultList as ArrayList<Wanted>).get(position).getRegion())
//                intent.putExtra("minEdu", (resultList as ArrayList<Wanted>).get(position).getMinEdubg())
//                intent.putExtra("career", (resultList as ArrayList<Wanted>).get(position).getCareer())
//                intent.putExtra("closeDt", (resultList as ArrayList<Wanted>).get(position).getCloseDt())
//                intent.putExtra("url", (resultList as ArrayList<Wanted>).get(position).getWantedMobileInfoUrl())
//                intent.putExtra("basicAddr", (resultList as ArrayList<Wanted>).get(position).getBasicAddr())
//                startActivity(intent)
//            }
//    }
//
//    fun onClick(v: View) {
//        when (v.id) {
//            R.id.searchContent -> { //검색버튼
//                total = null
//                val intent = Intent(this, WantedFilteringFragment::class.java)
//                startActivityForResult(intent, 100)
//            }
//            R.id.btn_wantedList_previous -> { //이전페이지버튼
//                p = p - 1
//                if (p == 0) {
//                    p++
//                    Toast.makeText(this, "첫번째 페이지 입니다.", Toast.LENGTH_SHORT).show()
//                } else {
//                    NetworkAsyncTask(this@WantedListActivity,resultList as MutableList<Wanted>,total,lp,lastpg).execute("$apiAddress$query&startPage=$p")
//                }
//                startpg!!.text = p.toString()
//            }
//            R.id.btn_wantedList_next -> { //다음페이지버튼
//                p = p + 1
//                if (p > lp) {
//                    p--
//                    Toast.makeText(this, "마지막 페이지 입니다.", Toast.LENGTH_SHORT).show()
//                } else {
//                    NetworkAsyncTask(this@WantedListActivity,resultList as MutableList<Wanted>,total,lp,lastpg).execute("$apiAddress$query&startPage=$p")
//                    startpg!!.text = p.toString()
//                }
//            }
//            /* R.id.btn_getStarList -> { //즐겨찾기 추가 버튼
//                 val sintent = Intent(this, StarListActivity::class.java)
//                 startActivity(sintent)
//             }*/
//        }
//    }
//    // 채용공고 리스트를 불러오는 함수
//    private fun getJobPostings(apiKey: String, keyword: String): List<Wanted> {
//        val jobPostings = mutableListOf<Wanted>()
//
//        // 워크넷 Open API를 호출하기 위한 URL 구성
//        val apiAddress = resources.getString(R.string.wanted_url)
//        val query = "?apiKey=$apiKey&keyword=$keyword"
//
//        try {
//            // API 호출 및 결과 수신
//            val result = downloadContents("$apiAddress$query")
//
//            // 결과 파싱
//            val parser = WantedXmlParser()
//            jobPostings.addAll(parser.parse1(result))
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // 예외 처리 - API 호출 또는 파싱 오류
//        }
//
//        return jobPostings
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            100 -> if (resultCode == Activity.RESULT_OK) {
//                query = ""
//                val addr = HashMap<String, String>()
//                val regionCode = data!!.getStringExtra("region_code")
//                if (regionCode != null) {
//                    addr["region"] = regionCode
//                }
//                val jobCode = data.getStringExtra("job_code")
//                if (jobCode != null) {
//                    addr["occupation"] = jobCode
//                }
//                val eduList = ArrayList<String>()
//                val holiList = ArrayList<String>()
//                if (data.getStringArrayListExtra("eduList") != null) {
//                    eduList.addAll(data.getStringArrayListExtra("eduList")!!)
//                    var edu: String? = null
//                    var i = 0
//                    while (i < eduList.size) {
//                        if (edu != null) {
//                            edu += eduList[i]
//                        } else {
//                            edu = eduList[i]
//                        }
//                        if (i != eduList.size - 1) {
//                            edu += "|"
//                        }
//                        i++
//                    }
//                    if (edu != null) {
//                        addr["education"] = edu
//                    }
//                }
//
//                val keys: Iterator<String> = addr.keys.iterator()
//                while (keys.hasNext()) {
//                    val key = keys.next()
//                    query = query + key + "=" + addr[key]
//                    if (keys.hasNext()) query += "&"
//                }
//                if (!isOnline()) {
//                    Toast.makeText(this, "네트워크를 사용가능하게 설정해주세요.", Toast.LENGTH_SHORT).show()
//                    return
//                }
//                NetworkAsyncTask(this@WantedListActivity,resultList as MutableList<Wanted>,total,lp,lastpg).execute("$apiAddress$query&startPage=$p")
//
//            }
//        }
//    }
//
//    internal class NetworkAsyncTask (private val activity: WantedListActivity, private val resultList: MutableList<Wanted>, private var total: String?, private var lp: Int, private var lastpg: TextView?) :
//        AsyncTask<String?, Void?, String?>() { // NetworkAsyncTask 클래스 시작
//        var progressDlg: ProgressDialog? = null
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDlg = ProgressDialog.show(
//                activity,
//                "Wait",
//                "Downloading..."
//            ) // 진행상황 다이얼로그 출력
//        }
//
//        protected override fun doInBackground(vararg strings: String?): String {
//            val address = strings[0]
//            val result: String? = activity.downloadContents(address)
//            if (result == null) {
//                cancel(true)
//                return NETWORK_ERR_MSG
//            }
//            return result
//        }
//
//        protected fun onPostExecute(result: String) {
//            progressDlg!!.dismiss() // 진행상황 다이얼로그 종료
//            resultList.clear() // 어댑터에 남아있는 이전 내용이 있다면 클리어
//
//            //     parser 생성 및 OpenAPI 수신 결과를 사용하여 parsing 수행
//            val parser = WantedXmlParser()
//            resultList.addAll(parser.parse1(result))
//            activity.adapter?.notifyDataSetChanged()
//            if (total == null) {
//                total = parser.parse2(result)
//                if (total == null) {
//                    Toast.makeText(activity, "정보가 존재하지 않습니다.", Toast.LENGTH_SHORT)
//                        .show()
//                } else if (total != null) {
//                    lp = total!!.toInt() / 20 + 1
//                }
//                lastpg?.setText(lp.toString())
//            }
//        }
//
//        override fun onCancelled(msg: String?) {
//            super.onCancelled(msg)
//            progressDlg?.dismiss()
//            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
//        }
//
//        companion object {
//            const val NETWORK_ERR_MSG = "Server Error!"
//            const val TAG = "NetworkAsyncTask"
//        }
//    } // 클래스 끝
//
//    /* 이하 네트워크 접속을 위한 메소드 */
//    /* 네트워크 환경 조사 */
//    private fun isOnline(): Boolean {
//        val connMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo = connMgr.activeNetworkInfo
//        return networkInfo != null && networkInfo.isConnected
//    }
//
//
//    /* 주소(apiAddress)에 접속하여 문자열 데이터를 수신한 후 반환 */
//    protected fun downloadContents(address: String?): String? {
//        var conn: HttpURLConnection? = null
//        var stream: InputStream? = null
//        var result: String? = null
//        try {
//            val url = URL(address)
//            conn = url.openConnection() as HttpURLConnection
//            stream = getNetworkConnection(conn)
//            result = readStreamToString(stream)
//            stream?.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            conn?.disconnect()
//        }
//        return result
//    }
//
//    /* URLConnection 을 전달받아 연결정보 설정 후 연결, 연결 후 수신한 InputStream 반환 */
//    @Throws(Exception::class)
//    private fun getNetworkConnection(conn: HttpURLConnection?): InputStream? {
//        conn!!.readTimeout = 6000
//        conn.connectTimeout = 6000
//        conn.requestMethod = "GET"
//        conn.doInput = true
//        if (conn.responseCode != HttpsURLConnection.HTTP_OK) {
//            throw IOException("HTTP error code: " + conn.responseCode)
//        }
//        return conn.inputStream
//    }
//
//    /* InputStream을 전달받아 문자열로 변환 후 반환 */
//    protected fun readStreamToString(stream: InputStream?): String? {
//        val result = StringBuilder()
//        try {
//            val inputStreamReader = InputStreamReader(stream)
//            val bufferedReader = BufferedReader(inputStreamReader)
//            var readLine = bufferedReader.readLine()
//            while (readLine != null) {
//                result.append(
//                    """
//                    $readLine
//
//                    """.trimIndent()
//                )
//                readLine = bufferedReader.readLine()
//            }
//            bufferedReader.close()
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
//        return result.toString()
//    }
//}