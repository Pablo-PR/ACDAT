DROP DATABASE IF EXISTS sanidad;

CREATE DATABASE `sanidad` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE sanidad;

-- sanidad.asegurado definition

CREATE TABLE `asegurado` (
  `idAsegurado` int(25) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(25) NOT NULL,
  `edad` int(2) NOT NULL,
  PRIMARY KEY (`idAsegurado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- sanidad.visitaCentro definition

CREATE TABLE `visitaCentro` (
  `idVisita` int(25) NOT NULL AUTO_INCREMENT,
  `codigoCentro` int(25) NOT NULL,
  `idAsegurado` int(25) NOT NULL,
  PRIMARY KEY (`idVisita`),
  KEY `visitaCentro_FK` (`idAsegurado`),
  CONSTRAINT `visitaCentro_FK` FOREIGN KEY (`idAsegurado`) REFERENCES `asegurado` (`idAsegurado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

COMMIT;