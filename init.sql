DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
  `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(255) NOT NULL,
  `url`        VARCHAR(255)          DEFAULT NULL,
  `created_at` DATETIME              DEFAULT NULL,
  `deleted_at` DATETIME              DEFAULT NULL,
  PRIMARY KEY (`id`)
);
