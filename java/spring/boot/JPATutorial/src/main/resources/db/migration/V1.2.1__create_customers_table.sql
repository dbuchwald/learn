CREATE TABLE dbo.customer
(
    id UNIQUEIDENTIFIER NOT NULL,
    first_name VARCHAR(64),
    last_name VARCHAR(128) NOT NULL,
    CONSTRAINT PK_customer PRIMARY KEY CLUSTERED(id)
);