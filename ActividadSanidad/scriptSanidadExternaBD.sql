DROP DATABASE IF EXISTS sanidad;
CREATE DATABASE `sanidad_externa` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE sanidad_externa;

-- sanidad_externa.centro_externo definition

CREATE TABLE `centro_externo` (
  `idCentro` int(25) NOT NULL,
  `nombre` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`idCentro`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

COMMIT;