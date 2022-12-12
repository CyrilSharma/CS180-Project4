# Access Messages
## Test 1: Login
Steps:
1. Run Server.java
2. Run Launch.java
3. Click the email box
4. Enter "customer1@gmail.com"
5. Click the password box and enter "passwordd"
6. Click login
7. Click ok on the error saying it is the wrong email or password
8. Click on the password box and retype "password"
9. Click login

Expected Result: User is able to login as a customer and access all the functions of the application while not being able to enter incorrect information

*Test Status: passed*

## Test 2: Create Account
Steps:
1. Run Server.java
2. Run Launch.java
3. Click the create account button
4. Click the email field and set to skjfakd.civ
5. Click the password field and set to password
6. Click the confirm password field and set to passwordd
7. Click the seller button
8. Write a store called "A New Turkey" and click add store
9. Click the create account button
10. Click OK on the error stating that the email is not valid
11. Click on the email field and set to seller3@gmail.com
12. Click the create account button
13. Click OK on the error stating the passwords don't match
14. Click on the confirm password field and change to password
15. Click create account

Expected Result: User is able to create an account as well as add stores if they are a seller without being able to enter invalid items to the form

*Test Result: passed*

## Test 3: Sending a message
Steps:
1. Run Server.java
2. Run Launch.java
3. Login as customer1@gmail.com with the password "password"
4. Run another instance of Launch.java to create a new client
5. Login as seller3@gmail.com with the password "password"
6. Click access conversations on the both instances of the client
7. On the customer's side, select the store "A New Turkey"
8. Click Message
9. In the text box at the top, type: "Hello, I am looking for turkeys"
10. Hit the enter key
11. On the seller view, click the blue customer1@gmail.com option in the list of customers
12. Click the "A New Turkey" store in the list of the seller's various stores
13. Click Message
14. Type "Here is my website, www.google.com"
15. Press the send message button
16. On the customer view, control-click on the message sent by the seller to open the link they sent

Expected Result: both users are able to see each other's views update in real time. The client sees the seller's message update on their screen in real time and the seller sees that they have a new message from a client in their view. Both parties can also send links.

*Test Result: passed*

## Test 4: The context menu
Steps:
1. Run the server (Server.java)
2. Run Launch.java
3. Login as customer1@gmail.com with password "password"
4. Run another instance of Launch.java
5. Login as seller3@gmail.com with password "password"
6. Click on access conversations in both instances
7. Select "A New Turkey" in customer view
8. Select customer1@gmail.com and "A New Turkey" in seller view
9. In customer view, send the message "I lov turkeys"
10. Right click the message and in the context menu, select edit
11. Select that you would like to edit the message
12. Change "I lov turkeys" to "I love turkeys" and click OK
13. In the seller view, right click on the newest message from the customer and select "copy" in the context menu
14. Paste the message into the text box by using the shortcut control-v.
15. Press the send message button
16. In the customer view, send the message "I hate your turkeys"
17. In seller view, right click on the new message and select the delete option in the context menu
18. Confirm you want to delete the message
19. Open a text file in an editor of your choice
20. Type: "I do not agree with your assessment.
    
    [On a new line] There are many great things about turkeys you simply do not understand."
21. Save the text file to a path of your choice.
22. In the seller view, click the send file button
23. Navigate to your text file using the file chooser GUI
24. Click OK to send the file
25. In the customer view, select the text box and type: "I don't like your turkeys for this reason:"
26. Click control-enter to create a new line.
27. Continue typing: "Your turkeys are too expensive"
28. Hit the enter key to send the message

Expected Result: The seller and the customer should be able to have a conversation in real time, copy messages, edit messages for both users, delete messages on one side, send the content in a text file (only .txt files are allowed) as a multi-line message to another user, and type multi-line messages to each other in the text box.

*Test Result: passed*

## Test 5: Blocking
Steps:
1. Run the server (Server.java)
2. Run two instances of Launch.java
3. Login as customer1@gmail.com with password "password"
4. On the other instance, login as seller3@gmail.com with password "password"
5. Click on access messages on both clients
6. Select customer1@gmail.com in seller view and select "A New Turkey" as the store and click message
7. Select "A New Turkey" as the store in customer view and click message
8. In customer view, type "Your turkeys are the worst" and click enter
9. Kill the server
10. In customer view, attempt to send the message "I will never buy your terrible products"
11. Click OK on the error
12. Restart the server
13. In customer view, send the message "I will never buy your terrible products"
14. In seller view, click the back button
15. Select customer1@gmail.com and click the block button and confirm you have done so
16. In customer view, type "You should stop selling turkeys"
17. Click OK on the error saying that you are not allowed to message that user
18. In seller view, select customer1@gmail.com again and click the invisible button
19. In customer view, click the back button and look for the store "A New Turkey", which you will find does not exist

