from ..models import TaskModel
from ..serializers import TestSerializer


def create_test(test, task: TaskModel) -> None:
    print(test)
    if test['input'] is not None and test['output'] is not None:
        serializer = TestSerializer(data=test, context={'task': task})
        if serializer.is_valid(raise_exception=True):
            serializer.save()
