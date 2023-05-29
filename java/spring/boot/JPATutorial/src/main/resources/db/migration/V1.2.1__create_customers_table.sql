CREATE TABLE dbo.customers
(
    id UNIQUEIDENTIFIER NOT NULL,
    first_name VARCHAR(64),
    last_name VARCHAR(128) NOT NULL,
    CONSTRAINT PK_customer_id PRIMARY KEY CLUSTERED(id)
);