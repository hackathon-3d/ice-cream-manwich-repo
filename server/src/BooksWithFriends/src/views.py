'''
Created on 8/23/2013

@author: Xander
'''
from BooksWithFriends.src.models import LibraryUser, MediaItem, LoanItem
from django.http import HttpResponse
import json

def new_user(request):
    if request.method == "POST":
        name = request.POST["name"]
        phone = request.POST["phone"]
        email = request.POST["email"]

        user = LibraryUser.objects.create_user(name, phone, email)
        user.save()
        if (user.pk):
            return HttpResponse(json.dumps({"user_id": user.pk}), content_type="application/json")
    
    return HttpResponse(json.dumps({"error_str": "Could Not create user"}), content_type="application/json")
    
def add_inventory_item(request, user_id ):
    user = LibraryUser.objects.get(pk=user_id)

    name = request.POST["name"]
    category = request.POST["category"]

    if (user.pk):
        item = MediaItem.objects.create_media_item(user, name, category)
        item.save()
        if (item.pk):
            return HttpResponse(json.dumps({"item_id": item.pk}), content_type="application/json")

        return HttpResponse(json.dumps({"error_str": "Could Not create item"}), content_type="application/json")

    return HttpResponse(json.dumps({"error_str": "Could Not find user"}), content_type="application/json")
    
def get_inventory_item(request, item_id):
    item = MediaItem.objects.get(pk=item_id)
    if (item.pk):
        return HttpResponse(json.dumps({"name": item.name,
                                        "category": item.category,
                                        "item_id": item.pk}),
                                         content_type="application/json")

    return HttpResponse(json.dumps({"error_str": "Could Not find item"}), content_type="application/json")

def get_inventory_all(request, user_id):
    inventory_items = MediaItem.objects.filter(user__pk=user_id)
    
    if (inventory_items):
        list = []
        for item in inventory_items:
            isLoaned = LoanItem.objects.get(item=item.pk)
            list.append({"name": item.name,
                         "category": item.category,
                         "item_id": item.pk})
            if (isLoaned.pk):
                list.append({"is_loaned": True,
                             "loan_to_user_id": isLoaned.user.pk})
            else:
                list.append({"is_loaned": False})
            
        return HttpResponse(json.dumps(list), content_type="application/json")

    return HttpResponse(json.dumps({"error_str": "Could Not find inventory items"}), content_type="application/json")

def get_all_loaned_items(request, user_id):
    user = LibraryUser.objects.get(pk=user_id)
    
    if (user.pk):
        loaned_list = LoanItem.objects.filter(user__pk=user.pk)
        if (loaned_list):
            list = []
            for loan in loaned_list:
                list.append({"name": loan.item.name,
                             "category": loan.item.category,
                             "due_date":loan.dueDate,
                             "loan_date":loan.loanDate,
                             "item_id": loan.item.pk})
                
            return HttpResponse(json.dumps(list), content_type="application/json")

        return HttpResponse(json.dumps({"error_str": "Could Not find loaned items"}), content_type="application/json")
    return HttpResponse(json.dumps({"error_str": "Could Not find user"}), content_type="application/json")


#    send_email = request.POST.get('send_email', False)
#    Project.queries.get(project_id)
#
#    try:
#        member = ProjectMember.objects.get(project=project, user=user)
#    except ProjectMember.DoesNotExist:
#        member = None
