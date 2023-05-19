CREATE TABLE dbo.customer_identifier
(
    customer_id UNIQUEIDENTIFIER NOT NULL,
    identifier_country CHAR(3) NOT NULL,
    identifier_type CHAR(5) NOT NULL,
    identifier_value VARCHAR(64) NOT NULL,
    CONSTRAINT PK_customer_identifier PRIMARY KEY CLUSTERED(customer_id, identifier_country, identifier_type)
)