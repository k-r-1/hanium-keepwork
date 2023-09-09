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

data class C_MemberModel (
    val company_id: String,
    val company_password: String,
    val company_manager: String,
    val company_email: String,
    val company_phonenum: String,
    val company_registnum: String,
    val company_name: String,
    val company_representative: String,
    val company_address: String,
    val company_establishment: Int,
    val company_employees: Int,
    val company_type: String
)

data class NoticeModel (
    val notice_listnum: Int,
    val notice_title: String,
    val notice_content: String,
    val notice_date: String
)