import subprocess
import os

from django.conf import settings
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.status import HTTP_200_OK, HTTP_201_CREATED, HTTP_400_BAD_REQUEST
from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response
from rest_framework.authtoken.models import Token

from .models import SolutionModel, ProfileModel, TaskModel
from .serializers import SolutionSerializer, UserSerializer, TaskSerializer, TestSerializer
from .services.files import get_cmd_commands_for_c_file, get_cmd_command

C_BIN_PATH = settings.C_BIN_PATH


class RegisterAPIView(APIView):
    def post(self, request: Request) -> Response:
        user_serializer = UserSerializer(data=request.data)
        if user_serializer.is_valid():
            user = user_serializer.save()
            token, created = Token.objects.get_or_create(user=user)
            return Response({'token': token.key}, status=HTTP_201_CREATED)

        return Response(status=HTTP_400_BAD_REQUEST)


class LoginAPIView(ObtainAuthToken):

    def post(self, request, *args, **kwargs):
        serializer = self.serializer_class(data=request.data,
                                           context={'request': request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token = Token.objects.get(user=user)
        return Response({
            'token': token.key,
        }, status=HTTP_200_OK)


class TestAuthAPIView(APIView):
    permission_classes = (IsAuthenticated, )

    def post(self, request: Request) -> Response:
        print(request.user.username)
        profile = ProfileModel.objects.get(user=request.user)
        print(profile)

        return Response({'user_id': profile.user.id}, status=HTTP_200_OK)


class FileUploadView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def post(self, request: Request) -> Response:
        print(f'user = {request.user}')
        serializer = SolutionSerializer(data=request.data)
        if serializer.is_valid():
            file = serializer.validated_data['file']
            lang = serializer.validated_data['lang']

            solution = SolutionModel(file=file, lang=lang, )
            solution.save()
            file = solution.file

            if lang == 'C' or lang == 'C++':
                environment = os.environ.copy()
                environment["PATH"] = C_BIN_PATH
                commands = get_cmd_commands_for_c_file(str(file), str(lang), ).split(';')

                build_command = commands[0]
                compile_command = commands[1]

                run_command = subprocess.Popen(build_command,
                                               shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                               env=environment, )
                output, error = run_command.communicate()
                if run_command.returncode != 0:
                    print(error)
                    print(f'returncode: {run_command.returncode}')

                run_command = subprocess.Popen(compile_command,
                                               shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                               stdin=subprocess.PIPE, env=environment, )
                input_data = b'4\n1\n2\n3\n4\n'
                output, error = run_command.communicate(input=input_data)

                if run_command.returncode != 0:
                    print(error)
                    print(f'returncode: {run_command.returncode}')

                print(output.decode('utf-8'))

            else:

                run_command = subprocess.Popen(get_cmd_command(str(file), str(lang)),
                                               shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                               stdin=subprocess.PIPE, )
                input_data = b'4\n1\n2\n3\n4\n'
                output, error = run_command.communicate(input=input_data)

                if run_command.returncode != 0:
                    print(error)
                    print(f'returncode: {run_command.returncode}')

                print(output.decode('utf-8'))

        return Response(serializer.data, status=HTTP_201_CREATED)


class TaskAPIView(APIView):

    def post(self, request: Request) -> Response:
        profile = ProfileModel.objects.get(user=request.user)
        serializer = TaskSerializer(data=request.data, context={'owner': profile})

        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=HTTP_201_CREATED)


class TestAPIView(APIView):
    def post(self, request: Request) -> Response:
        task = TaskModel.objects.get(id=request.data['task_id'])
        serializer = TestSerializer(data=request.data, context={'task': task})

        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=HTTP_201_CREATED)