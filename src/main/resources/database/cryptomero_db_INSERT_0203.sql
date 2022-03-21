-- tabel user_account
INSERT INTO `cryptomero`.`user_account` (`id_account`, `email`, `password`) VALUES ('1', 'stijnklijn@gmail.com', 'oostende');

-- tabel adres
INSERT INTO `cryptomero`.`address` (`id_address`, `street_name`, `house_no`, `house_add`, `postal_code`, `city`) VALUES ('1', 'Schieweg', '210', 'A', '3038BN', 'Rotterdam');

-- tabel customer
INSERT INTO `cryptomero`.`customer` (`first_name`, `last_name`, `dob`, `bsn`, `telephone`, `id_account`, `id_address`) VALUES ('Stijn', 'Klijn', '1985-11-13', '123456789', '0628328571', '1', '1');

-- tabel asset
INSERT INTO `cryptomero`.`asset` (`asset_name`, `asset_abbr`) VALUES ('bitcoin', 'bc');

-- tabel bank_account
INSERT INTO `cryptomero`.`bank_account` (`id_account`, `iban`, `balance_eur`) VALUES ('1', 'NL67INGB0007755322', '1000000');

-- tabel session
INSERT INTO `cryptomero`.`session` (`id_account`, `token`, `exp_time`) VALUES ('1', 'null', '2022-04-18T13:15:30');

-- tabel wallet
INSERT INTO `cryptomero`.`wallet` (`id_account`, `asset_name`, `amount`) VALUES ('1', 'bitcoin', '1');
