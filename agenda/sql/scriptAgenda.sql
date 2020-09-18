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
  `telefono` varchar(20) NOT NULL,
  `nacimiento` date DEFAULT NULL, 
  `email` text DEFAULT NULL,
  `contactoId` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`contactoId`) REFERENCES `tipoContacto`(`nombreContacto`)
);


CREATE TABLE IF NOT EXISTS `domicilios`
(
`id` int (11) NOT NULL AUTO_INCREMENT,
`pais` varchar(45) NOT NULL,
`provincia` varchar(45) NOT NULL,
`localidad` varchar(45) DEFAULT NULL,
`departamento` varchar(45) DEFAULT NULL,
PRIMARY KEY (`id`),
FOREIGN KEY (`id`) REFERENCES `personas`(`id`)
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

-- INSERT INTO `tipoContacto` (`nombreContacto`) VALUES ('Amigo');
-- INSERT INTO `tipoContacto` (`nombreContacto`) VALUES ('Familia');
-- INSERT INTO `tipoContacto` (`nombreContacto`) VALUES ('Trabajo');
