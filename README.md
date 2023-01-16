# PAP22L-Z27


## Members

- Krzysztof Fijałkowski
- Adam Jeliński
- Wojciech Łapacz


## Decription
The project idea is to create a student-friendly platform for exchanging terms of classes.

#### Link to API documentation
https://hackmd.io/@gambolkf/rkoV65Ocs

#### Functionalities:
- place your exchange offer on the market
- choose an exchange offer from the market
- add new course/lecturer/group/classroom
- registration with password hashing
- login by session validated with tokens
- history of user's exchanges
- ability to change password
- sending e-mail by user to report a problem (with a log file in attachment)
- ability to change user's account details

#### Features:
- server from scratch
- multi-threaded server
- sql-injection protection
- pooling connections to the database (much better efficiency)
- clientside rendering
- robust API, protected against malicious requests (eg: unauthorised request, invalid data)
- automatic email notifications (registration, exchange, password change)
- multi-threaded background operations eg: sending emails

## Technologies
- Java
- Oracle DB
- Vue.js
