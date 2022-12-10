# Instruction

To compile and run our project, go to Server.java and run Server. Then, you should go to Launch.java, and run Launch java. There, you can start by choosing to log in, create account, or exit. Once you log in or create an account, the program will take you to the MainMenuGUI where you can choose to do various things. 

# Submission

Project 5 was submmited by Nitin Murthy in Vocareum. <br />
Report was submitted by Atharva Gupta on BrightSpace


# Classes

## Testing

All of the testing can be found in Test.md

## AccountInterfaceClient

#### Functionality

Uses translator to communicate with server to manage account info
- User can delete their account
- User can modify their password 
- User can modify their email
- User can log out

#### Relationship

This class is crucial in AccountManagerGUI as it allows client to communicate the server to modify or manager their account.

## AccountManagerGUI

#### Functionality

GUI that allows users to 
- access dashboard
- access filter
- unblock/undo invisible 
- export conversation
- delete their account
- sign out
- modify their email or password

#### Relationship

This class is important to mainMenuGUI as it serves a way for user to sign out or manage their account.

## BlockGUI 

#### Functionality

GUI that allows users to 
- unblock user
- view a list of blocked users
- view a list of unblocked users

## BlockGUIInterface

## Constant

## CreateAccountGUI

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

## DashboardGUI

## DashboardInterfaceGUI

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

## ExportCSV

## Filter

#### Functionality

Manages the filter 
- Users can add words to the filters.
- Users can remove words from the filter.
- Filter(line) method allows the program to filter words and replace them with word composed of *.
- has UserFilter.txt file that stores all of users filtered words.

#### Relationship

## FilterInterfaceGUI

## FilterPanel

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

## Launch

## LoginGUI

## MainMenuGUI

## Message

## MessageGUI

## MessageInterfaceClient

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
- notifies users of new message
- allow users to send message

#### Relationship

This class provides useful methods to MessageManager and MainInterface so that the user can view their conversation history or export their conversation.

## PathManager

## PeopleView

## Query

## Role

#### Functionality

Basic enum to separate between seller and customer or a deleted account

#### Relationship

This class is crucial to other classes as it provides ways to check if the user is seller or customer.

## Server

## Translator
 
## User

#### Functionality

Manages customers and sellers
- allows customers to view Stores
- allow sellers to add stores and view customers
- stores various user data(id, email, role, stores)

#### Relationship

This class provides easy management in MainInterface so that the program can load data from Database, create User instance and store data there. 




