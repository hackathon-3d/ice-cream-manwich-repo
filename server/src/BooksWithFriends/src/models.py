'''
Created on 8/23/2013

@author: Xander
'''

from django.db import models
    
class LibraryUserManager(models.Manager):
    def create_user(self, name, phone, email=None):
        user=self.create(name=name, phone=name, email=email)
        return user

class LibraryUser(models.Model):
    name=models.CharField(max_length=100, blank=False)
    email=models.CharField(max_length=100, blank=True)
    phone = models.CharField(blank=False, max_length=20)
    
    objects=LibraryUserManager()
    
class MediaItemManager(models.Model):
    def create_media_item(self, user, name, category=None):
        mediaItem=self.create(user=user, name=name, category=category)
        return mediaItem
    
class MediaItem(models.Model):
    user=models.ForeignKey(LibraryUser)
    name = models.CharField(max_length=100, blank=False)
    category=models.CharField(max_length=50, blank=False)
    
    objects=MediaItemManager()
    
class LoanItemManager(models.Model):
    def create_loan_item(self, item, dueDate):
        loanItem=self.create(item=item, dueDate=dueDate)
        return loanItem

class LoanItem(models.Model):
    item=models.ForeignKey(MediaItem)
    loanDate=models.DateField(auto_now_add=True)
    dueDate = models.DateField(max_length=50, blank=True)

    objects=LoanItemManager()
    
class BorrowItemManager(models.Model):
    def create_borrow_item(self, item, dueDate):
        borrowItem=self.create(item=item, dueDate=dueDate)
        return borrowItem

class BorrowItem(models.Model):
    item=models.ForeignKey(MediaItem)
    loanDate=models.DateField(auto_now_add=True)
    dueDate = models.DateField(max_length=50, blank=True)
    
    objects=BorrowItemManager()
    
