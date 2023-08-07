package com.example.a23_hf069

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedSelectionViewModel : ViewModel() {
    var selectedJob: String? = ""
    var selectedRegion: String? = ""

    private val _region_filteredList = MutableLiveData<List<WantedFilteringFragment.Wanted>>()
    val region_filteredList: LiveData<List<WantedFilteringFragment.Wanted>> get() = _region_filteredList

    private val _edu_filteredList = MutableLiveData<List<WantedFilteringFragment.Wanted>>()
    val edu_filteredList: LiveData<List<WantedFilteringFragment.Wanted>> get() = _edu_filteredList

    private val _career_filteredList = MutableLiveData<List<WantedFilteringFragment.Wanted>>()
    val career_filteredList: LiveData<List<WantedFilteringFragment.Wanted>> get() = _career_filteredList

    private val _closeDt_filteredList = MutableLiveData<List<WantedFilteringFragment.Wanted>>()
    val closeDt_filteredList: LiveData<List<WantedFilteringFragment.Wanted>> get() = _closeDt_filteredList

    // 필터링된 리스트들을 업데이트하는 함수
    fun updateFilteredList(category: String, filteredList: List<WantedFilteringFragment.Wanted>) {
        when (category) {
            "region" -> _region_filteredList.value = filteredList
            "edu" -> _edu_filteredList.value = filteredList
            "career" -> _career_filteredList.value = filteredList
            "closeDt" -> _closeDt_filteredList.value = filteredList
        }
    }

}