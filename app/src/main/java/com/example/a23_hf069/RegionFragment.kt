package com.example.a23_hf069

import android.content.Context
import android.content.Intent
import android.graphics.Region
import android.os.AsyncTask
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_job_detail.*
import kotlinx.android.synthetic.main.fragment_wanted_work_net.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class RegionFragment : Fragment() {
    private var s_regionList: List<Region> = emptyList()
    private var m_regionList: List<Region> = emptyList()
    private var regionList: List<Region> = emptyList()
    private lateinit var listView1: ListView
    private lateinit var listView2: ListView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_region, container, false)

        // UI 요소 초기화
        listView1 = view.findViewById(R.id.lv_superRegion)
        listView2 = view.findViewById(R.id.lv_middleRegion)


        // API 호출
        fetchRegionData()

        return view
    }

    private fun fetchRegionData() {
        val url =
            "http://openapi.work.go.kr/opi/commonCode/commonCode.do?returnType=XML&target=CMCD&authKey=WNLJYZLM2VZXTT2TZA9XR2VR1HK&dtlGb=1"
        FetchRegionData().execute(url)
    }

    private inner class FetchRegionData : AsyncTask<String, Void, List<Region>>() {
        override fun doInBackground(vararg urls: String): List<Region> {
            val urlString = urls[0]
            var result: List<Region> = emptyList()
            var connection: HttpURLConnection? = null

            try {
                val url = URL(urlString)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.readTimeout = 15 * 1000
                connection.connectTimeout = 15 * 1000

                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    result = parseXml(inputStream)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection?.disconnect()
            }

            return result
        }

        private fun parseXml(inputStream: InputStream): List<Region> {
            regionList = listOf<Region>()
            val factory = XmlPullParserFactory.newInstance()
            val xpp = factory.newPullParser()
            xpp.setInput(inputStream, null)

            var eventType = xpp.eventType
            var regionCd: String? = null // 지역 분류 코드
            var regionNm: String? = null // 지역 이름


            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (xpp.name=="oneDepth") { // 대분류
                            "regionCd" -> regionCd = xpp.nextText()
                            "regionNm" -> regionNm = xpp.nextText()
                            regionCd?.let { c ->
                                regionNm?.let { n ->
                                    s_regionList.add(
                                        Region(
                                            c, n
                                        )
                                    )
                                }
                            }
                            regionCd = null
                            regionNm = null
                            regionList = s_regionList

                        } else if (xpp.name == "twoDepth") { // 중분류
                            "regionCd" -> regionCd = xpp.nextText()
                            "regionNm" -> regionNm = xpp.nextText()
                            regionCd?.let { c ->
                                regionNm?.let { n ->
                                    m_regionList.add(
                                        Region(
                                            c, n
                                        )
                                    )
                                }
                            }
                            regionList = m_regionList
                            regionCd = null
                            regionNm = null

                        }
                    }

                }
                eventType = xpp.next()
            }
            return regionList
        }

        override fun onPostExecute(result: List<Region>) {
            regionList = result
            showRegionList()
        }
    }

    private fun showRegionList() {
        val adapter1 = UIAdapter(this@RegionFragment, s_regionList)
        val adapter2 = UIAdapter(this@RegionFragment, m_regionList)
        listView1.adapter = adapter1
        listView2.adapter = adapter2

        listView1.setOnItemClickListener { _, _, position, _ ->

        }
        listView2.setOnItemClickListener { _, _, position, _ ->

        }
    }
}

class UIAdapter(private val fragment: Fragment, private val RegionList: List<Region>) :
    ArrayAdapter<Region>(fragment.requireActivity(), R.layout.filtering_list_item , RegionList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.filtering_list_item, parent, false)

        val region = RegionList[position]

        val region_btn: Button = view.findViewById(R.id.region_btn)

        return view
    }
}


data class Region(
    val regionCd: String,
    val regionNm: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(regionCd)
        parcel.writeString(regionNm)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Region> {
        override fun createFromParcel(parcel: Parcel): Region {
            return Region(parcel)
        }

        override fun newArray(size: Int): Array<Region?> {
            return arrayOfNulls(size)
        }
    }
}
