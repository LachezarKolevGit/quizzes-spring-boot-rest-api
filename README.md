# REST API that lets you upload, solve and delete quizzes. 
Created with spring-boot and H2 is used for database storage.

## Supported endpoints:
### Register a new user: <br />
* Send a POST request to http://localhost:8889/api/register following the requirments:
1. The email address has to contain @ and .
2. The password has to be of at least 5 symbols length
3. The email shouldn't be already present in the database

Example : 

```
curl --location 'http://localhost:8889/api/register' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test@gmail.com",
    "password": "123456"
}'
```
### Login:
* Send a POST request to http://localhost:8889/api/login 

Example : 

```
curl --location 'http://localhost:8889/api/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "test@gmail.com",
    "password": "123456"
}'
```
**After succesful login a jwtCookie will be used to keep track of the currently logged in user, so there isn't a need for authentication everytime a request to the API is made**

### Logout:
* An empty POST request to http://localhost:8889/api/logout will 'nullate' the jwtCookie and log out the user

Example : 

```
curl --location --request POST 'http://localhost:8889/api/logout' \
--header 'Cookie: jwtCookie=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTY3ODQ3NzI4MywiZXhwIjoxNjc4NTYzNjgzfQ.q10ePkIIjp91PmoBsXm3Bmsr0-iUd_tfKQnjnZgp6ks' \
--data ''
```
### Get a quiz to solve
* Send a GET request to http://localhost:8889/api/quiz and a random quiz will be returned

Example:

```
curl --location 'http://localhost:8889/api/quizz' \
--header 'Cookie: jwtCookie=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhYnYuYmciLCJpYXQiOjE2Nzg0ODAzMjIsImV4cCI6MTY3ODU2NjcyMn0.9HPdLW5IUvSCs3wof-hQygtRHIaSlm-PSFd82u-WARU' \
--data ''
```

### Upload a new quiz
* Send a POST request to http://localhost:8889/api/quizzes following the format from the example below. Multiple answers are supported, just separate them by `,` in the answer's json field ("answer": [1,2]).

Example:

```
{
curl --location 'http://localhost:8889/api/quizzes' \
--header 'Content-Type: application/json' \
--header 'Cookie: jwtCookie=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhYnYuYmciLCJpYXQiOjE2Nzg0ODAzMjIsImV4cCI6MTY3ODU2NjcyMn0.9HPdLW5IUvSCs3wof-hQygtRHIaSlm-PSFd82u-WARU' \
--data '{
    "title": "The Java Logo",
    "text": "What is depicted on the Java logo?",
    "options": [
        "Robot",
        "Tea leaf",
        "Cup of coffee",
        "Bug"
    ],
    "answer": [1]
}'
```
### Get all saved quizzes (has paging)
* Send a GET request to http://localhost:8889/api/quizzes with 
parameter `page` according to the index of the page you want

Example:

```
curl --location 'http://localhost:8889/api/quizzes?page=0' \
--header 'Cookie: jwtCookie=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBhYnYuYmciLCJpYXQiOjE2Nzg0ODAzMjIsImV4cCI6MTY3ODU2NjcyMn0.9HPdLW5IUvSCs3wof-hQygtRHIaSlm-PSFd82u-WARU' \
--data ''
```
### Get all solved quizzes by current logged in user

### Delete a quiz
