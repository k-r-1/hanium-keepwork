from django.shortcuts import render, get_object_or_404
from rest_framework import generics, status
from rest_framework.views import APIView
from rest_framework.response import Response
from .models import MemberPersonal, MemberCompany, Notice, Resume, Job, Favorite
from .serializers import MemberPersonalSerializer, MemberCompanySerializer, NoticeSerializer, ResumeSerializer, JobSerializer, FavoriteSerializer


class MemberPersonalList(APIView):
    # def get(self, request):
    #     p_members = MemberPersonal.objects.all()
    #     serializer = MemberPersonalSerializer(p_members, many=True)
    #     return Response(serializer.data)

    def get(self, request, personal_id):
        try:
            p_member = MemberPersonal.objects.get(personal_id=personal_id)
        except MemberPersonal.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

        serializer = MemberPersonalSerializer(p_member)
        return Response(serializer.data)

    def post(self, request):
        serializer = MemberPersonalSerializer(data=request.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class MemberCompanyList(APIView):
    # def get(self, request):
    #     c_members = MemberCompany.objects.all()
    #     serializer = MemberCompanySerializer(c_members, many=True)
    #     return Response(serializer.data)

    def get(self, request, company_id):
        try:
            c_member = MemberCompany.objects.get(company_id=company_id)
        except MemberCompany.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

        serializer = MemberCompanySerializer(c_member)
        return Response(serializer.data)

    def post(self, request):
        serializer = MemberCompanySerializer(data=request.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        
class NoticeList(APIView):
    def get(self, request):
        notices = Notice.objects.all()
        serializer = NoticeSerializer(notices, many=True)
        return Response(serializer.data)


#class ResumeList(APIView):
#    def get(self, request):
#        resumes = Resume.objects.all()
#        serializer = ResumeSerializer(resumes, many=True)
#        return Response(serializer.data)
#
#    def post(self, request):
#        serializer = ResumeSerializer(data=request.data)
#        if serializer.is_valid(raise_exception=True):
#            serializer.save()
#            return Response(serializer.data, status=status.HTTP_201_CREATED)
#        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#def profile_view(request):
#    if 'userId' in request.session:
#        userId = request.session['userId']
#        # user_id를 사용하여 현재 로그인한 사용자에 대한 작업을 수행
#        # 예: 사용자 이력서 조회 등
#    else:
#        # 사용자가 로그인하지 않은 경우 처리
#        pass
#위 함수는 personal_id, company_id를 인자로 받지 않고, userId를 파라미터로 받을 때 사용하는 함수

class ResumeList(APIView):
    def get(self, request):
         # 사용자 이름 가져오기 (이 부분은 SharedPreferences를 통해 사용자 이름을 얻는 방식으로 진행)
        userId = request.GET.get('personal_id', '')
        try:
            resumes = Resume.objects.filter(personal_id_id=userId)
        except Resume.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        #try:
        #    resumes = Resume.objects.filter(personal_id=personal_id)
        #except Resume.DoesNotExist:
        #    return Response(status=status.HTTP_404_NOT_FOUND) # => 파라미터로 personal_id를 가져올때 

        serializer = ResumeSerializer(resumes, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = ResumeSerializer(data=request.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ResumeDelete(APIView):
    def delete_resume(self, request, personal_id, resume_listnum):
        # personal_id와 resume_listnum을 사용하여 삭제할 이력서를 가져옵니다.
        resume = get_object_or_404(Resume, personal_id=personal_id, resume_listnum=resume_listnum)
    
        # 이력서 삭제
        resume.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)



# class JobList(APIView):
#     def get(self, request, company_name=None):
#         if company_name:
#             jobs = get_object_or_404(Job, company_name=company_name)
#             serializer = JobSerializer(jobs)
#         else:
#             jobs = Job.objects.all()
#             serializer = JobSerializer(jobs, many=True)
#         return Response(serializer.data)

#     def post(self, request):
#         serializer = JobSerializer(data=request.data)
#         if serializer.is_valid(raise_exception=True):
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

# # JobList 클래스 수정
# class JobList(APIView):
#     def get(self, request):
#         # 사용자 이름 가져오기 (이 부분은 SharedPreferences를 통해 사용자 이름을 얻는 방식으로 진행)
#         userName = request.GET.get('userName', '')  # userName을 GET 파라미터로 받음

#         # userName과 jobPosting의 company_name을 비교하여 해당 사용자의 공고만 가져옴
#         jobs = Job.objects.filter(company_name=userName)
#         serializer = JobSerializer(jobs, many=True)
#         return Response(serializer.data)

#     def post(self, request):
#         serializer = JobSerializer(data=request.data)
#         if serializer.is_valid(raise_exception=True):
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

# JobList 클래스 수정2
# class JobList(APIView):
#     def get(self, request):
#         # 사용자 이름 가져오기 (이 부분은 SharedPreferences를 통해 사용자 이름을 얻는 방식으로 진행)
#         userName = request.GET.get('userName', '')  # userName을 GET 파라미터로 받음

#         # userName과 jobPosting의 company_name을 비교하여 해당 사용자의 공고만 가져옴
#         jobs = Job.objects.filter(company_name=userName)
#         serializer = JobSerializer(jobs, many=True)
#         return Response(serializer.data)

#     def post(self, request):
#         serializer = JobSerializer(data=request.data)
#         if serializer.is_valid(raise_exception=True):
#             serializer.save()
#             return Response(serializer.data, status=status.HTTP_201_CREATED)
#         return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

#     def delete(self, request, listnum):
#         try:
#             job = Job.objects.get(job_listnum=listnum)
#             job.delete()
#             return Response(status=status.HTTP_204_NO_CONTENT)
#         except Job.DoesNotExist:
#             return Response(status=status.HTTP_404_NOT_FOUND)
        
# JobList 클래스 수정3
class JobList(APIView):
    def get(self, request):
        # 사용자 이름 가져오기 (이 부분은 SharedPreferences를 통해 사용자 이름을 얻는 방식으로 진행)
        userName = request.GET.get('userName', '')  # userName을 GET 파라미터로 받음

        # userName과 jobPosting의 company_name을 비교하여 해당 사용자의 공고만 가져옴
        jobs = Job.objects.filter(company_name=userName)
        serializer = JobSerializer(jobs, many=True)
        return Response(serializer.data)

    def post(self, request):
        serializer = JobSerializer(data=request.data)
        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    def delete(self, request, listnum):
        try:
            job = Job.objects.get(job_listnum=listnum)
            job.delete()
            return Response(status=status.HTTP_204_NO_CONTENT)
        except Job.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)
        
    def put(self, request, listnum):
        try:
            job = Job.objects.get(job_listnum=listnum)
            serializer = JobSerializer(job, data=request.data)
            if serializer.is_valid(raise_exception=True):
                serializer.save()
                return Response(serializer.data, status=status.HTTP_200_OK)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        except Job.DoesNotExist:
            return Response(status=status.HTTP_404_NOT_FOUND)

class AllJobsList(APIView):
    def get(self, request):
        # 모든 Job 데이터 가져오기
        jobs = Job.objects.all()
        serializer = JobSerializer(jobs, many=True)
        return Response(serializer.data)
        
class FavoriteList(APIView):
    def get(self, request):
        favorites = Favorite.objects.all()
        serializer = FavoriteSerializer(favorites, many=True)
        return Response(serializer.data)
