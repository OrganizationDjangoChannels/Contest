import subprocess
import os

from django.conf import settings
from rest_framework.authtoken.views import ObtainAuthToken
from rest_framework.parsers import MultiPartParser, FormParser
from rest_framework.permissions import IsAuthenticated
from rest_framework.status import HTTP_200_OK, HTTP_201_CREATED, HTTP_400_BAD_REQUEST, HTTP_404_NOT_FOUND
from rest_framework.views import APIView
from rest_framework.request import Request
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from rest_framework.exceptions import APIException
from django.db.models import Max

from .models import SolutionModel, ProfileModel, TaskModel, TestModel
from .serializers import SolutionSerializer, UserSerializer, TaskSerializer, TestSerializer, ProfileSerializer
from .services.files import get_cmd_commands_for_c_file, get_cmd_command, run_test, get_solution_status

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

    def post(self, request, *args, **kwargs) -> Response:
        serializer = self.serializer_class(data=request.data,
                                           context={'request': request})
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data['user']
        token = Token.objects.get(user=user)
        return Response({
            'token': token.key,
        }, status=HTTP_200_OK)


class ProfileAPIView(APIView):
    def get(self, request: Request, profile_id=None) -> Response:

        if request.user.is_authenticated and profile_id is None:  # get profile via token
            profile = ProfileModel.objects.get(user=request.user)
            response = Response(ProfileSerializer(profile).data)

        elif profile_id is None:  # get 10 profiles
            profiles = ProfileModel.objects.all().order_by('id')[0: 10]
            response = Response(ProfileSerializer(profiles, many=True).data)
        else:  # get profile by id
            try:
                profile = ProfileModel.objects.get(id=profile_id)
                solutions = SolutionModel.objects\
                    .filter(owner=profile, status='solved')\
                    .distinct('task', 'created_at')\
                    .order_by('-created_at')
                tasks_ids_array = []
                for solution in solutions:
                    tasks_ids_array.append(solution.task.id)
                tasks = TaskModel.objects.filter(id__in=tasks_ids_array).distinct()

                response = Response(
                    {
                        'profile': ProfileSerializer(profile).data,
                        'my_solutions': SolutionSerializer(solutions, many=True).data,
                        'solved_tasks': TaskSerializer(tasks, many=True).data,
                    }
                )

            except ProfileModel.DoesNotExist:
                response = Response({'message': 'The item does not exist'}, status=HTTP_404_NOT_FOUND)

        return response


class TestAuthAPIView(APIView):
    permission_classes = (IsAuthenticated,)

    def post(self, request: Request) -> Response:
        profile = ProfileModel.objects.get(user=request.user)

        return Response({
                         'user_id': profile.user.id,
                         'username': profile.user.username,
                         'profile': ProfileSerializer(profile).data,
                         },
                        status=HTTP_200_OK)


class TaskAPIView(APIView):

    def get(self, request: Request, task_id=None) -> Response:
        if task_id is None:
            by_myself = request.GET.get('by_myself', None)
            if by_myself == '1':
                profile = ProfileModel.objects.get(user=request.user)
                tasks = TaskModel.objects.filter(owner=profile).order_by('-id')[0: 50]
                response = Response(TaskSerializer(tasks, many=True).data)
            else:
                tasks = TaskModel.objects.all().order_by('-id')[0: 50]
                response = Response(TaskSerializer(tasks, many=True).data)

        else:
            try:
                task = TaskModel.objects.get(id=task_id)
                response = Response(TaskSerializer(task).data)
            except TaskModel.DoesNotExist:
                response = Response({'message': 'The item does not exist'}, status=HTTP_404_NOT_FOUND)

        return response

    def post(self, request: Request) -> Response:
        profile = ProfileModel.objects.get(user=request.user)
        serializer = TaskSerializer(data=request.data, context={'owner': profile})

        if serializer.is_valid(raise_exception=True):
            serializer.save()
            return Response(serializer.data, status=HTTP_201_CREATED)


class TestAPIView(APIView):
    def post(self, request: Request) -> Response:
        task = TaskModel.objects.get(id=request.data['task_id'])
        tests_created_number = 0
        for test in request.data['tests']:
            if test['input'] is not None and test['output'] is not None:
                serializer = TestSerializer(data=test, context={'task': task})
                if serializer.is_valid(raise_exception=True):
                    serializer.save()
                    tests_created_number += 1

        return Response({'tests_created': tests_created_number}, status=HTTP_201_CREATED)

    def get(self, request: Request) -> Response:
        task_id = request.GET.get('task_id', None)
        if task_id is None:
            tests = TestModel.objects.all().order_by('id')[0: 100]
            response = Response(TestSerializer(tests, many=True).data)
        else:
            tests = TestModel.objects.filter(task__id=task_id)
            response = Response(TestSerializer(tests, many=True).data)

        return response


class SolutionAPIView(APIView):
    parser_classes = (MultiPartParser, FormParser)

    def get(self, request: Request) -> Response:
        task_id = request.GET.get('task_id', None)
        profile = ProfileModel.objects.get(user=request.user)

        if task_id is None:
            solutions = SolutionModel.objects.all().order_by('-id')[0: 100]
            response = Response(SolutionSerializer(solutions, many=True).data)

        else:
            solutions = SolutionModel.objects.filter(task__id=task_id).order_by('-id')[0: 100]
            response = Response(SolutionSerializer(solutions, many=True).data)

        return response

    def post(self, request: Request) -> Response:
        task = TaskModel.objects.get(id=request.data['task_id'])
        profile = ProfileModel.objects.get(user=request.user)
        serializer = SolutionSerializer(data=request.data, context={'task': task, 'owner': profile})
        if serializer.is_valid(raise_exception=True):
            solution = SolutionModel(**serializer.validated_data)
            solution.save()
            file = solution.file
            lang = solution.lang

            tests = TestModel.objects.filter(task=task)
            tests_count = tests.count()
            passed_tests = 0

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
                    print('build error')
                    raise APIException(code=HTTP_400_BAD_REQUEST, detail=error, )

                for test in tests:
                    passed_tests += run_test(test=test, compile_command=compile_command, environment=environment)

            else:
                compile_command = get_cmd_command(str(file), str(lang))
                for test in tests:
                    passed_tests += run_test(test=test, compile_command=compile_command)

            task.sent_solutions += 1
            task.save()

            my_solution_max_points = SolutionModel.objects.filter(
                owner=profile, task=task).aggregate(Max('points'))['points__max']

            solution.passed_tests = passed_tests
            solution.points = tests_count / passed_tests * 100 * task.level
            solution.status = get_solution_status(solution.points, task.level)
            solution.task = task
            solution.owner = profile
            solution.save()

            if my_solution_max_points:
                if solution.points > my_solution_max_points:
                    profile.points += (solution.points - my_solution_max_points)
                    profile.save()
            else:
                profile.points += solution.points
                profile.save()

            return Response(
                {**serializer.data, 'passed_tests': passed_tests, 'points': solution.points},
                status=HTTP_201_CREATED
            )


class RatingsAPIView(APIView):
    def get(self, request: Request) -> Response:
        profiles = ProfileModel.objects.all().order_by('-points', 'id')[0:50]
        response = Response(ProfileSerializer(profiles, many=True).data)
        return response
