package com.example.a23_hf069

import androidx.lifecycle.ViewModel

class SharedSelectionViewModel : ViewModel() {
    var selectedJob: String? = null
    var selectedRegion: String? = null
    var selectedJobCode: String?=null
    var region_filteredList: List<WantedFilteringFragment.Wanted> = emptyList()
    var edu_filterdList: List<WantedFilteringFragment.Wanted> = emptyList()
    var career_filterdList: List<WantedFilteringFragment.Wanted> = emptyList()
    var closeDt_filterdList: List<WantedFilteringFragment.Wanted> = emptyList()
}