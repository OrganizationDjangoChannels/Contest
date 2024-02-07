from django.urls import path
from . import views

urlpatterns = [
    path('api/v1/testview/', views.TestAPIView.as_view()),
    path('api/v1/uploadfile/', views.FileUploadView.as_view()),
]

