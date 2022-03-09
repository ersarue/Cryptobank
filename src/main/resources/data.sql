-- tabel user_account
INSERT INTO `cryptomero`.`user_account` (`id_account`, `email`, `password`, `salt`) VALUES ('1', 'example@domain.com', '3d9597c957f77b88d78ecc2bedf25acf44acfa591d88559f5c79072eea3fdcb1', 'a5f3c');

-- tabel adres
INSERT INTO `cryptomero`.`address` (`id_address`, `street_name`, `house_no`, `house_add`, `postal_code`, `city`) VALUES ('1', 'Street', '100', 'A', '1000AA', 'City');

-- tabel customer
INSERT INTO `cryptomero`.`customer` (`first_name`, `name_prefix`, `last_name`, `dob`, `bsn`, `telephone`, `id_account`, `id_address`) VALUES ('firstName', 'namePrefix', 'lastName', '2000-01-01', '182358197', '0612345678', '1', '1');

-- tabel asset
INSERT INTO `cryptomero`.`asset` (`asset_name`, `asset_abbr`) VALUES ('bitcoin', 'bc');

-- tabel bank_account
INSERT INTO `cryptomero`.`bank_account` (`id_account`, `iban`, `balance_eur`) VALUES ('1', 'NL12CRME6357980432', '1000000');

-- tabel session
INSERT INTO `cryptomero`.`session` (`id_account`, `token`, `exp_time`) VALUES ('1', 'null', '2022-04-18T13:15:30');

-- tabel wallet
INSERT INTO `cryptomero`.`wallet` (`id_account`, `asset_name`, `amount`) VALUES ('1', 'bitcoin', '1');
