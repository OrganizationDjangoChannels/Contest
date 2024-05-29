FROM python:3.10

WORKDIR /code

COPY . /code/

EXPOSE 8000

ENV SECRET_KEY=mgr-*+qay&vk&t^ft6%vh_8so!oe8)3i+*mcir8%$%r$^gfj61 \
    PYTHON_COMPILER_PATH=python-3.12.1\python.exe \
    JAVA_COMPILER_PATH=jdk-21.0.1\bin\java.exe \
    C_COMPILER_PATH=g++-12.2.0/bin/gcc.exe \
    C_PLUS_PLUS_COMPILER_PATH=g++-12.2.0/bin/g++.exe \
    C_BIN_PATH=g++-12.2.0/bin \
    DATABASE_NAME=ContestDatabase \
    DATABASE_USER=postgres \
    DATABASE_PASSWORD=228Gtnhjd228 \
    DATABASE_HOST=127.0.0.1 \
    DATABASE_PORT=5432

RUN pip install -r requirements.txt

CMD cd ContestProjectAPI && \
    python manage.py migrate && \
    python manage.py runserver 0.0.0.0:8000



