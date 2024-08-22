# Generated by Django 4.0.3 on 2023-09-08 12:48

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('keepwork', '0003_remove_membercompany_id_remove_memberpersonal_id_and_more'),
    ]

    operations = [
        migrations.CreateModel(
            name='Notice',
            fields=[
                ('notice_listnum', models.AutoField(primary_key=True, serialize=False)),
                ('notice_title', models.CharField(max_length=100)),
                ('notice_content', models.TextField(blank=True, null=True)),
                ('notice_date', models.CharField(max_length=10)),
            ],
            options={
                'db_table': 'notice',
            },
        ),
    ]
