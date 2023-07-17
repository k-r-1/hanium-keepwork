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
//import android.widget.*
//import android.widget.AdapterView.OnItemClickListener
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat.getSystemService
//import androidx.databinding.DataBindingUtil.setContentView
//import java.io.BufferedReader
//import java.io.IOException
//import java.io.InputStream
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//import javax.net.ssl.HttpsURLConnection
//
//class RegionActivity : AppCompatActivity(){
//    var lvList1: ListView? = null
//    var lvList2: ListView? = null
//    var apiAddress: String? = null
//
//    var adapter1: ArrayAdapter<Region>? = null
//    var adapter2: ArrayAdapter<Region>? = null
//
//    var resultList1: List<Region>? = null
//    var resultList2: MutableList<Region>? = null
//    var name: TextView? = null
//    var superCd: String? = null
//    var code: String? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        //setContentView(R.layout.activity_region)
//       // name = findViewById<TextView>(R.id.tv_selected_region_name)
//       // lvList1 = findViewById<View>(R.id.lv_superRegion) as ListView?
//       // lvList2 = findViewById<View>(R.id.lv_region) as ListView?
//        resultList1 = ArrayList()
//        resultList2 = ArrayList()
//        adapter1 = ArrayAdapter<Region>(this, android.R.layout.simple_list_item_1,
//            resultList1 as ArrayList<Region>
//        )
//        lvList1!!.adapter = adapter1
//        adapter2 = ArrayAdapter<Region>(this, android.R.layout.simple_list_item_1,
//            resultList2 as ArrayList<Region>
//        )
//        lvList2!!.adapter = adapter2
//        apiAddress = getResources().getString(R.string.region_url)
//        lvList1!!.onItemClickListener = listener1
//        lvList2!!.onItemClickListener = listener2
//        if (!isOnline()) {
//            Toast.makeText(this@RegionActivity, "네트워크를 사용가능하게 설정해주세요.", Toast.LENGTH_SHORT).show()
//            return
//        }
//        NetworkAsyncTask().execute(apiAddress)
//    }
//
//    var listener1 =
//        OnItemClickListener { parent, view, position, id ->
//            resultList2!!.clear()
//            name!!.text = resultList1!![position].getName()
//            superCd = resultList1!![position].getSuperCd()
//            code = resultList1!![position].getSuperCd()
//            if (code == "00000") {
//                adapter2!!.clear()
//            }
//            resultList2!!.addAll(resultList1!![position].getrList())
//            adapter2!!.notifyDataSetChanged()
//        }
//
//    var listener2 =
//        OnItemClickListener { parent, view, position, id ->
//            name!!.text = resultList2!![position].getName()
//            code = resultList2!![position].getCode()
//        }
//
//    fun onClick(v: View) {
//        when (v.id) {
//           /* R.id.btn_region_select_complete -> {
//                val intent = Intent()
//                intent.putExtra("region_name", name!!.text.toString())
//                intent.putExtra("region_code", code)
//                setResult(Activity.RESULT_OK, intent)
//            }*/ //지역 선택 후 선택한 지역 표시
//        }
//        finish()
//    }
//
//
//    internal class NetworkAsyncTask : AsyncTask<String, Void, String>() {
//
//        companion object {
//            const val NETWORK_ERR_MSG = "Server Error!"
//            const val TAG = "NetworkAsyncTask"
//        }
//
//        private lateinit var progressDlg: ProgressDialog
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//            progressDlg = ProgressDialog.show(
//                RegionActivity@this,
//                "Wait",
//                "Downloading..."
//            )
//        }
//
//        override fun doInBackground(vararg strings: String): String {
//            val address = strings[0]
//            val result = downloadContents(address)
//            if (result == null) {
//                cancel(true)
//                return NETWORK_ERR_MSG
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: String) {
//            progressDlg.dismiss()
//            adapter1.clear()
//
//            val parser = RegionXmlParser()
//            resultList1 = parser.parse1(result)
//            if (resultList1 == null) {
//                Toast.makeText(RegionActivity@this, "리스트1 수신 실패", Toast.LENGTH_SHORT).show()
//            } else if (resultList1.isNotEmpty()) {
//                adapter1.addAll(resultList1)
//            }
//        }
//
//        override fun onCancelled(msg: String) {
//            super.onCancelled()
//            progressDlg.dismiss()
//            Toast.makeText(RegionActivity@this, msg, Toast.LENGTH_SHORT).show()
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