# Generated by Django 5.0.1 on 2024-03-08 20:00

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('ContestApp', '0010_alter_solutionmodel_points'),
    ]

    operations = [
        migrations.AddField(
            model_name='taskmodel',
            name='sent_solutions',
            field=models.IntegerField(default=0),
        ),
    ]
