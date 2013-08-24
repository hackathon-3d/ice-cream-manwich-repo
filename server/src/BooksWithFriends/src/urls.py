'''
Created on 8/23/2013

@author: Xander
'''

from django.conf.urls.defaults import *

urlpatterns = patterns('src.views', 

    url(r'^user$', 'user'),
    url(r'^user/(.+)$', 'user'),
                       
    url(r'^getInventoryItem/(.+)$', 'get_inventory_item'),
    url(r'^addInventoryItem/(.+)$', 'add_inventory_item'),
    url(r'^getInventoryAll/(.+)$', 'get_inventory_all'),
    
    url(r'^getFriend/(.+)$', 'get_friend'),
    url(r'^getAllFriends/(.+)$', 'get_all_friends'),

    url(r'^getBorrowedItem/(.+)$', 'get_borrowed_item'),
    url(r'^getAllBorrowedItems/(.+)$', 'get_all_borrowed_items'),

    url(r'^getLoanedItem/(.+)$', 'get_loaned_item'),
#    url(r'^getLoanForItem/(.+)$', 'get_loan_for_item'),
    url(r'^getAllLoanedItems/(.+)$', 'get_all_loaned_items'),
    url(r'^newLoan/(.+)/(.+)$', 'new_loan'),

    url(r'^getAllDueDate/(.+)$', 'get_all_due_dates'),
    url(r'^getUpcomingDueDates/(.+)$', 'get_upcoming_due_dates'),
    
)