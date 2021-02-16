# Endpoint Information
In this document, we will go over the API information for the Authentication microservice. This microservice will run on port `8081` by default.

We have four endpoints, two endpoints on `/user`, one on `/user/me`, and one on `/login`. All endpoints are preceded by `/auth`.

# User
### GET
A get request on this endpoint will return all users in the database. In order to make use of this endpoint, the request has to be authenticated with a valid Bearer-token. This token can be obtained by logging in (will be discussed later).

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the body of the response contains a JSON object of all users.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.

### POST
A post request on this endpoint will create a user and add it to the authentication database. This request does not require authentication. The password will be hashed before it is saved in the database, rendering it useless in case of a data leak.

In order to be able to add the user to the database, a valid request body has to be provided. A valid body is a `JSON` body and includes a `username` and a `password`. If a user with the provided username already exists, the request will be rejected (see possible responses). This will also happen when one of the mentioned fields (`username` or `password`) is not defined.

An example of a valid request body is the following:
```json
{
	"username": "user1",
	"password": "verysecurepassword"
}
```

- Possible Responses:
  - <span style="color:green">200 OK</span> - The request was OK and the user has been added to the database.
  - <span style="color:red">400 BAD REQUEST</span> - The request body was empty or incomplete, meaning the username or password (or both) were missing or empty.
  - <span style="color:red">409 CONFLICT</span> - A user with the provided username already exists.

# User/me
### DELETE
A delete request on this endpoint will delete the user that is making the request from the database. This implies that this request needs to be authenticated with a valid Bearer-token. This token can be obtained by logging in.

The use of Bearer-tokens (Json Web Tokens) allows us to extract the username of the user performing the request. This allows us to delete the correct user from the database.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the user was deleted.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.
  - <span style="color:red">400 BAD REQUEST</span> - The user that does the request is not found in the database.
    
# Login
### POST
Performing a post request on this endpoint will validate the login details provided and return a signed Json Web Token if the combination is valid. This request requires a valid request body in order to be processed.

The request body required for this request is the same as the body for creating a user. So, the following example will work:
```json
{
	"username": "user1",
	"password": "verysecurepassword"
}
```

This endpoint has the same requirements on the body as the create user one does. That means, the username and password fields have to be both provided.

NOTE: This endpoint will never be accessed by the users themselves, as logging in will happen through the MainService login endpoint. The documentation for that one can be found in the `docs` folder as well.

- Possible responses:
  - <span style="color:green">200 OK</span> - The login attempt was successful and the body of the request includes a valid JWT token.
  - <span style="color:red">403 FORBIDDEN</span> - The username/password combination was not correct.


