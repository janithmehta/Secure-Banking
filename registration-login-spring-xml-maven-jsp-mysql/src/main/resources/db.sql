CREATE DATABASE  IF NOT EXISTS `accounts`;
USE `accounts`;
DROP TABLE IF EXISTS `user_external`;
DROP TABLE IF EXISTS `user_internal`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varbinary(255) DEFAULT NULL,
  `password` varbinary(255) DEFAULT NULL,
  `email` varbinary(255) DEFAULT NULL,
  `status` varbinary(255) DEFAULT NULL,
  `address` varbinary(255) DEFAULT NULL,
  `dob` varbinary(255) DEFAULT NULL,
  `phone` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_internal` (
  `id` int(11) NOT NULL,
  `userType` varbinary(255) DEFAULT NULL,
  `designation` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user_external` (
  `id` int(11) NOT NULL,
  `userType` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;