# Generated by Django 5.0.1 on 2024-03-08 14:02

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ContestApp', '0007_solutionmodel_owner'),
    ]

    operations = [
        migrations.AddField(
            model_name='solutionmodel',
            name='passed_tests',
            field=models.IntegerField(default=0),
        ),
    ]