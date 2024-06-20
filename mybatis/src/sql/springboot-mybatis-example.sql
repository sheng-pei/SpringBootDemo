CREATE DATABASE IF NOT EXISTS `springboot_mybatis_example` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `springboot_mybatis_example`;

DROP TABLE IF EXISTS `springboot_mybatis_example`;
CREATE TABLE `springboot_mybatis_example`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(127) DEFAULT NULL,
    `deleted`     tinyint      default 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1;