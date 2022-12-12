# Test 1: Login
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

# Test 2: Create Account
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

# Test 3: Sending a message
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

# Test 3: Editing a message
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