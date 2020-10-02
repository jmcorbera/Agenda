DROP DATABASE IF EXISTS grupo_8;
CREATE DATABASE grupo_8;
USE grupo_8;

CREATE TABLE IF NOT EXISTS `tipoContacto`
(
	`nombreContacto` varchar(20) NOT NULL,
    PRIMARY KEY(`nombreContacto`)
); 

CREATE TABLE IF NOT EXISTS `personas`
(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `nacimiento` date DEFAULT NULL, 
  `email` text DEFAULT NULL,
  `contactoId` varchar(20) DEFAULT NULL,
  `contactoPreferente` text NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`contactoId`) REFERENCES `tipoContacto`(`nombreContacto`)
);

CREATE TABLE IF NOT EXISTS  `paises`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
PRIMARY KEY(`id`,`nombre`)
);

CREATE TABLE IF NOT EXISTS `provincias`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
`paisId` int (11) NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`paisId`) REFERENCES `paises` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `localidades`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
`provinciaId` int (11) NOT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`provinciaId`) REFERENCES `provincias` (`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `domicilios`
(
`id` int (11) NOT NULL,
`paisId` int(11) DEFAULT NULL,
`provinciaId` int(11) DEFAULT NULL,
`localidadId` int(11) DEFAULT NULL,
`calle` varchar(45) DEFAULT NULL,
`altura` varchar(45) DEFAULT NULL,
`piso` varchar(45) DEFAULT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`id`) REFERENCES `personas`(`id`)
);
ALTER TABLE `domicilios` ADD CONSTRAINT `provincia_fk`
FOREIGN KEY (`provinciaId`) REFERENCES `provincias` (`id`) ON DELETE SET NULL;

ALTER TABLE `domicilios` ADD CONSTRAINT `localidad_fk`
FOREIGN KEY (`localidadId`) REFERENCES `localidades` (`id`) ON DELETE SET NULL;

ALTER TABLE `domicilios` ADD CONSTRAINT `pais_fk`
FOREIGN KEY (`paisId`) REFERENCES `paises` (`id`) ON DELETE SET NULL;

INSERT IGNORE INTO `tipoContacto` (`nombreContacto`) VALUES ('Amigo');
INSERT IGNORE INTO `tipoContacto` (`nombreContacto`) VALUES ('Familia');
INSERT IGNORE INTO `tipoContacto` (`nombreContacto`) VALUES ('Trabajo');