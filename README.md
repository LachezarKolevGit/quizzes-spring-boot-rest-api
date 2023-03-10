# REST API that lets you upload, solve and delete quizzes. 
Created with spring-boot and H2 is used for database storage.

## Supported endpoints:
### Register a new user: </br>
* Send a POST request to http://localhost:8889/api/register following the requirments:
1. The email address has to contain @ and .
2. The password has to be of at least 5 symbols length
3. The email shouldn't be already present in the database

```
curl --location 'http://localhost:8889/api/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test@gmail.com",
    "password": "123456"
}'
```
* Login
* Logout
* Get a quiz to solve
* Get all saved quizzes (has paging)
* Get all solved quizzes by current logged in user
* Delete a quiz
