# Generated by Django 5.0.1 on 2024-02-03 13:48

import ContestApp.services.files
from django.db import migrations, models


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='SolutionModel',
            fields=[
                ('id', models.BigAutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('created_at', models.DateTimeField(auto_now_add=True)),
                ('updated_at', models.DateTimeField(auto_now=True)),
                ('file', models.FileField(null=True, upload_to=ContestApp.services.files.upload_program_path)),
                ('lang', models.CharField(max_length=255, null=True)),
            ],
            options={
                'db_table': 'solution',
            },
        ),
    ]