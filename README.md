## Probelm Statement
A rate limiter is a tool that monitors the number of requests per
a window time a service agrees to allow. If the request count exceeds the number agreed by the
service owner and the user (in a decided window time), the rate limiter blocks all the excess
calls (say by throwing exceptions). The user can be a human or any other service (ex: in a micro
service-based architecture)

## How to run the project
Import the project as a maven project and run as Java program in Tomcat server

## Sample data with default values for time limit and trnsaction limit per api per user:
Tom, Dick, Harry are the 3 users accessing the API's 
'Tom' can access api 'viewItem' 3 times in 60 secs
'Dick' can access api 'addItem' 3 times in 60 secs
To test trigger the below url in a browser:
http://localhost:8080/ratelimit/viewItem/Tom

## Result
The user would be able to access the api's 3 times in 60 secs.

## To configure new range for time window and transaction limit
Run postman with the url: localhost:8080/ratelimit/setDefault
with request body:
{
    "user" : "Tom",
    "timeInMin" : 5,
    "limit" : 5,
    "api" : "addItem"
}

This will extend the time window for user Tom for 5 mins for 5 transactions
