'''
Created on 8/23/2013

@author: Xander
'''

from django.db import models

class BarcodeMedia(models.Model):
    name = models.CharField(max_length=100, blank=False)
    dueDate = models.DateField(max_length=50, blank=True)