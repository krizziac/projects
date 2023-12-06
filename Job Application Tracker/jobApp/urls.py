from django.urls import path
from .views import jobAppList, interviewPrep, addJobApp, deleteJobApp, editJobApp, jobAppLogin
from django.contrib.auth.views import LogoutView

urlpatterns = [
    path('login/',jobAppLogin.as_view(), name = 'login'),
    path('logout/',LogoutView.as_view(next_page = 'login'), name = 'logout'),

    path('',jobAppList.as_view(), name = 'jobApp'),
    path('org/<int:pk>/',interviewPrep.as_view(), name = 'interviewJobApp'),
    path('add-jobApp/',addJobApp.as_view(), name = 'addJobApp'), 
    path('edit-jobApp/<int:pk>/',editJobApp.as_view(), name = 'editJobApp'),
    path('delete-jobApp/<int:pk>/',deleteJobApp.as_view(), name = 'deleteJobApp'),
]