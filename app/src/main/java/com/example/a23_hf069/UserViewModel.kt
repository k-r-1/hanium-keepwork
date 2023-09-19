package com.example.a23_hf069

import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private var _userId: String? = null // 사용자 ID를 저장할 변수

    val userId: String?
        get() = _userId

    // 추가: ViewModel을 초기화할 때 _userId를 초기화
    init {
        _userId = ""
    }

    fun setUserId(id: String) {
        _userId = id
    }
}
