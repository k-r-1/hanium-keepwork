//package com.example.a23_hf069
//
//import android.app.ProgressDialog
//import android.content.Context
//import android.net.ConnectivityManager
//import android.os.AsyncTask
//import android.os.Bundle
//import android.view.View
//import android.widget.*
//import android.widget.AdapterView.OnItemClickListener
//import androidx.appcompat.app.AppCompatActivity
//import kotlinx.coroutines.NonCancellable.cancel
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//import java.util.Collections.addAll
//import javax.net.ssl.HttpsURLConnection
//
//class JobsActivity : AppCompatActivity(){
//    var lvList: ListView? = null
//
//    var adapter: ArrayAdapter<Job>? = null
//
//    var resultList: List<Job>? = null
//
//    var name: TextView? = null
//    var code: String? = null
//    var apiAddress: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setContentView(R.layout.activity_jobs)
//       // name = findViewById<TextView>(R.id.tv_select_job)
//        //lvList = findViewById<View>(R.id.lv_jobs) as ListView?
//        resultList = ArrayList()
//        adapter = ArrayAdapter<Job>(this, android.R.layout.simple_list_item_1,
//            resultList as ArrayList<Job>
//        )
//        lvList!!.adapter = adapter
//        apiAddress = getResources().getString(R.string.jobs_url)
//        lvList!!.onItemClickListener = listener
//        if (!isOnline()) {
//            Toast.makeText(this, "네트워크를 사용가능하게 설정해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        NetworkAsyncTask().execute(apiAddress)
//    }
//
//    var listener =
//        OnItemClickListener { parent, view, position, id ->
//            name!!.text = resultList!![position].getJobNm()
//            code = resultList!![position].getJobCd()
//        }
//
//    fun onClick(v: View) {
//        when (v.id) {
//            /*R.id.btn_job_select_complete -> {
//                val intent = Intent()
//                intent.putExtra("job_name", name!!.text.toString())
//                intent.putExtra("job_code", code)
//                setResult(Activity.RESULT_OK, intent)
//            }*/ //직종선택 완료 후 선택한 직종 표시
//        }
//        finish()
//    }
//
//    internal class NetworkAsyncTask : (private val activity: JobsActivity, private val resultList: List<Job>)
//        AsyncTask<String?, Void?, String?>() {
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
//        override fun doInBackground(vararg strings: String?): String {
//            val address = strings[0]
//            val result: String? = activity.downloadContents(address)
//            if (result == null) {
//                cancel(true)
//                return NETWORK_ERR_MSG
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: String?) {
//            progressDlg!!.dismiss() // 진행상황 다이얼로그 종료
//            adapter.clear() // 어댑터에 남아있는 이전 내용이 있다면 클리어
//
////          parser 생성 및 OpenAPI 수신 결과를 사용하여 parsing 수행
//            val parser = JobXmlParser()
//            resultList.addAll(parser.parse4(result))
//            if (resultList == null) {
//                Toast.makeText(activity, "리스트1 수신 실패", Toast.LENGTH_SHORT).show()
//            } else if (!resultList.isEmpty()) {
//                adapter.addAll(resultList) // 리스트뷰에 연결되어 있는 어댑터에 parsing 결과 ArrayList 를 추가
//            }
//        }
//
//        override fun onCancelled(msg: String?) {
//            super.onCancelled(msg)
//            progressDlg!!.dismiss()
//            Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
//        }
//
//        companion object {
//            const val NETWORK_ERR_MSG = "Server Error!"
//            const val TAG = "NetworkAsyncTask"
//        }
//    } // 클래스 끝
//
//
//    /* 이하 네트워크 접속을 위한 메소드 */
//
//
//    /* 이하 네트워크 접속을 위한 메소드 */ /* 네트워크 환경 조사 */
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
//        conn!!.readTimeout = 3000
//        conn.connectTimeout = 3000
//        conn.requestMethod = "GET"
//        conn.doInput = true
//        if (conn.responseCode != HttpsURLConnection.HTTP_OK) {
//            throw IOException("HTTP error code: " + conn.responseCode)
//        }
//        return conn.inputStream
//    }
//
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