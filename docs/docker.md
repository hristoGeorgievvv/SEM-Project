# Docker usage
## Some info
This docker-compose script runs the following services:

- **Main service** bound to port `8080`
- **Auth service** bound to port `8081`
- **Fridge service** bound to port `8082`
- Redis container bound to port `6379` (default)
	- auth password `turbofolk`
- Mariadb container bound to port `3306` (default)
	- root password `azis`


### Deploy
```shell
docker-compose build

docker-compose up -d 
```

### Enter container
```shell
docker-compose exec auth/main/fridge/redis/mariadb sh
```
### Enter database
```shell
docker-compose exec mariadb sh
# From container do 
mysql -p
# Password *****
```
```mysql
SHOW DATABASES;
```
### Fix problems with database
```shell
docker-compose down

docker-compose up --force-recreate mariadb
```

## Run with filled database
For testing and demo purposes, you can run the services with a pre-filled database.

### Run
```shell
gradle build -x test -x checkstyleMain -x checkstyleTest -x pmdMain -x pmdTest

docker-compose -f docker-compose.yml -f docker-compose.populateddb.yml build

docker-compose -f docker-compose.yml -f docker-compose.populateddb.yml up -d
```

### New sql dump
To overwrite the sql dump used to fill the database, run the following command:
```shell
docker-compose exec mariadb mysqldump --password=***** --databases auth main fridge > docker/provision/mysql/init/02-data.sql
```