from rest_framework import serializers

from .models import SolutionModel


class SolutionSerializer(serializers.ModelSerializer):
    class Meta:
        model = SolutionModel
        fields = ('file', 'lang')
