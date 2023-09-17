package com.example.a23_hf069

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResumeSelectionAdapter : RecyclerView.Adapter<ResumeSelectionAdapter.ViewHolder>() {

    private val items = mutableListOf<String>() // 데이터 리스트
    private var selectedItemPosition = -1 // 현재 선택된 항목의 위치

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
        val itemText: TextView = itemView.findViewById(R.id.itemText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resume_selection_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        // 데이터 설정
        holder.itemText.text = item

        // 체크박스 상태 설정
        holder.checkBox.isChecked = (position == selectedItemPosition)

        // 체크박스 리스너 설정
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 새 항목 선택 시 이전 선택 항목의 체크 해제
                if (selectedItemPosition != position) {
                    val prevSelectedItemPosition = selectedItemPosition
                    selectedItemPosition = position
                    notifyItemChanged(prevSelectedItemPosition)
                }
            } else if (position == selectedItemPosition) {
                // 항목 선택 해제 시
                selectedItemPosition = -1
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // 데이터 추가 메서드 (외부에서 데이터를 추가할 때 호출)
    fun addItem(item: String) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }
}
