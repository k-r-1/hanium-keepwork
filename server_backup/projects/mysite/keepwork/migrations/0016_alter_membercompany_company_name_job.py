# Generated by Django 4.0.3 on 2023-09-16 14:57

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('keepwork', '0015_delete_job'),
    ]

    operations = [
        migrations.AlterField(
            model_name='membercompany',
            name='company_name',
            field=models.CharField(default='', max_length=50, unique=True),
        ),
        migrations.CreateModel(
            name='Job',
            fields=[
                ('job_listnum', models.AutoField(primary_key=True, serialize=False)),
                ('job_title', models.CharField(max_length=100)),
                ('job_experience_required', models.CharField(max_length=20)),
                ('job_education_required', models.CharField(max_length=30)),
                ('job_period', models.CharField(max_length=50)),
                ('job_days_of_week', models.CharField(max_length=50)),
                ('job_working_hours', models.CharField(max_length=50)),
                ('job_salary', models.CharField(max_length=50)),
                ('job_position', models.CharField(max_length=50)),
                ('job_category', models.CharField(max_length=50)),
                ('job_requirements', models.TextField()),
                ('job_contact_number', models.CharField(max_length=50)),
                ('job_email', models.CharField(max_length=50)),
                ('job_deadline', models.CharField(max_length=8)),
                ('company_name', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='keepwork.membercompany')),
            ],
            options={
                'db_table': 'job',
            },
        ),
    ]
