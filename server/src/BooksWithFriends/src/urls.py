'''
Created on 8/23/2013

@author: Xander
'''

from django.conf.urls.defaults import *

urlpatterns = patterns('src', 

    ('^newUser$', 'new_user'),
                       
    ('^getInventoryItem/(.+)$', 'get_inventory_item'),
    ('^addInventoryItem/(.+)$', 'add_inventory_item'),
    ('^getInventoryAll/(.+)$', 'get_inventory_all'),
    
    ('^getFriend/(.+)$', 'get_friend'),
    ('^getAllFriends/(.+)$', 'get_all_friends'),
    ('^getLoanedItem/(.+)$', 'get_loaned_item'),

    ('^getAllLoanedItems/(.+)$', 'get_all_loaned_items'),
    ('^getBorrowedItem/(.+)$', 'get_borrowed_item'),

    ('^getAllBorrowedItems/(.+)$', 'get_all_borrowed_items'),
    ('^newLoan/(.+)$', 'new_loan'),
    ('^newBorrow/(.+)$', 'new_borrow'),
    ('^getAllDueDate/(.+)$', 'get_all_due_dates'),
    ('^getUpcomingDueDates/(.+)$', 'get_upcoming_due_dates'),
    
)