-- MariaDB dump 10.18  Distrib 10.5.8-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: auth
-- ------------------------------------------------------
-- Server version	10.5.8-MariaDB-1:10.5.8+maria~focal

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `auth`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `auth` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `auth`;

--
-- Sequence structure for `hibernate_sequence`
--

DROP SEQUENCE IF EXISTS `hibernate_sequence`;
CREATE SEQUENCE `hibernate_sequence` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`hibernate_sequence`, 1001, 0);

--
-- Table structure for table `user_entity`
--

DROP TABLE IF EXISTS `user_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_entity` (
                               `id` bigint(20) NOT NULL,
                               `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                               `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `UK_2jsk4eakd0rmvybo409wgwxuw` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_entity`
--

LOCK TABLES `user_entity` WRITE;
/*!40000 ALTER TABLE `user_entity` DISABLE KEYS */;
INSERT INTO `user_entity` VALUES (1,'$2a$10$7L9T/qu6p9XC4jfiAaq5juQbxsHVYzDBQqqY.84u7cJaqGB8y7bOC','otto'),(2,'$2a$10$kyBWbTauazLdVR3LY/1Hzea3/ZwLRkNZZd9fH4nB0JRs2ZhT7ARWi','koen'),(3,'$2a$10$c23VHAUBHOvDgitN1gvq3.EGdVQM6W7y/ujNSmTuYv0bOR1PW7iyW','stefan'),(4,'$2a$10$uAkT8hn/2bUMEOHsHkBEheaPTHIwBmAc7vRQmfikvcNoB74CIYTru','andy'),(5,'$2a$10$hW.OcFoguoYg/0aMypt0D.srqF8cV5cEYARi0fan6KrE6OVN2Jmm.','joana'),(6,'$2a$10$AMglGiSdtNWPEEZVE3IvDOhcwlUPRbF/LywsZL0BRfkyceCuJjqmi','alessandro'),(7,'$2a$10$mS2W.kB0CBvRNr53svUEzumXfUHnFlptata2A.evbbqXHNhR443oi','christoph'),(8,'$2a$10$5xJR0NFIuzoyCDxgSeBQwuH/a.XRSKB1suEUJdHy12USa..VNK/RS','asterios');
/*!40000 ALTER TABLE `user_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Current Database: `main`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `main` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `main`;

--
-- Table structure for table `UserEntity`
--

DROP TABLE IF EXISTS `UserEntity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `UserEntity` (
                              `id` bigint(20) NOT NULL,
                              `credits` float NOT NULL,
                              `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `UK_bmlfh5xt4kn5ucq55kyoutlwi` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `UserEntity`
--

LOCK TABLES `UserEntity` WRITE;
/*!40000 ALTER TABLE `UserEntity` DISABLE KEYS */;
INSERT INTO `UserEntity` VALUES (1,0,'otto'),(2,-62.6667,'koen'),(3,-49,'stefan'),(4,-26.3333,'andy'),(5,28,'joana'),(6,90,'alessandro'),(7,0,'christoph'),(8,20,'asterios');
/*!40000 ALTER TABLE `UserEntity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Sequence structure for `hibernate_sequence`
--

DROP SEQUENCE IF EXISTS `hibernate_sequence`;
CREATE SEQUENCE `hibernate_sequence` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`hibernate_sequence`, 1001, 0);

--
-- Current Database: `fridge`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `fridge` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `fridge`;

--
-- Table structure for table `Fridge`
--

DROP TABLE IF EXISTS `Fridge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Fridge` (
                          `id` bigint(20) NOT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Fridge`
--

LOCK TABLES `Fridge` WRITE;
/*!40000 ALTER TABLE `Fridge` DISABLE KEYS */;
INSERT INTO `Fridge` VALUES (1),(1001),(2001);
/*!40000 ALTER TABLE `Fridge` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Product`
--

DROP TABLE IF EXISTS `Product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Product` (
                           `id` bigint(20) NOT NULL,
                           `credit_value` int(11) NOT NULL,
                           `expiration_date` datetime(6) NOT NULL,
                           `portions` int(11) NOT NULL,
                           `portions_left` int(11) NOT NULL,
                           `product_name` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `fridge_id` bigint(20) NOT NULL,
                           `owner_id` bigint(20) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK_418u6oonno11bc0lw8cj3dfd6` (`product_name`),
                           KEY `FK58h6r6rekh495axe3swq7vt0j` (`fridge_id`),
                           KEY `FKhy8xvx60f4ktotebdjphl7uxc` (`owner_id`),
                           CONSTRAINT `FK58h6r6rekh495axe3swq7vt0j` FOREIGN KEY (`fridge_id`) REFERENCES `Fridge` (`id`),
                           CONSTRAINT `FKhy8xvx60f4ktotebdjphl7uxc` FOREIGN KEY (`owner_id`) REFERENCES `User` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Product`
--

LOCK TABLES `Product` WRITE;
/*!40000 ALTER TABLE `Product` DISABLE KEYS */;
INSERT INTO `Product` VALUES (1003,10,'2021-02-01 00:00:00.000000',5,5,'apples',1,1002),(1004,3,'2021-01-29 00:00:00.000000',10,10,'milk',1,1002),(1005,8,'2021-05-03 00:00:00.000000',100,100,'cheese',1,1002),(1007,5,'2021-03-03 00:00:00.000000',3,3,'pears',1,1006),(1008,3,'2021-03-04 00:00:00.000000',8,8,'bananas',1,1006),(1009,1,'2022-04-04 00:00:00.000000',3,3,'chips',1,1006),(1011,6,'2023-04-04 00:00:00.000000',24,24,'oreos',1,1010),(1012,4,'2024-01-02 00:00:00.000000',24,24,'gum',1,1010),(1013,6,'2024-01-02 00:00:00.000000',5,5,'cola',1,1010),(1015,7,'2024-01-02 00:00:00.000000',5,5,'fanta',1,1014),(1016,10,'2021-02-02 00:00:00.000000',5,5,'yoghurt',1,1014),(1017,10,'2021-02-02 00:00:00.000000',5,5,'vla',1,1014),(1019,12,'2021-02-01 00:00:00.000000',3,3,'colliflower',1,1018),(1022,8,'2022-08-13 00:00:00.000000',1,1,'pizza',1,1021),(1023,18,'2021-08-13 00:00:00.000000',15,15,'mushrooms',1,1021),(1026,16,'2021-03-13 00:00:00.000000',5,5,'sausage',1,1025),(1027,15,'2021-03-13 00:00:00.000000',2,2,'beef',1,1025),(1028,19,'2021-03-13 00:00:00.000000',4,4,'pork',1,1025),(1030,5,'2021-03-13 00:00:00.000000',100,100,'butter',1,1029),(1032,13,'2021-05-11 00:00:00.000000',10,10,'bacon',1,1029);
/*!40000 ALTER TABLE `Product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProductTransaction`
--

DROP TABLE IF EXISTS `ProductTransaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ProductTransaction` (
                                      `id` bigint(20) NOT NULL,
                                      `credits` int(11) NOT NULL,
                                      `isRevert` bit(1) NOT NULL,
                                      `portions` int(11) NOT NULL,
                                      `timestamp` datetime(6) NOT NULL,
                                      `product_id` bigint(20) NOT NULL,
                                      `revertedProductTransaction_id` bigint(20) DEFAULT NULL,
                                      `user_id` bigint(20) NOT NULL,
                                      PRIMARY KEY (`id`),
                                      KEY `FKnv8kj4tb09iccr6usbdm1nhwv` (`product_id`),
                                      KEY `FKpj2i5tsjlfbg58oopp9f891se` (`revertedProductTransaction_id`),
                                      KEY `FKbss0o8mwj43ahltlq216oc1j` (`user_id`),
                                      CONSTRAINT `FKbss0o8mwj43ahltlq216oc1j` FOREIGN KEY (`user_id`) REFERENCES `User` (`id`),
                                      CONSTRAINT `FKnv8kj4tb09iccr6usbdm1nhwv` FOREIGN KEY (`product_id`) REFERENCES `Product` (`id`),
                                      CONSTRAINT `FKpj2i5tsjlfbg58oopp9f891se` FOREIGN KEY (`revertedProductTransaction_id`) REFERENCES `ProductTransaction` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProductTransaction`
--

LOCK TABLES `ProductTransaction` WRITE;
/*!40000 ALTER TABLE `ProductTransaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `ProductTransaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
                        `id` bigint(20) NOT NULL,
                        `username` varchar(191) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `fridge_id` bigint(20) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UK_jreodf78a7pl5qidfh43axdfb` (`username`),
                        KEY `FKcs4nqohds6ccemwo2xknk7anx` (`fridge_id`),
                        CONSTRAINT `FKcs4nqohds6ccemwo2xknk7anx` FOREIGN KEY (`fridge_id`) REFERENCES `Fridge` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1002,'otto',1),(1006,'koen',1),(1010,'stefan',1),(1014,'andy',1),(1018,'joana',1),(1021,'alessandro',1),(1025,'christoph',1),(1029,'asterios',1);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Sequence structure for `hibernate_sequence`
--

DROP SEQUENCE IF EXISTS `hibernate_sequence`;
CREATE SEQUENCE `hibernate_sequence` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 cache 1000 nocycle ENGINE=InnoDB;
SELECT SETVAL(`hibernate_sequence`, 3001, 0);
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-17 17:03:18