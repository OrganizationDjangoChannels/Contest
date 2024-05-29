import os
import subprocess
from time import time
from django.conf import settings
from pathlib import Path

PROGRAMMING_LANGS = {
    'C': 'C',
    'C++': 'C++',
    'Java': 'Java',
    'Python': 'Python',
}

PYTHON_PATH = settings.PYTHON_COMPILER_LOCAL_PATH

JAVA_JDK_PATH = settings.JAVA_COMPILER_LOCAL_PATH

C_PLUS_PLUS_PATH = settings.C_PLUS_PLUS_COMPILER_LOCAL_PATH
C_PATH = settings.C_COMPILER_LOCAL_PATH


def upload_program_path(instance, filename: str) -> str:
    return f'files/{time()}/{filename}'


def get_cmd_command(path_to_file: str, lang: str) -> str:
    full_path_to_file = os.path.join(settings.MEDIA_ROOT, path_to_file)

    if lang == 'Python':
        return f'{PYTHON_PATH} {full_path_to_file}'

    if lang == 'Java':
        return f'{JAVA_JDK_PATH} {full_path_to_file}'


def get_cmd_commands_for_c_file(path_to_file: str, lang: str) -> str:
    full_path_to_file = os.path.join(settings.MEDIA_ROOT, path_to_file)
    full_path_to_exe_file = Path(full_path_to_file).with_suffix('.exe')

    if lang == 'C++':
        return (f'{C_PLUS_PLUS_PATH} {full_path_to_file} -o {Path(full_path_to_file).with_suffix("")};'
                f'{full_path_to_exe_file}')

    if lang == 'C':
        return (f'{C_PATH} {full_path_to_file} -o {Path(full_path_to_file).with_suffix("")};'
                f'{full_path_to_exe_file}')


def run_test(test, compile_command: str, environment=None) -> int:
    run_command = subprocess.Popen(compile_command,
                                   shell=True, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                   stdin=subprocess.PIPE, env=environment, )
    input_data = test.input.encode('utf-8')

    try:
        output = subprocess.check_output(compile_command, stderr=subprocess.STDOUT, timeout=2, input=input_data,
                                         shell=True, env=environment, )
        print(f'{output = }')
        # output, error = run_command.communicate(input=input_data)
    except subprocess.TimeoutExpired as ex:
        print(ex)
        return 0
    except subprocess.CalledProcessError as ex:
        print(ex)
        return -1
    except BaseException as ex:
        print(ex)
        return -1

    if output.rstrip() == test.output.encode('utf-8'):
        print(f'test #{test.test_number} passed')
        return 1
    else:
        print(f'test #{test.test_number} failed')
        return 0


def get_solution_status(points: int | float, level: int) -> str:
    if points == 0:
        return 'failed'
    if 0 < points < 100 * level:
        return 'partial'
    if points == 100 * level:
        return 'solved'
