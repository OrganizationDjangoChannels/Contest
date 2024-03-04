import django.db.utils
from django.db import transaction
from rest_framework import serializers
from rest_framework.exceptions import ValidationError
from rest_framework.status import HTTP_400_BAD_REQUEST

from .models import SolutionModel, ProfileModel, TaskModel, TestModel
from django.contrib.auth.models import User


class CustomBaseSerializer(serializers.Serializer):
    created_at = serializers.DateTimeField(read_only=True)
    updated_at = serializers.DateTimeField(read_only=True)
    id = serializers.IntegerField(read_only=True, required=False)


class SolutionSerializer(serializers.ModelSerializer):
    class Meta:
        model = SolutionModel
        fields = ('file', 'lang')


class UserSerializer(CustomBaseSerializer):
    username = serializers.CharField(max_length=255)
    password = serializers.CharField(max_length=255, write_only=True)
    email = serializers.EmailField(max_length=255, required=False)

    def create(self, validated_data):
        with transaction.atomic():
            try:
                user = User.objects.create_user(**validated_data)
                profile = ProfileModel.objects.create(user=user)
            except django.db.utils.IntegrityError:
                raise ValidationError(code=HTTP_400_BAD_REQUEST, detail='The user already exists.')
        return user


class ProfileSerializer(serializers.ModelSerializer):
    user = UserSerializer()

    class Meta:
        model = ProfileModel
        fields = ('id', 'user', 'points', 'solved_tasks')
        read_only_fields = ('id', )


class TaskSerializer(serializers.ModelSerializer):
    owner = ProfileSerializer(read_only=True)

    class Meta:
        model = TaskModel
        fields = ('id', 'description', 'level', 'langs', 'owner')
        read_only_fields = ('id', 'owner')

    def create(self, validated_data):
        return TaskModel.objects.create(**validated_data, **self.context)


class TestSerializer(serializers.ModelSerializer):
    class Meta:
        model = TestModel
        fields = ('status', 'task', 'input', 'output')

    def create(self, validated_data):
        return TestModel.objects.create(**validated_data, **self.context)

