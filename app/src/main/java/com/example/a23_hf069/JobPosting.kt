data class JobPosting(
    val job_listnum: Int, // 또는 Long 등으로 변경
    val company_id: String,  // 또는 MemberCompany 모델에 맞는 데이터 타입으로 변경
    val job_title: String,
    val job_experience_required: String,
    val job_education_required: String,
    val job_period: String,
    val job_days_of_week: String,
    val job_working_hours: String,
    val job_salary: String,
    val job_position: String,
    val job_category: String,
    val job_requirements: String,
    val job_contact_number: String,
    val job_email: String,
    val job_deadline: String
)
