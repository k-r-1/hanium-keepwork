from django.db import models

class MemberPersonal(models.Model):
    personal_id = models.CharField(max_length=50, primary_key=True, unique=True)
    personal_password = models.CharField(max_length=50)
    personal_name = models.CharField(max_length=50)
    personal_birth = models.CharField(max_length=8)
    personal_email = models.CharField(max_length=50, unique=True)
    personal_phonenum = models.CharField(max_length=50, unique=True)
    personal_address = models.CharField(max_length=100)

    class Meta:
        db_table = 'member_personal'

    def __str__(self):
        return self.personal_id

class MemberCompany(models.Model):
    company_id = models.CharField(max_length=50, primary_key=True, unique=True)
    company_password = models.CharField(max_length=50)
    company_manager = models.CharField(max_length=50)
    company_email = models.CharField(max_length=50)
    company_phonenum = models.CharField(max_length=50, unique=True)
    company_registnum = models.CharField(max_length=12, unique=True)
    company_name = models.CharField(max_length=50, unique=True, default="")
    company_representative = models.CharField(max_length=50)
    company_address = models.CharField(max_length=100)
    company_establishment = models.CharField(max_length=10)
    company_employees = models.CharField(max_length=10)
    company_type = models.CharField(max_length=20)

    class Meta:
        db_table = 'member_company'

    def __str__(self):
        return self.company_id

class Notice(models.Model):
    notice_listnum = models.AutoField(primary_key=True)
    notice_title = models.CharField(max_length=100)
    notice_content = models.TextField(blank=True, null=True)
    notice_date = models.CharField(max_length=10)
    
    class Meta:
        db_table = 'notice'

    def __str__(self):
        return self.notice_listnum

class Resume(models.Model):
    resume_listnum = models.AutoField(primary_key=True)
    personal_id = models.ForeignKey(MemberPersonal, on_delete=models.CASCADE)
    resume_title = models.CharField(max_length=50)
    resume_academic = models.CharField(max_length=100)
    resume_career = models.CharField(max_length=100)
    resume_introduction = models.CharField(max_length=500)
    resume_certificate = models.CharField(max_length=100)
    resume_learning = models.CharField(max_length=100)
    resume_desire = models.CharField(max_length=100)
    resume_complete = models.CharField(max_length=10)

    class Meta:
        db_table = 'resume'

    def __str__(self):
        return self.resume_listnum


class Job(models.Model):
    job_listnum = models.AutoField(primary_key=True)
    company_name = models.ForeignKey(MemberCompany, on_delete=models.CASCADE)
    job_title = models.CharField(max_length=100) 
    job_experience_required = models.CharField(max_length=20)
    job_education_required = models.CharField(max_length=30,)
    job_period = models.CharField(max_length=50)
    job_days_of_week = models.CharField(max_length=50)
    job_working_hours = models.CharField(max_length=50)
    job_salary = models.CharField(max_length=50)
    job_position = models.CharField(max_length=50)
    job_category = models.CharField(max_length=50)
    job_requirements = models.TextField()
    job_contact_number = models.CharField(max_length=50)
    job_email = models.CharField(max_length=50)
    job_deadline = models.CharField(max_length=8)

    class Meta:
        db_table = 'job'

    def __str__(self):
        return self.job_listnum


class Favorite (models.Model):
    favorite_listnum = models.AutoField(primary_key=True)
    personal_id = models.CharField(max_length=50)
    job_listnum = models.CharField(max_length=50)

    class Meta:
        db_table = 'favorite'

    def __str__(self):
        return self.favorite_listnum