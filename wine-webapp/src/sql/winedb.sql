/*
MySQL Data Transfer
Source Host: 127.0.0.1
Source Database: winedb
Target Host: 127.0.0.1
Target Database: winedb
Date: 2013/5/13 21:42:54
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for acct_authority
-- ----------------------------
CREATE TABLE `acct_authority` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for acct_role
-- ----------------------------
CREATE TABLE `acct_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for acct_role_authority
-- ----------------------------
CREATE TABLE `acct_role_authority` (
  `role_id` bigint(20) NOT NULL,
  `authority_id` bigint(20) NOT NULL,
  KEY `FKAE243466A6F5A823` (`role_id`),
  KEY `FKAE2434666061E651` (`authority_id`),
  CONSTRAINT `FKAE2434666061E651` FOREIGN KEY (`authority_id`) REFERENCES `acct_authority` (`id`),
  CONSTRAINT `FKAE243466A6F5A823` FOREIGN KEY (`role_id`) REFERENCES `acct_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for acct_user
-- ----------------------------
CREATE TABLE `acct_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL,
  `login_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for acct_user_role
-- ----------------------------
CREATE TABLE `acct_user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  KEY `FKFE85CB3EA6F5A823` (`role_id`),
  KEY `FKFE85CB3E4C206C03` (`user_id`),
  CONSTRAINT `FKFE85CB3E4C206C03` FOREIGN KEY (`user_id`) REFERENCES `acct_user` (`id`),
  CONSTRAINT `FKFE85CB3EA6F5A823` FOREIGN KEY (`role_id`) REFERENCES `acct_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `acct_authority` VALUES ('1', '后台管理员');
INSERT INTO `acct_authority` VALUES ('2', '注册会员');
INSERT INTO `acct_role` VALUES ('1', '后台管理员');
INSERT INTO `acct_role` VALUES ('2', '注册会员');
INSERT INTO `acct_role_authority` VALUES ('1', '1');
INSERT INTO `acct_role_authority` VALUES ('2', '2');
INSERT INTO `acct_user` VALUES ('1', '2013-05-13 21:41:12', 'admin', '后台管理员', 'admin');
INSERT INTO `acct_user_role` VALUES ('1', '1');
