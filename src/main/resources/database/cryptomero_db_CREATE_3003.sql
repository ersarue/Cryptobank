-- MySQL Script

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema cryptomero
-- ---------------------------asset_name--------------------------

-- -----------------------------------------------------
-- Schema cryptomero
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cryptomero` DEFAULT CHARACTER SET utf8 ;
USE `cryptomero` ;

-- -----------------------------------------------------
-- Table `cryptomero`.`user_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`user_account` (
  `id_account` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(254) UNIQUE NOT NULL,
  `password` VARCHAR(128) NOT NULL,
  `salt` VARCHAR(8) NOT NULL,
  PRIMARY KEY (`id_account`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`address` (
  `id_address` INT NOT NULL AUTO_INCREMENT,
  `street_name` VARCHAR(45) NOT NULL,
  `house_no` INT NOT NULL,
  `house_add` VARCHAR(15) NULL,
  `postal_code` VARCHAR(7) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_address`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`customer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`customer` (
  `first_name` VARCHAR(45) NOT NULL,
  `name_prefix` VARCHAR(15) NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `dob` DATE NOT NULL,
  `bsn` VARCHAR(9) NOT NULL,
  `telephone` VARCHAR(30) NOT NULL,
  `id_account` INT NOT NULL,
  `id_address` INT NOT NULL,
  PRIMARY KEY (`id_account`),
  INDEX `verzinNaam1_idx` (`id_account` ASC) VISIBLE,
  INDEX `verzinNaam2_idx` (`id_address` ASC) VISIBLE,
  CONSTRAINT `verzinNaam1`
    FOREIGN KEY (`id_account`)
    REFERENCES `cryptomero`.`user_account` (`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `verzinNaam2`
    FOREIGN KEY (`id_address`)
    REFERENCES `cryptomero`.`address` (`id_address`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`asset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`asset` (
  `asset_name` VARCHAR(45) NOT NULL,
  `asset_abbr` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`asset_name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`rate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`rate` (
    `asset` VARCHAR(45) NOT NULL,
    `datetime` DATETIME NOT NULL,
    `rate` DECIMAL(32,16) NOT NULL,
    PRIMARY KEY (`asset`,`datetime`),
    CONSTRAINT `rate_asset`
        FOREIGN KEY (`asset`)
        REFERENCES `asset` (`asset_name`)
        ON DELETE RESTRICT
        ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`bank_account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`bank_account` (
  `id_account` INT NOT NULL,
  `iban` VARCHAR(18) NOT NULL,
  `balance_eur` DECIMAL(13,4) NOT NULL,
  PRIMARY KEY (`id_account`),
  INDEX `verzinNaam3_idx` (`id_account` ASC) VISIBLE,
  CONSTRAINT `verzinNaam3`
    FOREIGN KEY (`id_account`)
    REFERENCES `cryptomero`.`customer` (`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`wallet`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`wallet` (
  `id_account` INT NOT NULL,
  `asset_name` VARCHAR(45) NOT NULL,
  `amount` DECIMAL(13,4) NOT NULL,
  PRIMARY KEY (`id_account`, `asset_name`),
  INDEX `verzinNaam5_idx` (`asset_name` ASC) VISIBLE,
  INDEX `verzinNaam4_idx` (`id_account` ASC) VISIBLE,
  CONSTRAINT `verzinNaam4`
    FOREIGN KEY (`id_account`)
    REFERENCES `cryptomero`.`customer` (`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `verzinNaam5`
    FOREIGN KEY (`asset_name`)
    REFERENCES `cryptomero`.`asset` (`asset_name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `cryptomero`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cryptomero`.`transaction` (
  `id_transaction` INT NOT NULL AUTO_INCREMENT,
  `datetime` DATETIME NOT NULL,
  `asset_giver` INT NOT NULL,
  `asset_recipient` INT NOT NULL,
  `asset_name` VARCHAR(45) NOT NULL,
  `asset_amount` DECIMAL(13,4) NOT NULL,
  `eur_amount` DECIMAL(13,4) NOT NULL,
  PRIMARY KEY (`id_transaction`),
  CONSTRAINT `transactionFrom_account`
    FOREIGN KEY (`asset_giver`)
    REFERENCES `cryptomero`.`customer` (`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `transactionTo_account`
    FOREIGN KEY (`asset_recipient`)
    REFERENCES `cryptomero`.`customer` (`id_account`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `transaction_asset`
    FOREIGN KEY (`asset_name`)
    REFERENCES `cryptomero`.`asset` (`asset_name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE USER 'userCryptomero'@'localhost' IDENTIFIED BY 'pwCryptomero';
GRANT ALL PRIVILEGES ON cryptomero . * TO 'userCryptomero'@'localhost';
