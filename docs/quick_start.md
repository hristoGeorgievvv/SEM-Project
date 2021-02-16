# Quick Start
In this document, we will go over the basics of creating a user, and logging in. Once you do this you will have a JWT token that authorizes you to utilize the rest of the application.

# User creation

To create a user account, you will have to make a `POST` request to the authentication microservice.
The microservice runs on port `8081`, and we are posting on the `user` endpoint, so the request will look something like 
`http://localhost:8081/auth/user`. The request takes a JSON object as the request body, and does not require any other authentication.

An example proper JSON request
```json
{
	"username": "username",
	"password": "password"
}
```

Now the user has been added to the authentication microservice's database. 

# Logging in

The next step is to log in to your account. This _must_  be done on the main microservice 
for everything to function properly, because upon logging into the main microservice for the 
first time, your user account is also then added to the main microservice's database. 

To log in, you will make a `POST` request to `/main/login`, on port `8080`. The full request could 
look like `http://localhost:8080/main/login`.

In this request, provide the _same_ username and password JSON that was used for creating the user.
This request will create the user on the main microservice, and then internally main will also send a
login request to auth, which will return a JWT token if the username and password match ones that
are found in the authentication microservice's database.

# Using the application

If your login was successful, the response will be a JWT token. To utilize the application, put this token in your
HTTP request header as a bearer token. (In postman this is done by going to authentication, selecting bearer token, and 
pasting the token into the field on the right). 

Now all requests made to the other endpoints will be authorized, note that the token expires after 10 minutes, after which 
you will need to login again and use the new token.  