from django.shortcuts import render
from django.views.generic.list import ListView
from django.views.generic.detail import DetailView
from django.views.generic.edit import CreateView, UpdateView, DeleteView
from django.urls import reverse_lazy

from django.contrib.auth.views import LoginView

#ensures only login page is shown when user is not logged in
from django.contrib.auth.mixins import LoginRequiredMixin

from .models import jobApp
# Create your views here.


class jobAppLogin(LoginView):
    template_name = 'jobApp/login.html'
    fields = '__all__'
    redirect_authenticated_user = True

    def get_success_url(self):
        return reverse_lazy('jobApp')

class jobAppList(LoginRequiredMixin,ListView):
    model = jobApp

   # when iterating through for-loop in HTML, the deault reference to a jobApp object is called "object" 
   #this function changes "object" -> "job"
    context_object_name ='job' 

    """
    Ensures data belongs to the user
    """
    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['job'] = context['job'].filter(user=self.request.user) 
        return context

#creates reference to a interview prep page
class interviewPrep(LoginRequiredMixin,DetailView):
    model = jobApp
    template_name = 'jobApp/jobapp_prep.html'

#creates a new "jobApp" object
class addJobApp(LoginRequiredMixin,CreateView):
    model = jobApp
    fields = '__all__'

    #upon "succes", this takes us back to the main page/ page that has this reference key
    success_url = reverse_lazy('jobApp')

class deleteJobApp(LoginRequiredMixin,DeleteView):
   model = jobApp

    #upon "succes", this takes us back to the main page/ page that has this reference key
   context_object_name ='job' 
   success_url = reverse_lazy('jobApp')

class editJobApp(LoginRequiredMixin,UpdateView):
    model = jobApp
    fields = '__all__'
    success_url = reverse_lazy('jobApp')

#1:08:16