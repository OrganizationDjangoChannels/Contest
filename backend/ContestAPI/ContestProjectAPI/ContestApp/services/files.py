from time import time


def upload_program_path(instance, filename: str) -> str:
    return f'files/{time()}/{filename}'
