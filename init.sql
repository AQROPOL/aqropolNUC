BEGIN;

DROP TABLE IF EXISTS nuc, sensor, measure;

CREATE EXTENSION pgcrypto;

CREATE TABLE nuc (
	id serial PRIMARY KEY,
	token varchar(255) UNIQUE NOT NULL
);

CREATE TABLE sensor (
	id serial PRIMARY KEY,
	name varchar(255) NOT NULL,
	type varchar(255) NOT NULL,
	unity varchar(255) NOT NULL
);

CREATE TABLE measure (
	id serial PRIMARY KEY,
	id_nuc serial NOT NULL,
	id_sensor serial NOT NULL,
	datetime TIMESTAMP NOT NULL,
	hash bytea NOT NULL,
	value bytea NOT NULL
);

ALTER TABLE measure ADD CONSTRAINT fk_nuc_measure FOREIGN KEY (id_nuc) REFERENCES nuc (id) ON DELETE CASCADE;
ALTER TABLE measure ADD CONSTRAINT fk_capteur_measure FOREIGN KEY (id_sensor) REFERENCES sensor (id) ON DELETE CASCADE;

ALTER TABLE sensor ADD CONSTRAINT unique_sensor_measurment UNIQUE (name, type, unity);

CREATE USER api PASSWORD 'password';

GRANT UPDATE, SELECT, INSERT, DELETE ON sensor, measure, nuc TO api;
GRANT SELECT ON measure TO PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON measure, sensor, nuc TO api;

CREATE OR REPLACE FUNCTION hash_update_measure_tg() RETURNS trigger AS $$
DECLARE
	name varchar(255);
	type varchar(255);
BEGIN
	IF tg_op = 'INSERT' OR tg_op = 'UPDATE' THEN

		SELECT s.name INTO name FROM sensor s WHERE id = NEW.id_sensor;
		SELECT s.type INTO type FROM sensor s WHERE id = NEW.id_sensor;

		NEW.hash = digest(NEW.id_sensor || encode(NEW.value, 'escape') || NEW.datetime, 'sha256');
		RETURN NEW;
	END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER measure_table_hash_update
	BEFORE INSERT OR UPDATE ON measure
	FOR EACH ROW EXECUTE PROCEDURE hash_update_measure_tg();

insert into nuc (token) values ('token_de_mon_nuc_dev_001');
insert into sensor (name, type, unity) values ('SDS011', 'pm10', 'ug/m3');
insert into sensor (name, type, unity) values ('SDS011', 'pm2.5', 'ug/m3');


COMMIT;

