# Generated by Django 5.0.1 on 2024-02-11 09:45

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ContestApp', '0003_alter_attemptmodel_profile_alter_attemptmodel_task_and_more'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='profilemodel',
            name='username',
        ),
        migrations.RemoveField(
            model_name='testmodel',
            name='file',
        ),
        migrations.AddField(
            model_name='testmodel',
            name='input',
            field=models.CharField(max_length=255, null=True),
        ),
        migrations.AddField(
            model_name='testmodel',
            name='output',
            field=models.CharField(max_length=255, null=True),
        ),
    ]
