from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.status import HTTP_200_OK, HTTP_201_CREATED
from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response
from .serializers import SolutionSerializer


class TestAPIView(APIView):
    def get(self, request: Request) -> Response:
        return Response({'test_message': 'get_request'}, status=HTTP_200_OK)


class FileUploadView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request: Request) -> Response:
        serializer = SolutionSerializer(data=request.data)
        if serializer.is_valid():
            # uploaded_file = serializer.validated_data['file']

            serializer.save()
        return Response(serializer.data, status=HTTP_201_CREATED)

