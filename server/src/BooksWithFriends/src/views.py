'''
Created on 8/23/2013

@author: Xander
'''
from BooksWithFriends.src.models import LibraryUser, MediaItem, LoanItem, \
    FriendConnection
from datetime import *
from django.http import HttpResponse
import json

def user(request, user_id=None):
    if request.method == "POST":
        phone = request.POST["phone"]
        
        try:
            libUser = LibraryUser.objects.get(phone=phone)
        except LibraryUser.DoesNotExist:
            libUser = None
        
        if (libUser):
            return HttpResponse(json.dumps({"user_id": libUser.pk}), content_type="application/json")

        name = request.POST["name"]
        email = request.POST.get("email", "")

        user = LibraryUser.objects.create_user(name, phone, email)
        user.save()
        if (user.pk):
            return HttpResponse(json.dumps({"user_id": user.pk}), content_type="application/json")
    
    elif request.method == "GET" and user_id:
        user = LibraryUser.objects.get(pk=user_id)
        return HttpResponse(json.dumps({"name": user.name,
                                        "phone": user.phone,
                                        "email": user.email }),
                                        content_type="application/json")

    return HttpResponse(json.dumps({"error_str": "Could Not create user"}), content_type="application/json")
    
def add_inventory_item(request, user_id ):
    user = LibraryUser.objects.get(pk=user_id)

    name = request.POST["name"]
    category = request.POST.get("category", "None")

    if (user.pk):
        item = MediaItem.objects.create_media_item(user, name, category)
        item.save()
        if (item.pk):
            return HttpResponse(json.dumps({"item_id": item.pk}), content_type="application/json")

        return HttpResponse(json.dumps({"error_str": "Could Not create item"}), content_type="application/json")

    return HttpResponse(json.dumps({"error_str": "Could Not find user"}), content_type="application/json")
    
def get_inventory_item(request, item_id):
    try:
        item = MediaItem.objects.get(pk=item_id)
    except MediaItem.DoesNotExist:
        return HttpResponse(json.dumps({"error_str": "Could Not find item"}), content_type="application/json")
        
    return HttpResponse(json.dumps({"name": item.name,
                                    "category": item.category,
                                    "item_id": item.pk}),
                                     content_type="application/json")

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

def new_loan(request, loan_from_user_id, loan_to_user_id):
    user = LibraryUser.objects.get(pk=loan_from_user_id)
    loan_to_user = LibraryUser.objects.get(pk=loan_to_user_id)

    item_name = request.POST["name"]
    category = request.POST.get("category", "None")
    dueDate = request.POST.get("dueDate", "")

    dateDue = datetime.strptime(dueDate, "%m/%d/%Y")
    
    try:
        item = MediaItem.objects.get(user__pk=user.pk, name=item_name, category=category)
    except:
        item = MediaItem.objects.create_media_item(user, item_name, category)
        item.save()
    
    loan = LoanItem.objects.create_loan_item(user, loan_to_user, item, dateDue)
    loan.save()
    
    isFriend = True
    try:
        FriendConnection.objects.get(user1__pk=loan_from_user_id, user2__pk=loan_to_user_id)
    except:
        isFriend = False

    try:
        FriendConnection.objects.get(user1__pk=loan_to_user_id, user2__pk=loan_from_user_id)
    except:
        isFriend = False

    if (not isFriend):
        connection = FriendConnection.objects.create_friend(user, loan_to_user)
        connection.save()
    
    loan_dump = ({"name": loan.item.name,
                 "category": loan.item.category,
                 "due_date": datetime.strftime(loan.dueDate, "%m/%d/%Y"),
                 "loan_date": datetime.strftime(loan.loanDate, "%m/%d/%Y"),
                 "item_id": loan.item.pk,
                 "loan_id": loan.pk,
                 "loan_from_name": loan.loanedFrom.name,
                 "loan_to_name": loan.loanedTo.name})
        
    return HttpResponse(json.dumps(loan_dump), content_type="application/json")
    
def get_loaned_item(request, loan_id):
    loan = LoanItem.objects.get(item=loan_id)
    
    list = ({"name": loan.item.name,
             "category": loan.item.category,
             "due_date": datetime.strftime(loan.dueDate, "%m/%d/%Y"),
             "loan_date": datetime.strftime(loan.loanDate, "%m/%d/%Y"),
             "item_id": loan.item.pk,
             "loan_id": loan.pk,
             "loan_from_name": loan.loanedFrom.name,
             "loan_to_name": loan.loanedTo.name})
                
    return HttpResponse(json.dumps(list), content_type="application/json")

