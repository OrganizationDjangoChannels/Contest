from django.shortcuts import render
from rest_framework.status import HTTP_200_OK
from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response


class TestAPIView(APIView):
    def get(self, request: Request) -> Response:
        return Response({'test_message': 'get_request'}, status=HTTP_200_OK)
