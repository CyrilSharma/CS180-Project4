# Setup:
1. Click the create account button
2. Click the email field and set to customer@gmail.com
3. Click the password field and set to password
4. Click the confirm password field and set to password
5. Click the customer button
6. Click the create account button

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

# Test 4: 