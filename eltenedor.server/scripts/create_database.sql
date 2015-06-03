CREATE DATABASE `eltenedor-ekain`
    CHARACTER SET 'latin1'
    COLLATE 'latin1_spanish_ci';

USE `eltenedor-ekain`;

#
# Estructura de la tabla `restaurant`
#
CREATE TABLE `restaurant` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico',
  `name` varchar(32) collate latin1_spanish_ci default NULL COMMENT 'Nombre del restaurante',
  `description` varchar(128) collate latin1_spanish_ci default NULL COMMENT 'Descripcion del restaurante',
  `table_count` tinyint(2) NOT NULL default '0' COMMENT 'Numero total de mesas',
  `longitude` varchar(32) collate latin1_spanish_ci default NULL COMMENT 'Longitud de las coordenadas del restaurante',
  `latitude` varchar(32) collate latin1_spanish_ci default NULL COMMENT 'Latitud de las coordenadas del restaurante',
  `logo_image` mediumblob COMMENT 'Archivo Adjunto en binario',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Restaurantes';

#
# Estructura de la tabla `user`
#
CREATE TABLE `user` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico',
  `name` varchar(32) collate latin1_spanish_ci default NULL COMMENT 'Nombre del usuario',
  `document` varchar(10) collate latin1_spanish_ci default NULL COMMENT 'Document identificador del usuario',
  `login` varchar(16) collate latin1_spanish_ci default NULL COMMENT 'Nombre de acceso',
  `password` varchar(16) collate latin1_spanish_ci default NULL COMMENT 'Contrasena de acceso',
  `email` varchar(64) collate latin1_spanish_ci default NULL COMMENT 'Email del usuario',
  `phone` varchar(9) collate latin1_spanish_ci default NULL COMMENT 'Telefono movil del usuario',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Usuarios';

#
# Estructura de la tabla `balance`
#
CREATE TABLE `balance` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico',
  `user` int(4) NOT NULL COMMENT 'Identificador unico del usuario',
  `amount` tinyint(2) NOT NULL default '0' COMMENT 'Importe disponible',
  PRIMARY KEY  (`id`),
  KEY `IDX_BALANCE_USER` (`user`),
  CONSTRAINT `FK_BALANCE_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Saldo';

#
# Estructura de la tabla `reservation`
#
CREATE TABLE `reservation` (
  `id` int(4) NOT NULL auto_increment COMMENT 'Identificador unico',
  `restaurant` int(4) NOT NULL COMMENT 'Identificador del restaurante',
  `user` int(4) NOT NULL COMMENT 'Identificador del usuario',
  `date` datetime default NULL COMMENT 'Fecha de la reserva',
  `status` tinyint(2) default '0' COMMENT 'Estado en el que se encuentra',
  `creation_date` datetime default NULL COMMENT 'Fecha de creacion',
  `person_count` int(4) NOT NULL COMMENT 'Numero de personas incluidas en la reserva',
  PRIMARY KEY  (`id`),
  KEY `IDX_RESERVATION_RESTAURANT` (`restaurant`),
  KEY `IDX_RESERVATION_USER` (`user`),
  CONSTRAINT `FK_RESERVATION_RESTAURANT` FOREIGN KEY (`restaurant`) REFERENCES `restaurant` (`id`),
  CONSTRAINT `FK_RESERVATION_USER` FOREIGN KEY (`user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COLLATE=latin1_spanish_ci COMMENT='Reservas';


