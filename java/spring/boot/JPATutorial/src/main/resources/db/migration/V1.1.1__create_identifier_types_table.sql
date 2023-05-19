CREATE TABLE dbo.identifier_type
(
    identifier_country CHAR(3) NOT NULL,
    identifier_type CHAR(5) NOT NULL,
    identifier_description VARCHAR(128) NOT NULL,
    CONSTRAINT PK_identifier_type PRIMARY KEY CLUSTERED (identifier_country, identifier_type)
);

