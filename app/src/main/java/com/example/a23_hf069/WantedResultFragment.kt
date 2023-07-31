package com.example.a23_hf069

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels


class WantedResultFragment : Fragment() {
    private lateinit var listView: ListView
    //viewModel 생성 (단, var로 선언하면 안됨)
    private val sharedSelectionViewModel: SharedSelectionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ListView를 보여줄 레이아웃 파일을 연결
        val rootView = inflater.inflate(R.layout.fragment_wanted_result, container, false)
        listView = rootView.findViewById(R.id.listView)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateWantedList()
    }

    // ListView에 표시될 채용공고 목록을 업데이트하는 함수
    fun updateWantedList() {
        // 리스트뷰 어댑터를 해제하여 초기화
        listView.adapter = null

        val filteredList = mutableListOf<WantedFilteringFragment.Wanted>()

        filteredList.addAll(sharedSelectionViewModel.region_filteredList)
        filteredList.addAll(sharedSelectionViewModel.edu_filterdList)
        filteredList.addAll(sharedSelectionViewModel.career_filterdList)
        filteredList.addAll(sharedSelectionViewModel.closeDt_filterdList)

        val adapter = WantedListAdapter(requireContext(), filteredList)
        listView.adapter = adapter
    }

    class WantedListAdapter(context: Context, private val wantedList: List<WantedFilteringFragment.Wanted>) :
        ArrayAdapter<WantedFilteringFragment.Wanted>(context, R.layout.wanted_list_item, wantedList) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var itemView = convertView
            if (itemView == null) {
                val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                itemView = inflater.inflate(R.layout.wanted_list_item, parent, false)
            }

            val titleTextView: TextView = itemView?.findViewById(R.id.tv_title) ?: throw NullPointerException("tv_title not found in the layout")
            val companyTextView: TextView = itemView?.findViewById(R.id.tv_company) ?: throw NullPointerException("tv_company not found in the layout")
            val closeDtTextView: TextView = itemView?.findViewById(R.id.tv_any) ?: throw NullPointerException("tv_any not found in the layout")

            val currentItem = wantedList[position]
            titleTextView.text = currentItem.title
            companyTextView.text = currentItem.company
            closeDtTextView.text = currentItem.closeDt

            return itemView
        }
    }
}
