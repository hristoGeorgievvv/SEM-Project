# Endpoint Information
In this document, we will go over the API information for the Main microservice. This is the microservice where for example credit calculation happens. This microservice will run on port `8080` by default.
		
All endpoints on the main microservice have the prefix `/main`.

# Users
### GET
A get request on this endpoint will return all users in the database. This request has to be authenticated with a valid Bearer-token. This token can be obtained by logging in.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the body of the response contains an array of users.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.

# Login
### POST
erforming a post request on this endpoint will validate the login details provided and return a signed Json Web Token if the combination is valid. This request requires a valid request body in order to be processed.

The request body should be in `JSON` format and include two fields, namely `username` and `password`. If either one of these fields is not included, the request will be rejected.
```json
{
	"username": "user1",
	"password": "verysecurepassword"
}
```

**Note that this endpoint forwards the request to the Authentication microservice. Upon successful login, the user will be added to the database of the Main microservice as well and their credits will be set to 0. This is the reason we process user login through this endpoint rather than through the endpoint on the authentication microservice.** If the user already exists in the main microservice database, nothing special happens.

- Possible responses:
  - <span style="color:green">200 OK</span> - The login attempt was successful and the body of the response includes a valid JWT token.
  - <span style="color:red">403 FORBIDDEN</span> - The username/password combination was not correct.

# Flagged
### GET
A get request on this endpoint will return all flagged users in the database. A user is flagged when the user has less than -50 credits. The request must include a valid Bearer-token, which can be obtained by logging in.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the body of the response contains an array of flagged users.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.

# Reset
### POST
A post request on this endpoint will reset all user credits to 0. The request must include a valid Bearer-token.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the credit counts are reset.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.

# Flagged/me
### GET
A get request on this endpoint will tell you whether or not you are flagged. This request has to include a valid Bearer-token. The request needs no body, instead, we can extract the username of the requesting user from the authorization token. That way we now who you are.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the body of the response contains `true` or `false`, depending on your `flagged` state.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.
  - <span style="color:red">400 BAD REQUEST</span> - The user that does the request is not found in the database.

# Credits/me
### GET
A get request on this endpoint will return your current amount of credits. This request has to include a valid Bearer-token. The request needs no body, instead, we can extract the username of the requesting user from the authorization token.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the body of the response contains your current amount of credits
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.
  - <span style="color:red">400 BAD REQUEST</span> - The user that does the request is not found in the database.

# User/me
### DELETE
A delete request on this endpoint will delete your account from the databases. Your account will be deleted on both the database from the authentication microservice and the main microservice.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the user was deleted.
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.
  - <span style="color:red">400 BAD REQUEST</span> - The user that does the request is not found in the database.