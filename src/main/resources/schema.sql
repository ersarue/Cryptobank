CREATE SCHEMA cryptomero;

USE cryptomero;

CREATE TABLE `user_account`
(
    `id_account` INT          NOT NULL AUTO_INCREMENT,
    `email`      VARCHAR(30)  NOT NULL,
    `password`   VARCHAR(100) NOT NULL,
    `salt`       VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id_account`)
);

CREATE TABLE `address`
(
    `id_address`   INT    NOT NULL AUTO_INCREMENT,
    `street_name`  VARCHAR(45) NOT NULL,
    `house_no` INT NOT NULL,
    `house_add` VARCHAR (15) NULL,
    `postal_code` VARCHAR(7) NOT NULL,
    `city` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id_address`)
);

CREATE TABLE `customer`
(
    `first_name` VARCHAR(45) NOT NULL,
    `name_prefix` VARCHAR(15) NULL,
    `last_name` VARCHAR(45) NOT NULL,
    `dob` DATE NOT NULL,
    `bsn` INT NOT NULL,
    `telephone` VARCHAR(30) NOT NULL,
    `id_account` INT NOT NULL,
    `id_address` INT NOT NULL,
    PRIMARY KEY(`id_account`),
    CONSTRAINT `verzinNaam1`
    FOREIGN KEY (`id_account`)
    REFERENCES `cryptomero`.`user_account`(`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `verzinNaam2`
    FOREIGN KEY (`id_address`)
    REFERENCES `cryptomero`.`address`(`id_address`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `asset`
(
    `asset_name` VARCHAR(45) NOT NULL,
    `asset_abbr` VARCHAR(10) NOT NULL,
    PRIMARY KEY(`asset_name`)
);

CREATE TABLE `bank_account`
(
    `id_account` INT NOT NULL,
    `iban` VARCHAR(18) NOT NULL,
    `balance_eur` DECIMAL(13,4) NOT NULL,
    PRIMARY KEY(`id_account`),
    CONSTRAINT `verzinNaam3`
    FOREIGN KEY(`id_account`)
    REFERENCES `cryptomero`.`customer`(`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE `wallet`
(
    `id_account` INT NOT NULL,
    `asset_name` VARCHAR(45) NOT NULL,
    `amount` DECIMAL(13,4) NOT NULL,
    PRIMARY KEY(`id_account`,`asset_name`),
    CONSTRAINT `verzinNaam4`
    FOREIGN KEY(`id_account`)
    REFERENCES `cryptomero`.`customer`(`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `verzinNaam5`
    FOREIGN KEY (`asset_name`)
    REFERENCES `cryptomero`.`asset`(`asset_name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE `session`
(
    `id_account` INT NOT NULL,
    `token` VARCHAR(100) NOT NULL,
    `exp_time` DATETIME NOT NULL,
    PRIMARY KEY(`id_account`),
    CONSTRAINT `verzinNaam6`
    FOREIGN KEY(`id_account`)
    REFERENCES `cryptomero`.`user_account`(`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);