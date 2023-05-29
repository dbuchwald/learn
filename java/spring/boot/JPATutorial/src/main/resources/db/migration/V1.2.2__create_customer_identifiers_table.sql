CREATE TABLE dbo.customer_identifiers
(
    customer_id UNIQUEIDENTIFIER NOT NULL,
    identifier_id INT NOT NULL,
    identifier_value VARCHAR(64) NOT NULL,
    CONSTRAINT PK_customer_identifiers_customer_identifier PRIMARY KEY CLUSTERED(customer_id, identifier_id),
    CONSTRAINT FK_customer_identifiers_customer_id FOREIGN KEY (customer_id) REFERENCES dbo.customers (id),
    CONSTRAINT FK_customer_identifiers_identifier_id FOREIGN KEY (identifier_id) REFERENCES dbo.identifier_types (id)
)