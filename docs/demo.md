# Demo

In this file you will find the information to run a demo.

## Run with filled database

For testing and demo purposes, you can run the services with a pre-filled database.

```shell
gradle build -x test -x checkstyleMain -x checkstyleTest -x pmdMain -x pmdTest

docker-compose -f docker-compose.yml -f docker-compose.populateddb.yml build

docker-compose -f docker-compose.yml -f docker-compose.populateddb.yml up -d
```

### Users

These are existing users:

* `otto`, with password `ducks`
* `koen`, with password `BlueBallz`
* `stefan`, with password `sharks`
* `andy`, with password `long_awnser_no`
* `joana`, with password `ADS4LIFE`
* `alessandro`, with password `SEQUEL`
* `christoph`, with password `<3squirl`
* `asterios`, with password `itsEZ`

## Log in
Log in with one of the users on `localhost:8080/main/login` using a `POST` request with body:
```json
{
    "username": "asterios",
    "password": "itsEZ"
}
```

If your login was successful, the response will be a JWT token. To utilize the application, put this token in your HTTP request header as a bearer token. (In postman this is done by going to authentication, selecting bearer token, and pasting the token into the field on the right).

Now all requests made to the other endpoints will be authorized, note that the token expires after 10 minutes, after which you will need to login again and use the new token.

## Get info
To get info about all users, do a `GET` request on `localhost:8080/main/users`.

To get only the flagged user, do a `GET` request on `localhost:8080/main/flagged`.

To get info about the currently logged in user, do a `GET` request on either `localhost:8080/main/credits/me` or `localhost:8080/main/flagged/me`.

## Add product
To add a product to the fridge, do a `POST` request on `localhost:8082/fridge/products` with body:
```json
{
    "productName" : "beer",
    "portions" : 6,
    "creditValue" : 15,
    "expirationDate" : "2021-05-06"
}
```

## Get products
To see all available products, do a `GET` request on `localhost:8082/fridge/products`.

## Take a portion
To take a portion, do `POST` request on `localhost:8082/fridge/take` with body:
```json
{
  "username": "asterios",
  "productId": 1013,
  "amount": 5
}
```
You can verify that the credits are changed after a product is finished by doing a `GET` request on `localhost:8080/main/users`.

## Meals
To process a meal, do a `POST` request on `localhost:8082/meal` with body:
```json
{
  "usernames": [
    "otto",
    "koen",
    "stefan"
  ],
  "products": {
    "1019": 1,
    "1023": 5
  }
}
```
You can verify that the credits are changed by doing a `GET` request on `localhost:8080/main/users`.

## Reset
To reset all the credits, do a `POST` request on `localhost:8080/main/reset`
You can verify that the credits are changed by doing a `GET` request on `localhost:8080/main/users`.


This concludes the demo. For all the endpoint information, see [endpoints](./docs/endpoints/).

