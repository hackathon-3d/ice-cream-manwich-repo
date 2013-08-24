'''
Created on 8/23/2013

@author: Xander
'''
from BooksWithFriends.src.models import LibraryUser, MediaItem
from django.http import HttpResponse
import json

def new_user(request, name, phone, email=None):
    user = LibraryUser.objects.create_user(name, phone, email)
    user.save()
    if (user.pk):
        return HttpResponse(json.dumps({"user_id": user.pk}), content_type="application/json")
    
    return HttpResponse(json.dumps({"error_str": "Could Not create user"}), content_type="application/json")
    
def add_inventory_item(request, user_id, name, category=None ):
    user = LibraryUser.objects.get(pk=user_id)

    if (user.pk):
        item = MediaItem.objects.create_media_item(user, name, category)
        item.save()
        if (item.pk):
            return HttpResponse(json.dumps({"item_id": item.pk}), content_type="application/json")

        return HttpResponse(json.dumps({"error_str": "Could Not create item"}), content_type="application/json")
    else:
        return HttpResponse(json.dumps({"error_str": "Could Not find user"}), content_type="application/json")
    
def get_inventory(request, user_id):
#    Project.queries.get(project_id)
#
#    try:
#        member = ProjectMember.objects.get(project=project, user=user)
#    except ProjectMember.DoesNotExist:
#        member = None
