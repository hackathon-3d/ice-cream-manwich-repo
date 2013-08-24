'''
Created on 8/23/2013

@author: Xander
'''

from django.conf.urls.defaults import *

urlpatterns = patterns('src', 
    ('^getInventory/(.+)$', 'getInventory'),
    ('^addInventoryItem/(.+)$', 'addInventoryItem'),
    ('^getFriend/(.+)$', 'getFriend'),
    ('^getAllFriends/(.+)$', 'getAllFriends'),

    ('^getLoanedItem/(.+)$', 'getLoanedItem'),
    ('^getAllLoanedItems/(.+)$', 'getAllLoanedItems'),
    ('^getBorrowedItem/(.+)$', 'getBorrowedItem'),
    ('^getAllBorrowedItems/(.+)$', 'getAllBorrowedItems'),
    ('^newLoan/(.+)$', 'newLoan'),
    ('^newBorrow/(.+)$', 'newBorrow'),
    ('^getAllDueDate/(.+)$', 'getAllDueDates'),
    ('^getUpcomingDueDates/(.+)$', 'getUpcomingDueDates'),
    
)