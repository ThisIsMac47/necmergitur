-- phpMyAdmin SQL Dump
-- version 3.4.11.1deb2+deb7u2
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Lun 18 Janvier 2016 à 08:09
-- Version du serveur: 5.5.46
-- Version de PHP: 5.4.45-0+deb7u2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `backdb`
--

-- --------------------------------------------------------

--
-- Structure de la table `degrees`
--

CREATE TABLE IF NOT EXISTS `degrees` (
  `user_id` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `degrees`
--

INSERT INTO `degrees` (`user_id`, `name`) VALUES
('2D48E747-DF73-D74E-71F8-8C42560D443C', 'PSE2'),
('C63E1D10-D987-7A75-74DE-A0AC44323E60', 'PSE1'),
('2D48E747-DF73-D74E-71F8-8C42560D443C', 'PSC1'),
('C63E1D10-D987-7A75-74DE-A0AC44323E60', 'Driver'),
('8438D303-92D4-C7E4-81D3-80E526DF1479', 'Radio');

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `age` mediumint(9) DEFAULT NULL,
  `adress` varchar(255) DEFAULT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `last_geo` varchar(30) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `available` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `registerId` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`id`, `name`, `age`, `adress`, `mail`, `phone`, `last_geo`, `password`, `available`, `status`, `registerId`) VALUES
('2D48E747-DF73-D74E-71F8-8C42560D443C', 'Vincent', 44, '8603 In Street', 'scelerisque.scelerisque.dui@dapibus.co.uk', '01 87 30 61 78', '48.8968845,2.3182994', '42', 'AVAILABLE', 'NONE', 'APA91bENDzpjlSqCI2SQujXwRr0_QnHvrObLSBfjntA1IOxSYhQir2OjyqkWK4bD-jOjAT8yHkpllOhyu8KUftEhUo61MRFjmftD0mGbf6G70IWurDpzrYX9g_zsvIJbQuZ89EY6jUvN4CKQIrDd1xRmI1ui-PqfucTo8PiQouHbMubh_pWoo0Q'),
('C63E1D10-D987-7A75-74DE-A0AC44323E60', 'Valentin', 43, '7601 Erat Av.', 'eget.tincidunt.dui@perconubia.net', '04 76 48 04 27', '48.8967944,2.3182598', '42', 'AVAILABLE', 'NONE', 'APA91bG1XFsNPbemSj82brv9g7F-SlNfiFiC9G64bLH1l3_VPhUyFU6a1Ym-UxD1zMCTw0BB6X9UhE59S0ZWIwvLGmyb47kMxlrn8XgPTpMAu-O-gTuAUbSf7DO6ZF-PhT8QGxFIdW4y4Z2iOwznMl8G1S3jXpgZjV-idnO-HuojQFBrXHvJlEI'),
('8438D303-92D4-C7E4-81D3-80E526DF1479', 'Lewis', 43, 'P.O. Box 554, 5469 Tortor, Ave', 'varius.et@sitamet.edu', '08 75 02 92 62', '48.8939501,2.3076729', '42', 'ONLY_EMERGENCY', 'NONE', 'APA91bFkzm0ULy1S_tfKCINmS0Aq-v4-iHpcarfUnd4tmyjbEvl5kTKZKRg9uf-iekMOomHcnvffmtOreYkii_yOuwuTleaQLlpFF9ub8vlF5a6YQbPk6M_odoAHZTiuGyOKjvhG4TO3B9qDk5YCbhoUgxvwOVE_CALMM3SJQKl5wxSvWpnqUIE'),
('4217408C-A601-6D54-5886-C8C7D11A508D', 'Serena', 23, 'P.O. Box 610, 6091 Nec Ave', 'cursus@facilisisfacilisis.edu', '07 04 33 28 38', '8.38616, -20.75792', 'CKF53MFL3KC', 'NO_AVAILABLE', 'NONE', ''),
('BDE17581-6D2D-FD64-C08E-4B8AD59690CD', 'Mary', 34, 'P.O. Box 866, 5433 Sed Ave', 'litora@velarcueu.co.uk', '05 52 97 25 62', '67.91858, -53.45312', 'COC00IZW1UJ', 'NO_AVAILABLE', 'NONE', ''),
('16A72B10-FDA0-5376-2686-12A75BAB6E6F', 'Ima', 21, '2114 Risus, Street', 'purus.Duis@viverraDonectempus.net', '07 68 36 02 52', '30.41125, 109.3118', 'HVS52IXP3UV', 'NO_AVAILABLE', 'NONE', ''),
('1CAF32EC-9094-7D29-0797-6D0BF1D951E9', 'Riley', 25, '435-1318 Mauris St.', 'hendrerit.a.arcu@ridiculusmus.edu', '02 75 72 60 42', '36.27739, -107.21223', 'KYS88CCZ2VL', 'NO_AVAILABLE', 'NONE', ''),
('8DECC0F3-01EE-EDF1-E751-C5A94765C8B0', 'Desirae', 65, '966-6238 Orci St.', 'Fusce.diam.nunc@Maecenas.edu', '03 71 53 01 44', '43.97822, 98.08425', 'FIL65GPW2GR', 'NO_AVAILABLE', 'NONE', ''),
('20DE881D-77D3-7E25-5A17-32B6CAECAACA', 'Hiram', 42, 'P.O. Box 385, 8446 Fusce Rd.', 'arcu@vel.ca', '09 64 98 52 34', '-1.73852, 173.14379', 'SDE26JON7ZK', 'NO_AVAILABLE', 'NONE', ''),
('B2ED634F-4A05-CA3F-F7A8-88B36E1A9A88', 'Lenore', 44, 'P.O. Box 672, 4747 Interdum St.', 'ut@auctorvitaealiquet.ca', '07 21 02 48 19', '65.65908, 139.60289', 'CJW75MPQ2KU', 'NO_AVAILABLE', 'NONE', '');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
