/*
MySQL Data Transfer
Source Host: 127.0.0.1
Source Database: winedb
Target Host: 127.0.0.1
Target Database: winedb
Date: 2013/5/17 23:14:45
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
  `userlock` bit(1) NOT NULL,
  `role_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

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
-- Table structure for t_news
-- ----------------------------
CREATE TABLE `t_news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `createdate` datetime DEFAULT NULL,
  `desciption` varchar(255) DEFAULT NULL,
  `ispublish` bit(1) DEFAULT NULL,
  `link` varchar(255) DEFAULT NULL,
  `publishdate` datetime DEFAULT NULL,
  `source` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `viewcount` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `acct_authority` VALUES ('1', '后台管理员');
INSERT INTO `acct_authority` VALUES ('2', '注册会员');
INSERT INTO `acct_role` VALUES ('1', '后台管理员');
INSERT INTO `acct_role` VALUES ('2', '注册会员');
INSERT INTO `acct_role_authority` VALUES ('1', '1');
INSERT INTO `acct_role_authority` VALUES ('2', '2');
INSERT INTO `acct_user` VALUES ('1', '2013-05-17 22:37:39', 'admin', '后台管理员', 'admin', '', '后台管理员');
INSERT INTO `acct_user` VALUES ('2', '2013-05-16 21:41:59', 'user123', 'user', '123456', '', '注册会员');
INSERT INTO `acct_user` VALUES ('4', '2013-05-16 22:04:27', 'user125', '3333335', '1111111', '', '注册会员');
INSERT INTO `acct_user_role` VALUES ('1', '1');
INSERT INTO `acct_user_role` VALUES ('2', '2');
INSERT INTO `acct_user_role` VALUES ('4', '2');
INSERT INTO `t_news` VALUES ('2', '2013-05-17 22:35:45', '222222222', '', '111111', '2013-05-17 00:00:00', '222222', '12222', '0', '0');
