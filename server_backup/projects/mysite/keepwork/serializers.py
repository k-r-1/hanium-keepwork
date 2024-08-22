from rest_framework import serializers
from .models import MemberPersonal, MemberCompany, Notice, Resume, Job, Favorite

class MemberPersonalSerializer(serializers.ModelSerializer):
    class Meta:
        model = MemberPersonal
        fields = '__all__'

class MemberCompanySerializer(serializers.ModelSerializer):
    class Meta:
        model = MemberCompany
        fields = '__all__'

class NoticeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Notice
        fields = '__all__'

class ResumeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Resume
        fields = '__all__'

class JobSerializer(serializers.ModelSerializer):
    class Meta:
        model = Job
        fields = '__all__'

class FavoriteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Favorite
        fields = '__all__'
