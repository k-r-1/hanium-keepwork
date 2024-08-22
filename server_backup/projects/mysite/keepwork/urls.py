from django.urls import path, include
from .views import MemberPersonalList, MemberCompanyList, NoticeList, ResumeList, JobList, FavoriteList, ResumeDelete, AllJobsList
from . import views
# from . import recommend

urlpatterns = [
    path('member_personal/<str:personal_id>/', MemberPersonalList.as_view()),
    path('member_company/<str:company_id>/', MemberCompanyList.as_view()),
    path('notice/', NoticeList.as_view()),
    path('resume/', ResumeList.as_view()),
    path('resume/<str:personal_id>/', ResumeList.as_view()),
    path('resume/<str:personal_id>/<int:resume_listnum>/', ResumeDelete.as_view()),
   # path('resume/<str:company_id>/', ResumeList.as_view()),
   # path('resume/<str:identifier>/', ResumeList.as_view()),
   # path('resume/personal/<str:personal_id>/', ResumeList.as_view()),
   # path('resume/company/<str:company_id>/', ResumeList.as_view()),
    path('job/', JobList.as_view()),
    path('job/<int:listnum>/', JobList.as_view()),
    path('favorite/', FavoriteList.as_view()),
    path('job/all/', AllJobsList.as_view()),  # 모든 Job 데이터를 가져오는 엔드포인트
    # path('api/get_recommendations/<str:user_id>/', recommend.recommendations_api, name='recommendations_api'),
]
