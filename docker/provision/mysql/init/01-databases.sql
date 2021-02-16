-- create databases
CREATE DATABASE IF NOT EXISTS `fridge`;
CREATE DATABASE IF NOT EXISTS `main`;
CREATE DATABASE IF NOT EXISTS `auth`;

-- create root user and grant rights
-- drop user 'root'@'localhost';
-- flush privileges;
-- CREATE USER 'root'@'localhost' IDENTIFIED BY 'azis';

GRANT ALL ON *.* TO 'root'@'%';
