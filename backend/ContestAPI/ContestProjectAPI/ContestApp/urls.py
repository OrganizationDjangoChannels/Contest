from django.urls import path, include
from . import views

urlpatterns = [
    path('api/v1/uploadfile/', views.FileUploadView.as_view()),
    path('api/v1/check_auth/', views.TestAuthAPIView.as_view()),
    path('api/v1/register/', views.RegisterAPIView.as_view()),
    path('api/v1/login/', views.LoginAPIView.as_view()),
    path('api/v1/task/', views.TaskAPIView.as_view()),
    path('api/v1/test/', views.TestAPIView.as_view()),
    path('api/v1/auth/', include('rest_framework.urls', namespace='rest_framework')),

]

