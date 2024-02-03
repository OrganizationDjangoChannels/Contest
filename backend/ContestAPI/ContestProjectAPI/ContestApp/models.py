from django.db import models
from .services.files import upload_program_path


class BaseModel(models.Model):
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    objects = models.Manager
    DoesNotExist = models.Manager

    class Meta:
        abstract = True


class SolutionModel(BaseModel):
    file = models.FileField(upload_to=upload_program_path, null=True)
    lang = models.CharField(max_length=255, null=True)

    class Meta:
        db_table = 'solution'

