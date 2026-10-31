-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema BD_Metas_personales
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema BD_Metas_personales
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `BD_Metas_personales` DEFAULT CHARACTER SET utf8 ;
USE `BD_Metas_personales` ;

-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Usuario` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `nombres` VARCHAR(100) NOT NULL,
  `apellidos` VARCHAR(100) NOT NULL,
  `username` VARCHAR(100) NOT NULL,
  `email` VARCHAR(150) NOT NULL,
  `bday` DATE NOT NULL,
  `phone_number` VARCHAR(20) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
  
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Área`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Área` (
  `area_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `area` VARCHAR(100) NOT NULL,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`area_id`),
  INDEX `fk_Área_Usuario_idx` (`user_id` ASC),
  UNIQUE INDEX `area_name_UNIQUE` (`area` ASC),
  CONSTRAINT `fk_Área_Usuario`
    FOREIGN KEY (`user_id`)
    REFERENCES `BD_Metas_personales`.`Usuario` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Imagen`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Imagen` (
  `asset_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `url` VARCHAR(255) NOT NULL,
  `mime_type` BIGINT NOT NULL,
  `uploaded_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`asset_id`),
  INDEX `fk_Imagen_Usuario1_idx` (`user_id` ASC),
  INDEX `uploaded_at` (`uploaded_at` ASC),
  CONSTRAINT `fk_Imagen_Usuario1`
    FOREIGN KEY (`user_id`)
    REFERENCES `BD_Metas_personales`.`Usuario` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Estado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Estado` (
  `status_id` INT NOT NULL AUTO_INCREMENT,
  `status` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`status_id`),
  INDEX `status` (`status` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Ruta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Ruta` (
  `route_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `area_id` INT NOT NULL,
  `route_name` VARCHAR(150) NOT NULL,
  `route_description` TEXT NULL DEFAULT NULL,
  `starting_date` DATE NOT NULL,
  `duration_months` INT NULL DEFAULT NULL,
  `ending_date` DATE NULL DEFAULT NULL,
  `edition_counter` INT NULL DEFAULT 0,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `asset_id` INT NULL DEFAULT NULL,
  `status_id` INT NOT NULL,
  PRIMARY KEY (`route_id`),
  INDEX `fk_Ruta_Usuario1_idx` (`user_id` ASC),
  INDEX `fk_Ruta_Área1_idx` (`area_id` ASC) ,
  INDEX `fk_Ruta_Imagen1_idx` (`asset_id` ASC) ,
  INDEX `fk_Ruta_Estado1_idx` (`status_id` ASC) ,
  INDEX `route_name` (`route_name` ASC) ,
  INDEX `starting_date` (`starting_date` ASC) ,
  CONSTRAINT `fk_Ruta_Usuario1`
    FOREIGN KEY (`user_id`)
    REFERENCES `BD_Metas_personales`.`Usuario` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Ruta_Área1`
    FOREIGN KEY (`area_id`)
    REFERENCES `BD_Metas_personales`.`Área` (`area_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Ruta_Imagen1`
    FOREIGN KEY (`asset_id`)
    REFERENCES `BD_Metas_personales`.`Imagen` (`asset_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Ruta_Estado1`
    FOREIGN KEY (`status_id`)
    REFERENCES `BD_Metas_personales`.`Estado` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Hito`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Hito` (
  `hito_id` INT NOT NULL AUTO_INCREMENT,
  `route_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `hito_name` VARCHAR(150) NOT NULL,
  `hito_description` TEXT NULL DEFAULT NULL,
  `due_date` DATE NOT NULL,
  `edition_counter` INT NULL DEFAULT 0,
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `asset_id` INT NULL DEFAULT NULL,
  `status_id` INT NOT NULL,
  INDEX `fk_Hito_Ruta1_idx` (`route_id` ASC) ,
  PRIMARY KEY (`hito_id`),
  INDEX `fk_Hito_Usuario1_idx` (`user_id` ASC) ,
  INDEX `fk_Hito_Imagen1_idx` (`asset_id` ASC) ,
  INDEX `fk_Hito_Estado1_idx` (`status_id` ASC) ,
  INDEX `hito_name` (`hito_name` ASC) ,
  INDEX `due_date` (`due_date` ASC) ,
  CONSTRAINT `fk_Hito_Ruta1`
    FOREIGN KEY (`route_id`)
    REFERENCES `BD_Metas_personales`.`Ruta` (`route_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Hito_Usuario1`
    FOREIGN KEY (`user_id`)
    REFERENCES `BD_Metas_personales`.`Usuario` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Hito_Imagen1`
    FOREIGN KEY (`asset_id`)
    REFERENCES `BD_Metas_personales`.`Imagen` (`asset_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Hito_Estado1`
    FOREIGN KEY (`status_id`)
    REFERENCES `BD_Metas_personales`.`Estado` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `BD_Metas_personales`.`Tarea`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `BD_Metas_personales`.`Tarea` (
  `tarea_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `hito_id` INT NULL DEFAULT NULL,
  `task_name` VARCHAR(150) NOT NULL,
  `task_description` TEXT NULL DEFAULT NULL,
  `reminder_time` DATETIME NULL DEFAULT NULL,
  `due_time` DATETIME NULL DEFAULT NULL,
  `created_at` TIMESTAMP NOT NULL,
  `asset_id` INT NULL DEFAULT NULL,
  `status_id` INT NOT NULL,
  PRIMARY KEY (`tarea_id`),
  INDEX `fk_Tarea_Usuario1_idx` (`user_id` ASC) ,
  INDEX `fk_Tarea_Hito1_idx` (`hito_id` ASC) ,
  INDEX `fk_Tarea_Imagen1_idx` (`asset_id` ASC) ,
  INDEX `fk_Tarea_Estado1_idx` (`status_id` ASC) ,
  INDEX `due_time` (`due_time` ASC) ,
  CONSTRAINT `fk_Tarea_Usuario1`
    FOREIGN KEY (`user_id`)
    REFERENCES `BD_Metas_personales`.`Usuario` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Tarea_Hito1`
    FOREIGN KEY (`hito_id`)
    REFERENCES `BD_Metas_personales`.`Hito` (`hito_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Tarea_Imagen1`
    FOREIGN KEY (`asset_id`)
    REFERENCES `BD_Metas_personales`.`Imagen` (`asset_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Tarea_Estado1`
    FOREIGN KEY (`status_id`)
    REFERENCES `BD_Metas_personales`.`Estado` (`status_id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;




