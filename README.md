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

#### Relationship

This class is connected to AccountManagerGUI where users can select to unblock users.

## BlockGUIInterface

#### Functionality

Uses translator to communicate with server in order to receive blocked/unblocked user list 

#### Relationship

This class is crucial for BlockGUI as it connects the client with server to receive data

## Constant

#### Functionality

Classs that stores IP and port number for connecting server.

## CreateAccountGUI

#### Functionality

GUI that allows user to create a new account
- users can type their email, password 
- password has to be matched with password in ConfirmPassword Field

#### Relationship

If succeeded in creating account, this GUI connects user to the MainMenuGUI.

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

#### Functionality

GUI that displays statistics on each store or user.
- Sellers can choose store and user to view the conversation statistic.
- Customers can choose store to view the conversation statistic.

#### Relationship

This class is connected to AccountManagerGUI where users can choose to access dashboard.

## DashboardInterfaceGUI

#### Functionality

Uses translator to receive conversatoin data 
- get a map of seller and store
- get statistics for customers and sellers 

#### Relationship

This class is important to DashboardGUI as it offers the data from Dashboard in server.

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

#### Functionality

GUI that allows user to save their conversation data into csv file and choose the location.

#### Relationship

This class is connected with AccountManagerGUi, where users can choose to expoert their conversations.

## Filter

#### Functionality

Manages the filter 
- Users can add words to the filters.
- Users can remove words from the filter.
- Filter(line) method allows the program to filter words and replace them with word composed of *.
- has UserFilter.txt file that stores all of users filtered words.

#### Relationship

This class gives data to FilterInterfaceGUI through Server.

## FilterInterfaceGUI

#### Functionality

The class that carry over client-server interaction from users. 

#### Relationship

This class is crucial in Filter as it offers ways to communicate with server through use of translator object. 

## FilterPanel

#### Functionality

GUI that allows users to interact with filter
- allows users enable or disable filter
- allows users to add or remove words from filter

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

## Launch

#### Functionality

The class that lets user to start this program
- by running code here, user can open LogInGUI and use this software

#### Relationship

This class is crucial since it is a way to operate this program 

## LoginGUI

#### Functionality

GUI that prompts users to log in.
- users can either type their credentials to log in or move to createAccountGUI.

#### Relationship

This class connects users to MainMenuGUI or CreateAccountGUI.

## MainMenuGUI

#### Functionality

GUI that allows users to move to other GUIS
- users can move to peopleView or AccountManagerGUI through buttons.

#### Relationship

Once user is logged in, this class connects user to other GUIs that allow users to do various actions.

## Message

#### Functionality

basic class composed of message, sender, recipient, timeStamp, store, and messageID
- offers better and easier ways to store data 

#### Relationship

MessageInterfaceClient uses this class in order to efficiently manage message data received from server. 

## MessageGUI

#### Functionality

GUI that displays the current conversation with recipient
- displays real-time conversation chat
- allows send, edit, or delete message 
- color of the bubble differs depending on sender of the message
- supports multi-line message

#### Relationship

This class provides methods to interact with other users, and this class relies on MessageInterfaceClient for conversation data.

## MessageInterfaceClient

#### Functionality

The class that carry over user interactions with messages through use of translator object.
- allow users to send, edit, delete message through MessageManager class in server.
- allow users to get their personal history through MessageManager.
- allow users to export conversations
- do other various works that need to be done for MessageGUI

#### Relationship

This class is important to MessageGUI where sending and receiving conversation data is required.

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
- notifies users of new message.
- allow users to send message.

#### Relationship

This class provides useful methods to MessageManager and MainInterface so that the user can view their conversation history or export their conversation.

## PathManager

#### Functionality

Simple class that organizes paths for data files.

#### Relationship

This class can be used in any server-side class that refer to database in certain folders.

## PeopleView

#### Functionality

GUI that list users that the current user can message to. It differs depending on the user role
- Customers can choose which store they want to message to and can block or be invisible to the stores.
- Sellers can choose which customer they want to message to and pick which store they will use in order to contact the customer.
  - They could also block customers or be invisible ot them.

#### Relationship

This GUI class is important to MessageGUI since this class offers a way for user to pick recipient for conversations.

## Query

#### Functionality

Basic class composed of object, function, and parameter. 

#### Relationship

This class stores information in translator class and 

## Role

This class makes it much easier for translator to operate as all of necessary data can be stored in a single query class.

#### Functionality

Basic enum to separate between seller and customer or a deleted account.

#### Relationship

This class is crucial to other classes as it provides ways to check if the user is seller or customer.

## Server

Server communicates with various Client classes through Socket, ObjectOutStream, and ObjectInStream.
- reads and accepts sockets sent by user  
- send back the data requested by Client

#### Relationship

This class is essential in many client-side classes such as GUIs as this allows users to access data in server.

## Translator

#### Functionality

Translator serves as a bridge between client-server interactions
- offers ways for client to access various data in server through use of Query
 
#### Relationship

This claass is very important in GUI classes where data need to be loaded from the server, such as MessageGUI, PeopleView, DashboardGUI, and FilterPanel. 

## User

#### Functionality

Manages customers and sellers
- allows customers to view Stores
- allow sellers to add stores and view customers
- stores various user data(id, email, role, stores)

#### Relationship

This class provides easy management in MainInterface so that the program can load data from Database, create User instance and store data there. 




