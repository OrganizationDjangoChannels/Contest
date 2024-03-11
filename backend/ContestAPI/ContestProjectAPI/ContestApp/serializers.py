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
        fields = ('id', 'title', 'description', 'level', 'langs', 'owner', 'sent_solutions')
        read_only_fields = ('id', 'owner', 'sent_solutions')

    def create(self, validated_data):
        return TaskModel.objects.create(**validated_data, **self.context)


class TestSerializer(serializers.ModelSerializer):
    task = TaskSerializer(read_only=True)

    class Meta:
        model = TestModel
        fields = ('id', 'status', 'task', 'input', 'output', 'test_number')
        read_only_fields = ('id', 'task')

    def create(self, validated_data):
        return TestModel.objects.create(**validated_data, **self.context)


class SolutionSerializer(serializers.ModelSerializer):
    task = TaskSerializer(read_only=True)
    owner = ProfileSerializer(read_only=True)

    class Meta:
        model = SolutionModel
        fields = ('id', 'file', 'lang', 'points', 'status', 'task', 'owner', 'created_at', 'passed_tests')
        read_only_fields = ('id', 'task', 'owner', 'created_at', 'passed_tests')

    def create(self, validated_data):
        return SolutionModel.objects.create(**validated_data, **self.context)
