from django.db import models
from django.contrib.auth.models import User
# Create your models here.

"""
models represent a table in databases
variables represent the attributes/columns within the table
"""

progress = (
    ('submitted', 'Application Submitted'),
    ('interview', 'Interviewing'),
    ('noRes','No Response'),
    ('reject', 'Rejected'),
    ('offer','Offer Given'),
    ('accepted','Offer Accepted'),
    ('offerDec','Offer Declined'),
)

class jobApp(models.Model):
    user = models.ForeignKey(User, on_delete = models.CASCADE, null = True, blank = True)
    companyName = models.CharField(max_length = 25)
    jobTitle = models.CharField(max_length = 200)
    interviewPrep = models.TextField(null= True, blank = True)
    appProgress = models.CharField(max_length = 21,choices = progress, default = 'submitted' )


    def __str__(self):
        return self.companyName
    
    """
    When returning multiple items based on some query, we return based 
    on the company name
    """
    class Meta:
        ordering=['companyName']