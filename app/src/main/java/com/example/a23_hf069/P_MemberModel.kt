package com.example.a23_hf069
data class P_MemberModel (
    val personal_id: String,
    val personal_password: String,
    val personal_name: String,
    val personal_birth: String,
    val personal_email: String,
    val personal_phonenum: String,
    val personal_address: String
)


data class NoticeModel (
    val notice_listnum: Int,
    val notice_title: String,
    val notice_content: String,
    val notice_date: String
)