def get_loan_for_item(request, item_id):
    loaned_list = LoanItem.objects.filter(item__pk=item_id)
    
    list = []
    for loan in loaned_list:
        list.append({"name": loan.item.name,
                     "category": loan.item.category,
                     "due_date": datetime.strftime(loan.dueDate, "%m/%d/%Y"),
                     "loan_date": datetime.strftime(loan.loanDate, "%m/%d/%Y"),
                     "item_id": loan.item.pk,
                     "loan_id": loan.pk,
                     "loan_from_name": loan.loanedFrom.name,
                     "loan_to_name": loan.loanedTo.name})
                
    return HttpResponse(json.dumps(list), content_type="application/json")

def get_all_loaned_items(request, user_id):
    user = LibraryUser.objects.get(pk=user_id)
    
    if (user.pk):
        loaned_list = LoanItem.objects.filter(loanedFrom__pk=user.pk)
        if (loaned_list):
            list = []
            for loan in loaned_list:
                list.append({"name": loan.item.name,
                             "category": loan.item.category,
                             "due_date": datetime.strftime(loan.dueDate, "%m/%d/%Y"),
                             "loan_date": datetime.strftime(loan.loanDate, "%m/%d/%Y"),
                             "item_id": loan.item.pk,
                             "loan_id": loan.pk,
                             "loan_from_name": loan.loanedFrom.name,
                             "loan_to_name": loan.loanedTo.name})
                
            return HttpResponse(json.dumps(list), content_type="application/json")

        return HttpResponse(json.dumps({"error_str": "Could Not find loaned items"}), content_type="application/json")
    return HttpResponse(json.dumps({"error_str": "Could Not find user"}), content_type="application/json")

def get_borrowed_item(request, loan_id):
    loan = LoanItem.objects.get(pk=loan_id)
    
    list = ({"name": loan.item.name,
             "category": loan.item.category,
             "due_date": datetime.strftime(loan.dueDate, "%m/%d/%Y"),
             "loan_date": datetime.strftime(loan.loanDate, "%m/%d/%Y"),
             "item_id": loan.item.pk,
             "loan_id": loan.pk,
             "loan_from_name": loan.loanedFrom.name,
             "loan_to_name": loan.loanedTo.name})
                
    return HttpResponse(json.dumps(list), content_type="application/json")

def get_all_borrowed_items(request, user_id):
    user = LibraryUser.objects.get(pk=user_id)
    
    if (user.pk):
        loaned_list = LoanItem.objects.filter(loanedTo__pk=user.pk)
        if (loaned_list):
            list = []
            for loan in loaned_list:
                list.append({"name": loan.item.name,
                             "category": loan.item.category,
                             "due_date": datetime.strftime(loan.dueDate, "%m/%d/%Y"),
                             "loan_date": datetime.strftime(loan.loanDate, "%m/%d/%Y"),
                             "item_id": loan.item.pk,
                             "loan_id": loan.pk,
                             "loan_from_name": loan.loanedFrom.name,
                             "loan_to_name": loan.loanedTo.name})
                
            return HttpResponse(json.dumps(list), content_type="application/json")

        return HttpResponse(json.dumps({"error_str": "Could Not find loaned items"}), content_type="application/json")
    return HttpResponse(json.dumps({"error_str": "Could Not find user"}), content_type="application/json")

def get_friend(request, user_id):
    user = LibraryUser.objects.get(pk=user_id)

    list = {"name":  user.name,
            "phone": user.phone,
            "email": user.email }

    return HttpResponse(json.dumps(list), content_type="application/json")
    
def get_all_friends(request, user_id):
    user = LibraryUser.objects.get(pk=user_id)
    friend_list1 = FriendConnection.objects.filter(user1__pk=user.pk)
    friend_list2 = FriendConnection.objects.filter(user2__pk=user.pk)
    
    list = []
    for friend in friend_list1:
        list.append({"name": friend.user2.name,
                     "phone": friend.user2.phone,
                     "email": friend.user2.email })

    for friend in friend_list2:
        list.append({"name": friend.user1.name,
                     "phone": friend.user1.phone,
                     "email": friend.user1.email })
    if (list):
        return HttpResponse(json.dumps(list), content_type="application/json")
    return HttpResponse(json.dumps({"error_str": "Could Not find any friends"}), content_type="application/json")
    