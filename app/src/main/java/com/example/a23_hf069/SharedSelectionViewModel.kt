package com.example.a23_hf069

import androidx.lifecycle.ViewModel

class SharedSelectionViewModel : ViewModel() {
    var selectedJob: String? = null
    var selectedRegion: String? = null
    var region_filteredList: List<WantedFilteringFragment.Wanted> = emptyList()
}