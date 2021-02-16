# `Main` microservice

# Entities
* `User`
	* Holds user state, separate from a similar `User` entity in the `auth service` (the existance of the latter signifies that a user can be issued auth tokens).
	* This means that `User`s can exist even when authentication tokens can't be issued for them (e.g. user has deleted account but you want them to be able to come back).
	* This is where we want  to keep our general information about a user:
		- Username
		- Balance
		- etc.
# Responsibilities
## Auth cycle
#### Registration
1. User sends `register` request to `main service`.
2. `Main service` forwards request to `auth service`.
	- On success create local `user` entity 
	- On failure -> `400 Bad Request`
#### Login
1. User sends `login` request to `main service`. 
2. `Main service` forwards request to `auth service`.
	- On success forward token back to client
	- On failure -> `401 Unauthorized`
	