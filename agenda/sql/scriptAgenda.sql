DROP DATABASE grupo_8;
CREATE DATABASE `grupo_8`;
USE grupo_8;
CREATE TABLE `personas`
(
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) NOT NULL,
  `telefono` varchar(20) NOT NULL,
  `nacimiento` date DEFAULT NULL, 
  `email` varchar(20) NOT NULL,
  `idDomicilio` INT (11) DEFAULT NULL, 
  PRIMARY KEY (`id`)
);

CREATE TABLE `domicilios`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`pais` varchar(45) NOT NULL,
`provincia` varchar(45) NOT NULL,
`localidad` varchar(45) DEFAULT NULL,
`departamento` varchar(45) DEFAULT NULL,
PRIMARY KEY (`id`)
);

CREATE TABLE `paises`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
PRIMARY KEY(`id`,`nombre`)
);
CREATE TABLE `provincias`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
`paisId` int (11) NOT NULL,
PRIMARY KEY (`id`,`nombre`),
FOREIGN KEY (`paisId`) REFERENCES `paises`(`id`)
);
CREATE TABLE `localidades`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
`provinciaId` int (11) NOT NULL,
PRIMARY KEY (`id`,`nombre`),
FOREIGN KEY (`provinciaId`) REFERENCES `provincias`(`id`)
);
CREATE TABLE `departamentos`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`nombre` varchar(45) NOT NULL,
`localidadId` int (11) NOT NULL,
PRIMARY KEY (`id`,`nombre`),
FOREIGN KEY (`localidadId`) REFERENCES `localidades`(`id`)
);
