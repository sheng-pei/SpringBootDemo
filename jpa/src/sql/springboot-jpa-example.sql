CREATE DATABASE IF NOT EXISTS `springboot_jpa_example` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `springboot_jpa_example`;

DROP TABLE IF EXISTS `springboot_jpa_example`;
CREATE TABLE `springboot_jpa_example`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `description` varchar(127) DEFAULT NULL,
    `deleted`     tinyint      default 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1;

