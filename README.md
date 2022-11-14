# Instructions

To compile and run our project, select the MainInterface.java file and click run. Once the program started running, look at the console to see the 
instructions to log in, create an account, or delete an account.

# Submission

Project 4 was submmited by ---- in Vocareum. <br />
Report was submitted by Atharva Gupta on BrightSpace


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

- Error that is thrown when the user is trying to use a key that is invalid. 

- This class is necessary as in the MainInterface.java class, if a user is trying to login, we check for the key to confirm the account.


## InvalidUserException

- Error that is thrown when the user is invalid when logging in. 

- This class is necessary within the project as in the MainInterface.java class, the user can be notified that an issue has occured when trying to login
  due to this exception.

## InvalidWordException

- Error that is thrown when the user is trying to add existing word to the filter or get rid of word that does not exist. 

- This class is necessary in Filter class as the program can notify user if the word is valid or not. 

- These exceptions play a vital role when creating, editing, and deleting accounts. 

## MainInterface
- In this class, the interface is where the user can login/create an account. Additionally, in this class a user can utilize all the funcitonality of marketplace messaging system to buy turkeys. 

- This class is important becuase the Main Interface is our bridge for this project. This is the class the user will run to start and initilize our program. 



## MessageManager
- Message Manager is a class that handles the message sending for a particular user. It specifically add create,e dit, and remove message
functionality as well as the means to export conversations into a .CSV file. 

- This class plays a vital role in creating a message history and a message file between a customer and a seller. 


## Role

- Basic enum to separate between seller and customer
 
## User
- Creates the Customer and Seller distinctions and includes unique funcitonalites such as viewing the stores, adding stores, and viewing customers. 
This is vital for the seller and customer roles. 
- This class also includes functionalities for reading stores from a file and getting a seller name that is associated with a specific store. 
- User.java is vital throughout this project as this is how it interacts with getting a specific seller that is associated with a store and viewing a list of customers that a seller can choose. 
