CREATE DATABASE  IF NOT EXISTS `hr` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `hr`;
-- MariaDB dump 10.17  Distrib 10.4.11-MariaDB, for Win64 (AMD64)
--
-- Host: 127.0.0.1    Database: hr
-- ------------------------------------------------------
-- Server version	10.4.11-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `details`
--

DROP TABLE IF EXISTS `details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `details` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `company_name` varchar(256) DEFAULT NULL,
  `branch` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `full_name` varchar(256) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `town` varchar(256) DEFAULT NULL,
  `education` varchar(256) DEFAULT NULL,
  `is_active` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `details`
--

LOCK TABLES `details` WRITE;
/*!40000 ALTER TABLE `details` DISABLE KEYS */;
INSERT INTO `details` VALUES (1,14,'Spartak','steel','sgdntveu uyememi e6uimkimk d7im',NULL,NULL,NULL,NULL,1),(2,15,'Autogas','car','adgh y etyneu eunum4',NULL,NULL,NULL,NULL,1),(3,16,NULL,NULL,NULL,'Mitko Colov',35,'Burgas','Math High School',1),(4,17,NULL,NULL,NULL,'Nina Ivanova',28,'Sofia','High shcool',1),(5,18,'Evro Petrol','fuel','gy ytnn uyny rrumb 5im',NULL,NULL,NULL,NULL,1),(6,19,NULL,NULL,NULL,'Nikola Milev',35,'Varna','University',1),(7,11,'Puma','sport','jmhavbf qrbfgbuy qryfbi',NULL,NULL,NULL,NULL,1),(8,9,'Romika','sport','ghjdvxjfb ehtbi hbtoiv',NULL,NULL,NULL,NULL,1),(9,5,'Training','sport','pooihtg UJYv',NULL,NULL,NULL,NULL,1),(11,12,NULL,NULL,NULL,'Evlogi Georgiev',32,'Jambol','High School',1),(12,4,'dtch','dh','arggg UJYv',NULL,NULL,NULL,NULL,1),(13,13,'Lima','electrik','kugyvbt kjhb',NULL,NULL,NULL,NULL,1),(14,10,NULL,NULL,NULL,'Emil Georgiev',30,'Jambol','High School',1),(15,6,NULL,NULL,NULL,'Ivan Georgiev',30,'Plovdiv','High School',1),(16,3,NULL,NULL,NULL,'Miho Mihov',30,'Plovdiv','University',1),(17,2,NULL,NULL,NULL,'Milen Gocev',20,'Plovdiv','University',1),(18,1,NULL,NULL,NULL,'Todor Lolov',28,'Ruse','University',1),(19,22,NULL,NULL,NULL,'Milena Ivanova',NULL,NULL,NULL,1),(20,23,NULL,NULL,NULL,'Georgi Cvetkov',NULL,NULL,NULL,1);
/*!40000 ALTER TABLE `details` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-09-03 20:20:51
