CREATE TABLE dbo.identifier_types
(
    id INT NOT NULL,
    id_country CHAR(3) NOT NULL,
    id_type CHAR(5) NOT NULL,
    description VARCHAR(128) NOT NULL,
    CONSTRAINT PK_identifier_types_identifier_id PRIMARY KEY CLUSTERED (id),
    CONSTRAINT AK_identifier_types_identifier_country_type UNIQUE (id_country, id_type)
);

