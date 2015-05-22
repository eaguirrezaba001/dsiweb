CREATE DATABASE IF NOT EXISTS `agendadb`;
USE `agendadb`;

CREATE TABLE IF NOT EXISTS `CONTACTO` (
	`uuid` char(36) DEFAULT NULL,
    `androidId` BIGINT UNSIGNED NOT NULL,
    `nombre` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) DEFAULT NULL,
    `telefono` VARCHAR(45) DEFAULT NULL,
    `direccion` VARCHAR(255) DEFAULT NULL,
    `imageUri` VARCHAR(255) DEFAULT NULL,
    `propietario` VARCHAR(45) NOT NULL,
    `creado` TIMESTAMP DEFAULT current_timestamp,
    `actualizado` TIMESTAMP DEFAULT current_timestamp,
    PRIMARY KEY(`uuid`),
    KEY `idx_contacto_nombre` (`nombre`),
    KEY `idx_contacto_propietario` (`propietario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

delimiter //
CREATE TRIGGER contacto_uuid BEFORE INSERT ON `CONTACTO`
	for each row begin
		IF NEW.uuid IS NULL THEN
			set NEW.uuid=UUID();
		END IF;
        if NEW.creado IS NULL THEN
			set NEW.creado=current_timestamp;
            set NEW.actualizado=NEW.creado;
		END IF;
	END; //
delimiter ;

delimiter //
CREATE TRIGGER contacto_actualizado BEFORE UPDATE ON `CONTACTO`
	for each row begin
		set NEW.actualizado=current_timestamp;
	END; //
delimiter ;

