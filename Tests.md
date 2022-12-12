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
11. Click on the email field and set to customer3@gmail.com
12. Click the create account button
13. Click OK on the error stating the passwords don't match
14. Click on the confirm password field and change to password
15. Click create account

Expected Result: User is able to create an account as well as add stores if they are a seller without being able to enter invalid items to the form

*Test Result: passed*