-- tabel user_account
INSERT INTO `cryptomero`.`user_account` (`id_account`, `email`, `password`, `salt`) VALUES ('1', 'example1@domain.com', '78baf80f5a429d314d29cedb3e0319d6ba7ef9b030b8d6e2ee055983a7879cd5e7f64fd3623baba3a7b49257d0c2156d9d0c970779276972b17742c33511d816', '22b29644'), ('2', 'example2@domain.com', '78baf80f5a429d314d29cedb3e0319d6ba7ef9b030b8d6e2ee055983a7879cd5e7f64fd3623baba3a7b49257d0c2156d9d0c970779276972b17742c33511d816', '22b29644');

-- tabel adres
INSERT INTO `cryptomero`.`address` (`id_address`, `street_name`, `house_no`, `house_add`, `postal_code`, `city`) VALUES ('1', 'Koos Postemalaan', '6', null, '1217DX', 'Hilversum');

-- tabel customer
INSERT INTO `cryptomero`.`customer` (`first_name`, `name_prefix`, `last_name`, `dob`, `bsn`, `telephone`, `id_account`, `id_address`) VALUES ('firstName1', 'namePrefix1', 'lastName1', '2000-01-01', '182358197', '0612345678', '1', '1'), ('firstName2', 'namePrefix2', 'lastName2', '2000-01-01', '565056761', '0612345678', '2', '1');

-- tabel asset
INSERT INTO `cryptomero`.`asset` (`asset_name`, `asset_abbr`) VALUES ('Bitcoin', 'BTC'), ('Ethereum', 'ETH'), ('Tether', 'USDT'), ('BNB', 'BNB'), ('USD Coin', 'USDC'), ('XRP', 'XRP'), ('Terra', 'LUNA'), ('Cardano', 'ADA'), ('Solana', 'SOL'), ('Avalanche', 'AVAX'), ('Polkadot', 'DOT'), ('Binance USD', 'BUSD'), ('Dogecoin', 'DOGE'), ('TerraUSD', 'UST'), ('Shiba Inu', 'SHIB'), ('Polygon', 'MATIC'), ('Wrapped Bitcoin', 'WBTC'), ('Cronos', 'CRO'), ('Dai', 'DAI'), ('Cosmos', 'ATOM');

-- tabel bank_account
INSERT INTO `cryptomero`.`bank_account` (`id_account`, `iban`, `balance_eur`) VALUES ('1', 'NL12CRME6357980432', '1000000'), ('2', 'NL17CRME8804842978', '1000000');

-- tabel wallet
INSERT INTO `cryptomero`.`wallet` (`id_account`, `asset_name`, `amount`) VALUES ('1', 'Bitcoin', '1'), ('1', 'Ethereum', '2'), ('2', 'Tether', '3'), ('2', 'BNB', '4');