Expected Result: The first feature should demonstrate the automatic reconnection of the client and the server, meaning that if there is an internet disruption or the server goes down, when they are reconnected, the client will work normally. Now that the customer has sent hate mail to the seller, the seller has blocked them and the customer cannot send new messages to them, though he can view the previous conversation history. The seller then becomes invisible to the customer, meaning that when the customer clicks the back button, the seller's store no longer appears in the list of stores the customer can see.

*Test Result: passed*

# Manage Account
Setup
1. Click the create account button
2. Click the email field and set to customer@gmail.com
3. Click the password field and set to password
4. Click the confirm password field and set to password
5. Click the customer button
6. Click the create account button

*Test Status: passed*

# Test 1: Delete Account & Signout
Steps:
1. Run Server.java
2. Run Launch.java
3. Click the create account button
4. Click the email field and set to customerABC@gmail.com
5. Click the password field and set to password
6. Click the confirm password field and set to password
7. Click the customer button
8. Click the create account button
12. Click on the manage account button.
13. Click on the sign out button.
14. Click yes.
15. Login (see below for login instructions)
16. Click on the manage account button.
17. Click on the delete account button.
18. Click yes.
19. Login (see below for login instructions)
20. Click ok.

Login:
1. Click the email field and set to customerABC@gmail.com
2. Click the password field and set to password
3. Click on the login button.

Expected Result: User can succesfull sign in and out while the account exists. When the account is deleted, the user can no longer log in.

*Test Status: passed*

# Test 2: Edit Username.
Steps:
1. Run Server.java
2. Run Launch.java
3. Click the email field and set to customer@gmail.com
4. Click the password field and set to password
5. Click Login.
6.  Click on the edit username button.
7.  Click yes in the popup.
8.  Enter customer1@gmail.com and confirm.
9.  Enter customer17982@gmail.com and confirm.
10. Click sign out.
11. Enter customer@gmail.com for the username field.
12. Enter the password as password.
13. Click Login
14. Click ok.
15. Enter customer17982@gmail.com for the username field.
16. Enter the password as password.
17. Click Login
18. Click the manage Account button.
19. Click edit username.
20. Set the username to customer@gmail.com

Expected Result: User is unable to rename themselves to an existing account. When user tries to rename to a new account, the rename operation is succesful. When the user signs out and signs in, they are able to use the new name, and unable to use the old name. At the end they should be able to change their name back to what it was.

*Test Status: passed*

# Test 3: Edit Password.
Steps:
1. Run Server.java
2. Run Launch.java
3. Click the email field and set to customer@gmail.com
4. Click the password field and set to password
5. Click Login.
6.  Click on the manage account button
7.  Click on the edit password button.
9.  Enter password2 and confirm.
11. Click sign out.
12. Enter customer@gmail.com for the username field.
13. Enter the password as password.
14. Click Login
15. Click ok.
16. Enter customer@gmail.com for the username field.
17. Enter the password as password2.
18. Click Login
19. Click on the manage account button
20. Click on the edit password button.
21. Enter password and confirm.

Expected Result: User can change password. When user tries to log in using old password, attempt fails. Logging in with new password succeeds.

*Test Status: passed*

# Test 4: Dashboard
1. Run Server.java
2. Run Launch.java
3. Login as customer1@gmail.com with password "password"
5. Click manage account.
6. Click dashboard.
7. Click A New Turkey.
8. Navigate to the access conversations button and click it.
9. Click A New Turkey.
10. Type "I changed my mind" and send.
11. Navigate back to dashboard and click on a new turkey.
12. Navigate back to conversations and send something to Turkey Island.
13. Navigate back to dashboard and click on sort a-z, and z-a.

Expected Result: Dashboard should correctly display counts of messages for recepient and sender. When the sender sends a new message, the dashboard updates to reflect the change. After the user sends a message to Turkey Island there should be at least two stores in dashboard, thus the effects of sorting should now be visible. You should see the things in ascending or descending alphabetical order according to which buttons were clicked.

*Test Status: passed*

# Test 5: Export CSV.
1. Run Server.java
2. Run Launch.java
3. Login as customer1@gmail.com with password "password"
4. Navigate to the access conversations button and click it.
5. Click on some conversation and send a few messages in it, remember which seller you messaged.
6. Navigate to manage account.
7. Click export conversations.
8. Select the same seller you talked to earlier.
9. Click export, and choose a directory of your convenience.

Expected Result: Upon manual inspection of the csv file, you should see the same messages you sent (along with any other messages previously in the conversation), in the messages column of the csv.

*Test Status: passed*

# Test 6: Filter.
1. Run Server.java
2. Run Launch.java
3. Login as customer1@gmail.com with password "password"
4. Go to a conversation of your choice and type spinach.
5. Navigate to the manage Conversations button.
6. Click it, and then click on filter.
7. Enter spinach in the textbox.
8. Click enable filter (if the filter is not already enabled).
9. Navigate back to your original conversation.
10. Navigate back to the filter.
11. Click remove word, then type spinach and click enter.
12. Go back to the original conversation.

Expected Result: When a word is added to the filter, you should see the word gets replaced with *** in the conversation. When you remove the word, you should see it goes back to normal.