# Instruction

Go to MainInterface.java and run MainInterface. There, you can start by choosing to log in, create account, or delete account. 


# Submission

Project 4 was submmited by ---- in Vocareum. <br />
Report was submitted by ---- on BrightSpace


# Classes

## Dashboard

Manages the statistic of users. 
- User can view their statistics depending on their role.  
- Customers can view seller info that statistics for their stores 
  - Store name, seller email, number of message sent, and number of message received
- Sellers can view statistics for each of their stores
  - Store name, customer email, number of message received, and the most common words
- Supports sorting of the dashboard
  - Users can sort stores and sellers by alphabetical or inverse alphabetical order.
  - Users can sort stores by highest or lowest message received. 

This class provides useful methods to MainInterface class so that MainInterface can give user option of presenting the dashboard. 

## Database 

Manages the data of users. All of the user data is stored in UserDatabase.txt and can be accessed through Database class using get() method. 
- With get() method, the program can get a HashMap of user data that includes id, email, password, role, last online date, and blocked users.
- Also manages creating, modifying, verifying, and deleting account. 
- Support user blocking and unblocking features.

Its relationship to other classes are crucial as it provides an easy access of the user data. 
  
## Filter

- Manages the filter 
  - Users can add words to the filters.
  - Users can remove words from the filter.
  - Filter(line) method allows the program to filter words and replace them with word composed of *.
  - has UserFilter.txt file that stores all of users filtered words.

## InvalidKeyException



## InvalidUserException



## InvalidWordException

Error that is thrown when the user is trying to add existing word to the filter or get rid of word that does not exist. 

This class is necessary in Filter class as the program can notify user if the word is valid or not. 

## MainInterface



## MessageManager



## Role

- Basic enum to separate between seller and customer
 
## User
