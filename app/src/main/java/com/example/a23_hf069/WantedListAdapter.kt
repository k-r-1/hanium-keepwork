//package com.example.a23_hf069
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.TextView
//
//class WantedListAdapter(private val context: Context, private val wantedList: MutableList<Wanted>) : BaseAdapter() {
//    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
//
//    override fun getCount(): Int {
//        return wantedList?.size ?: 0
//    }
//
//    override fun getItem(position: Int): Any? {
//        return wantedList?.get(position)
//    }
//
//    override fun getItemId(position: Int): Long {
//        return 0
//    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        var convertView = convertView
//        val holder: ViewHolder
//        if (convertView == null) {
//            convertView = layoutInflater.inflate(R.layout.adapter_wanted_list, parent, false)
//            holder = ViewHolder()
//            holder.title = convertView!!.findViewById<TextView>(R.id.tv_wanted_title) //채용정보 제목자리
//            holder.region = convertView.findViewById<TextView>(R.id.tv_wanted_region) //지역자리
//            holder.closeDt = convertView.findViewById<TextView>(R.id.tv_wanted_closeDt) //마감일 자리
//            convertView.tag = holder
//        } else {
//            holder = convertView.tag as ViewHolder
//        }
//        holder.title!!.text = wantedList!![position].getTitle()
//        holder.region!!.text = wantedList!![position].getRegion()
//        holder.closeDt!!.text = wantedList!![position].getCloseDt()
//        return convertView
//    }
//
//    internal class ViewHolder {
//        var title: TextView? = null
//        var region: TextView? = null
//        var closeDt: TextView? = null
//    }
//}
