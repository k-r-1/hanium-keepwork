# Generated by Django 4.0.3 on 2023-10-06 15:27

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('keepwork', '0018_alter_membercompany_company_name'),
    ]

    operations = [
        migrations.CreateModel(
            name='Favorite',
            fields=[
                ('favorite_listnum', models.AutoField(primary_key=True, serialize=False)),
                ('personal_id', models.CharField(max_length=50)),
                ('job_listnum', models.CharField(max_length=50)),
            ],
            options={
                'db_table': 'favorite',
            },
        ),
    ]
