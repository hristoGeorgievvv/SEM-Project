# Endpoint Information
In this document, we will go over the API information for the Fridge microservice. This is the microservice where products are handled. This microservice will run on port `8082` by default.
		
All endpoints on the fridge microservice have the prefix `/fridge`.

# Take
### POST
A post request on the `take` endpoint will try to consume portions from a product. This request must include a valid Bearer-token and a valid body.

The body of the request must include the following: `productId`, `amount` and `username`. The `productId` field is used to define the ID of the product that you want to consume. The `amount` field then specifies how many portions you want to consume. The field `username` specifies which user will take the portions. A user can take portions on behalf of another user (for instance when sharing a meal).

A valid body looks like this:
```json
{
    "username": "user1",
    "productId": 5,
    "amount": 3
}
```

A request without a username looks the same, but the username field gets ommitted:
```json
{
    "productId": 5,
    "amount": 3
}
```

- Possible responses:
	+ <span style="color:green">200 OK</span> - The request had a valid authorization token and the product specified can be found. Also, if a username was given, the user has been found.
	+ <span style="color:red">400 BAD REQUEST</span> - The request had a valid authorization token, but the product or the user (if specified) cannot be found.
	+ <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.

A `200 OK` response will include more information in the response body. For example, if the amount of portions is insufficient, the body will include an `INSUFFICIENT` statement. A possible response can look like this:
```json
{
    "productId": 5,
    "portionsRequested": 12,
    "portionsLeft": 10,
    "outOf": 10,
    "delta": 0,
    "productStatus": "INSUFFICIENT"
}
```

The previous response shows that there were no portions taken (`delta` = 0) because there were not enough portions (`productStatus`). A resoponse where everything worked looks like this:
```json
{
    "productId": 5,
    "portionsRequested": 3,
    "portionsLeft": 7,
    "outOf": 10,
    "delta": -3,
    "productStatus": "SUFFICIENT"
}
```

Here we see that three portions were taken (`delta` = -3). We can also see that there are now seven portions left (`portionsLeft`).

# Products
### DELETE
A delete request on the `/products` endpoint will delete a product. The request must include a valid Bearer-token and must include the productId as a parameter. Note that this is different from a request body. In order to be able to delete a product, the requesting user must be the owner of the product.

A parameter can be added in the URL or via a program like Postman. Adding it in the url looks like the following for a productId of 5:
```
...:8082/fridge/products?productId=5
```

- Possible responses:
	+ <span style="color:green">200 OK</span> - The request had a valid authorization token and the product specified can be found. The product is deleted
	+ <span style="color:red">400 BAD REQUEST</span> - The request had a valid authorization token, but the product cannot be found.
	+ <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token or the product specified has a different owner.

### POST
A post request on this endpoint will add a product to the fridge. The product owner will be set to the requesting user. The request has to include a valid Bearer-token and a valid body.

The body of the request must include a `productName`, an amount of `portions`, a `creditvalue` and an `expirationDate`. A valid request body looks like the following:
```json
{
    "productName": "milk",
    "portions": 10,
    "creditvalue": 10,
    "expirationDate": "2021-10-12"
}
```

Note that products must have unique names, so adding two products called 'milk' will not work.

- Possible responses:
	+ <span style="color:green">200 OK</span> - The request had a valid authorization token and the product has been added.
	+ <span style="color:red">400 BAD REQUEST</span> - The product could not be added. A product with the specified name already exists.
	+ <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.

### PATCH
A patch request on the products endpoint will update an existing product in the database. The request must include a valid Bearer-token and the requester must be the owner of the product. Also, the request should include a valid body. The body of the request has the same form as the body of the request to add a product.

- Possible responses:
	+ <span style="color:green">200 OK</span> - The request had a valid authorization token and the product has been found and updated.
	+ <span style="color:red">400 BAD REQUEST</span> - The product could not be edited. The product was not found.
	+ <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token or you are not the owner of the product you are trying to edit.

### GET
A get request on the products endpoint will return all products in the fridge. The request must be authenticated with a valid Bearer-token.

- Possible responses:
	+ <span style="color:green">200 OK</span> - The request had a valid authorization token and the response contains an array of products in the fridge.
	+ <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.
	
# Undo
### POST
A post request on the `/undo` endpoint will undo the last transaction of the user on the specified product. The request must be authenticated with a valid Bearer-token and must include a productId as a parameter.

A parameter can be added in the URL or via a program like Postman. Adding it in the url looks like the following for a productId of 5:
```
...:8082/fridge/undo?productId=5
```

- Possible responses:
	+ <span style="color:green">200 OK</span> - The request had a valid authorization token and the product has been found.
	+ <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token or the transaction cannot be undone.

# Meal
### POST
A post mapping on the meal endpoint will create a meal. This will take out products for all users participating in the meal. The request must have a valid Bearer-token and a valid body. A valid body may look like this:
```json
{
    "usernames": [
        "user1",
        "user2"
    ],
    "products": {
        "5": 1,
        "12", 2
    }
}
```

The `usernames` field in the body specifies an array of users participating in the meal. The `products` field specifies a map with the product IDs and the amount of portions per user. For example, in the body above, product 5 will be taken once for each user. So, each user will take one portion from product 5.

- Possible responses:
  - <span style="color:green">200 OK</span> - The request was OK and the products have been used by all the specified users
  - <span style="color:red">403 FORBIDDEN</span> - The request had an invalid authorization token.
  - <span style="color:red">4xx BAD_REQUEST</span> - The request was invalid because a user was not found, a product was not found, or a product did not have enough portions available.
