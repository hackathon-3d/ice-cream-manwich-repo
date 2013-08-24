'''
Created on 8/23/2013

@author: Xander
'''

from django.db import models
    
class LibraryUserManager(models.Manager):
    def create_user(self, name, phone, email=None):
        user=self.create(name=name, phone=phone, email=email)
        return user

class LibraryUser(models.Model):
    name=models.CharField(max_length=100, blank=False)
    email=models.CharField(max_length=100, blank=True)
    phone = models.CharField(blank=False, max_length=20)
    
    objects=LibraryUserManager()
    
class MediaItemManager(models.Manager):
    def create_media_item(self, user, name, category=None):
        mediaItem=self.create(user=user, name=name, category=category)
        return mediaItem
    
class MediaItem(models.Model):
    user=models.ForeignKey(LibraryUser)
    name = models.CharField(max_length=100, blank=False)
    category=models.CharField(max_length=50, blank=False)
    
    objects=MediaItemManager()
    
class LoanItemManager(models.Manager):
    def create_loan_item(self, loanedFrom, loanedTo, item, dueDate):
        loanItem=self.create(loanedFrom=loanedFrom, loanedTo=loanedTo, item=item, dueDate=dueDate)
        return loanItem

class LoanItem(models.Model):
    loanedFrom=models.ForeignKey(LibraryUser, related_name='loaned_from')
    loanedTo=models.ForeignKey(LibraryUser, related_name='loaned_to')
    item=models.ForeignKey(MediaItem)
    loanDate=models.DateField(auto_now_add=True)
    dueDate=models.DateField(blank=True)

    objects=LoanItemManager()
    
class FriendConnectionManager(models.Manager):
    def create_friend(self, friend1, friend2):
        connection=self.create(user1=friend1, user2=friend2)
        return connection

class FriendConnection(models.Model):
    user1 = models.ForeignKey(LibraryUser, related_name='connections')
    user2 = models.ForeignKey(LibraryUser, related_name='reverse_connections')

    objects=FriendConnectionManager()
