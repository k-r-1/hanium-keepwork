package com.example.a23_hf069

import JobPosting
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.POST

interface RetrofitInterface {
    companion object {
        const val API_URL = "http://13.125.126.74:8000/"
    }
    @GET("keepwork/member_personal/")
    fun getData(@Query("personal_id") id: String): Call<List<P_MemberModel>>

    @POST("keepwork/member_personal/")
    fun postData(@Body data: P_MemberModel): Call<P_MemberModel>

    @GET("keepwork/member_company/")
    fun getCorporateData(@Query("company_id") id: String): Call<List<C_MemberModel>> // 추가: C_MemberModel 사용

    @GET("keepwork/job/")
    fun getJobPostingData(@Query("company_id") id: String): Call<List<JobPosting>>

    @GET("keepwork/notice/")
    fun getNoticeData(
        @Query("notice_listnum") noticeListNum: Int?,
        @Query("notice_title") noticeTitle: String?,
        @Query("notice_content") noticeContent: Int?,
        @Query("notice_date") noticeDate : String?
    ): Call<List<NoticeModel>>

    @GET("keepwork/resume/")
    fun getResumeData(
        @Query("personal_id_id") personalId2: String?,
        @Query("resume_listnum") resumeListnum: Int?,
        @Query("resume_title") resumeTitle: String?,
        @Query("resume_academic") resumeAcademic: String?,
        @Query("resume_career") resumeCareer: String?,
        @Query("resume_introduction") resumeIntroduction: String?,
        @Query("resume_certificate") resumeCertificate: String?,
        @Query("resume_learning") resumeLearning: String?,
        @Query("resume_desire") resumeDesire: String?,
        @Query("resume_complete") resumeComplete: String?
    ): Call<List<ResumeModel>>
}