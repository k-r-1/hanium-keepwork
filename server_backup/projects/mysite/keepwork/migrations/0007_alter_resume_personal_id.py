# Generated by Django 4.0.3 on 2023-09-10 14:39

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('keepwork', '0006_resume'),
    ]

    operations = [
        migrations.AlterField(
            model_name='resume',
            name='personal_id',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='keepwork.memberpersonal'),
        ),
    ]
