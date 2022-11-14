# Instruction

Go to MainInterface.java and run MainInterface. There, you can start by choosing to log in, create account, delete account, or exit. Once you log in or create an account, the program will take you to the inner menus where you can choose to do various things. 

# Submission

Project 4 was submmited by ---- in Vocareum. <br />
Report was submitted by ---- on BrightSpace


# Classes

## Testing

All of the testing for classes were done by running TestMainInterface2.java with inputs from inputs.txt and save all of the outputs in output.txt comparing it with the expected.txt 

## Dashboard

#### Functionality

Manages the statistic of users. 
- User can view their statistics depending on their role.  
- Customers can view seller info that statistics for their stores 
  - Store name, seller email, number of message sent, and number of message received
- Sellers can view statistics for each of their stores
  - Store name, customer email, number of message received, and the most common words
- Supports sorting of the dashboard
  - Users can sort stores and sellers by alphabetical or inverse alphabetical order.
  - Users can sort stores by highest or lowest message received. 

#### Relationship

This class provides useful methods to MainInterface class so that MainInterface can give user option of presenting the dashboard. 

## Database 

#### Functionality

Manages the data of users. 
- All of the user data is stored in UserDatabase.txt and can be accessed through Database class using get() method. 
- Generates user ID when the account was created.
- With get() method, the program can get a HashMap of user data that includes id, email, password, role, last online date, blocked users, and invisible status.
- Also manages creating, modifying, verifying, and deleting account. 
- Support user blocking and unblocking features.

#### Relationship

Its relationship to other classes are crucial as it provides an easy access of the user data. 

## Filter

#### Functionality

Manages the filter 
- Users can add words to the filters.
- Users can remove words from the filter.
- Filter(line) method allows the program to filter words and replace them with word composed of *.
- has UserFilter.txt file that stores all of users filtered words.

#### Relationship

This class provides useful methods to MainInterface class so that users can modify their filter through MainInterface and MainInterface can filter messages that contain words from filter list.  

## InvalidKeyException

#### Functionality

Error that is thrown when the key or email includes the speicial character

#### Relationship

This class is necessary in Database as it prevents the program from entering data with special characters into database, which might cause potential problems. 

## InvalidUserException

#### Functionality

Error that is thrown when the user includes special character in their information(email/password), when user tries to create account with emails that already exists, or when user tries to access email that does not exists in the database.

#### Relationship

This class is necessary in Database class as the program can notify user that they are entering an invalid information. 

## InvalidWordException

#### Functionality

Error that is thrown when the user is trying to add existing word to the filter or get rid of word that does not exist. 

#### Relationship

This class is necessary in Filter class as the program can notify user if the word is valid or not. 

## MainInterface

#### Functionality

This class serves as a main interface where users can access every functionality of this program.
- In the outer menu, users can choose to 
  - log in
  - create account
  - delete account
  - exit in the outer menu.
- once user logs in or creates the account, the user is directed to the inner menu
- In the inner menu, users can choose to 
  - message a user
  - view messages
  - edit a message
  - delete a message
  - export a conversation
  - block a user
  - unblock a user
  - view stores
  - view customers
  - add a store
  - open dashboard
  - open filter
  - delete account
  - exit

#### Relationship

This class is important to other classes as it works as a main tool that allow users to use every available features.

## MessageManager

#### Functionality

Manages messages
- allow users to see their conversation history through their individual history file.
  - Each data from conversation history includes message, recipient, messageID, timestamp, and store
- allow users to send, edit, delete the message.
- saves and loads the data from individual history file.

#### Relationship

This class is essential in the MainInterface class as it provides methods to send, edit, or delete messages.

## MessageInterface

#### Functionality

A class that assists MessageManager class
- allow users to export their conversation into .csv file
- notifies users new message 
- allow users to send message

#### Relationship

This class provides useful methods to MessageManager and MainInterface so that the user can view their conversation history or export their conversation.

## Role

#### Functionality

Basic enum to separate between seller and customer

#### Relationship

This class is crucial to other classes as it provides ways to check if the user is seller or customer.
 
## User

#### Functionality

Manages customers and sellers
- allows customers to view Stores
- allow sellers to add stores and view customers
- stores various user data(id, email, role, stores)

#### Relationship

This class provides easy management in MainInterface so that the program can load data from Database, create User instance and store data there. 




