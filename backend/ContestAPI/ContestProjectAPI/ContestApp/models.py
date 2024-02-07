from django.db import models
from .services.files import upload_program_path
from django.contrib.auth.models import User

TEST_STATUSES = (
    ('passed', 'passed'),
    ('failed', 'failed'),
)


class BaseModel(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    objects = models.Manager
    DoesNotExist = models.Manager

    class Meta:
        abstract = True


class ProfileModel(BaseModel):
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    username = models.CharField(max_length=32, null=True)
    points = models.IntegerField(default=0)
    solved_tasks = models.IntegerField(default=0)

    class Meta:
        db_table = 'profile'


class TaskModel(BaseModel):
    description = models.TextField(null=True)
    level = models.IntegerField(default=0)
    langs = models.CharField(max_length=255, null=True)
    owner = models.OneToOneField(ProfileModel, on_delete=models.CASCADE, null=True)

    class Meta:
        db_table = 'task'


class SolutionModel(BaseModel):
    file = models.FileField(upload_to=upload_program_path, null=True)
    lang = models.CharField(max_length=255, null=True)
    task = models.ForeignKey(TaskModel, on_delete=models.CASCADE, null=True)
    points = models.IntegerField(default=0)
    status = models.CharField(max_length=6, choices=TEST_STATUSES, null=True)

    class Meta:
        db_table = 'solution'


class AttemptModel(BaseModel):
    task = models.OneToOneField(TaskModel, on_delete=models.CASCADE, null=True)
    profile = models.OneToOneField(ProfileModel, on_delete=models.CASCADE, null=True)
    attempt_number = models.IntegerField(default=0)

    class Meta:
        db_table = 'attempt'


class TestModel(BaseModel):
    task = models.ForeignKey(TaskModel, on_delete=models.CASCADE, null=True)
    status = models.CharField(max_length=6, choices=TEST_STATUSES, null=True)
    file = models.FileField(upload_to=upload_program_path, null=True)

    class Meta:
        db_table = 'test'
