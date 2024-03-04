# Generated by Django 5.0.1 on 2024-02-14 11:19

import django.db.models.deletion
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ContestApp', '0004_remove_profilemodel_username_remove_testmodel_file_and_more'),
    ]

    operations = [
        migrations.AlterField(
            model_name='attemptmodel',
            name='profile',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='ContestApp.profilemodel'),
        ),
        migrations.AlterField(
            model_name='attemptmodel',
            name='task',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='ContestApp.taskmodel'),
        ),
        migrations.AlterField(
            model_name='taskmodel',
            name='owner',
            field=models.ForeignKey(null=True, on_delete=django.db.models.deletion.CASCADE, to='ContestApp.profilemodel'),
        ),
    ]