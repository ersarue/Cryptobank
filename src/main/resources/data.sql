-- tabel user_account
INSERT INTO `cryptomero`.`user_account` (`id_account`, `email`, `password`, `salt`)
VALUES ('1', 'example@domain.com', '99b321944e4a8be84e17f5069a2b21d120b3d185e059db039464a9499cb74c0f', 'cbcd2a4f'),
       ('2', 'example2@domain.com', '99b321944e4a8be84e17f5069a2b21d120b3d185e059db039464a9499cb74c0a', 'cbcd2a4a');

-- tabel adres
INSERT INTO `cryptomero`.`address` (`id_address`, `street_name`, `house_no`, `house_add`, `postal_code`, `city`)
VALUES  ('1', 'Street', '100', 'A', '1000AA', 'City'),
        ('2', 'Street', '99', 'D', '1000CC', 'Duckstad'),
--        Address no. 3 is not linked to a customer in order to test the delete() method (circumvents FK-constraint)
        ('3', 'Broadstreet', '3', 'B', '1234AB', 'Village');

-- tabel customer
INSERT INTO `cryptomero`.`customer` (`first_name`, `name_prefix`, `last_name`, `dob`, `bsn`, `telephone`, `id_account`, `id_address`)
VALUES ('firstName', 'namePrefix', 'lastName', '2000-01-01', '182358197', '0612345678', '1', '1'),
       ('customer2', 'namePrefix', 'lastName2', '1970-01-01', '480777664', '0612345678', '2', '2');

-- tabel asset
INSERT INTO `cryptomero`.`asset` (`asset_name`, `asset_abbr`)
VALUES ('Bitcoin', 'BTC'),
       ('Ethereum', 'ETH');

-- tabel bank_account
INSERT INTO `cryptomero`.`bank_account` (`id_account`, `iban`, `balance_eur`)
VALUES ('1', 'NL12CRME6357980432', '1000000');

-- tabel session
INSERT INTO `cryptomero`.`session` (`id_account`, `token`, `exp_time`)
VALUES ('1', 'null', '2022-04-18T13:15:30');

-- tabel wallet
INSERT INTO `cryptomero`.`wallet` (`id_account`, `asset_name`, `amount`)
VALUES ('1', 'Bitcoin', '1'),
       ('2', 'Ethereum', '3');

-- tabel transaction
INSERT INTO `cryptomero`.`transaction` (`id_transaction`, `datetime`, `asset_giver`, `asset_recipient`, `asset_name`, `asset_amount`, `eur_amount`, `eur_fee`)
VALUES ('1', '2022-03-30 11:20:34', '1', '2', 'Bitcoin', '0.5', '21409.61', '30.50'),
       ('2', '2022-03-30 11:21:50', '2', '1', 'Ethereum', '2.0', '6099.62', '4.25');
