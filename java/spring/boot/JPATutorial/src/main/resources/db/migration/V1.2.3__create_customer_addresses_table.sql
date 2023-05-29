CREATE TABLE dbo.customer_addresses
(
    address_id UNIQUEIDENTIFIER NOT NULL,
    customer_id UNIQUEIDENTIFIER NOT NULL,
    address_type CHAR(1) NOT NULL,    
    street_address VARCHAR(128) NOT NULL,
    zip_code VARCHAR(16) NOT NULL,
    city VARCHAR(32) NOT NULL,
    country VARCHAR(64) NOT NULL,
    CONSTRAINT PK_customer_addresses_address_id PRIMARY KEY CLUSTERED(address_id),
    CONSTRAINT FK_customer_addresses_customer_id FOREIGN KEY (customer_id) REFERENCES dbo.customers (id),
    CONSTRAINT UN_customer_addresses_customer_id_type UNIQUE (customer_id, address_type)
)

CREATE UNIQUE INDEX IDX_customer_addresses_customer_id ON dbo.customer_addresses(customer_id, address_type)