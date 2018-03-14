BEGIN;

DROP TABLE IF EXISTS nuc, sensor, measure;

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
	value bytea NOT NULL
);

ALTER TABLE measure ADD CONSTRAINT fk_nuc_measure FOREIGN KEY (id_nuc) REFERENCES nuc (id) ON DELETE CASCADE;
ALTER TABLE measure ADD CONSTRAINT fk_capteur_measure FOREIGN KEY (id_sensor) REFERENCES sensor (id) ON DELETE CASCADE;

ALTER TABLE sensor ADD CONSTRAINT unique_sensor_measurment UNIQUE (name, type, unity);

CREATE USER api PASSWORD 'password';

GRANT UPDATE, SELECT, INSERT, DELETE ON sensor, measure TO api;
GRANT SELECT ON measure TO PUBLIC;
GRANT SELECT, UPDATE, INSERT, DELETE ON measure, sensor TO api;

COMMIT;
