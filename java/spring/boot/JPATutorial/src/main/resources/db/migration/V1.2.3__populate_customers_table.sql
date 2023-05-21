DECLARE @customerId UNIQUEIDENTIFIER;

SET @customerId = NEWID();
INSERT INTO dbo.customer (id, first_name, last_name) VALUES (@customerId, 'John', 'Smith');
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'PESEL', '48283749873')
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'NIP', '482-837-49-87')

SET @customerId = NEWID();
INSERT INTO dbo.customer (id, first_name, last_name) VALUES (@customerId, 'Jane', 'Smith');
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'PESEL', '12387298273')

SET @customerId = NEWID();
INSERT INTO dbo.customer (id, first_name, last_name) VALUES (@customerId, 'John', 'Doe');
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'REGON', '728179713')
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'NIP', '123-151-42-33')

SET @customerId = NEWID();
INSERT INTO dbo.customer (id, first_name, last_name) VALUES (@customerId, 'Jack', 'Smith');
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'PESEL', '59894303284')
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'REGON', '834758937')
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'NIP', '324-123-14-14')

SET @customerId = NEWID();
INSERT INTO dbo.customer (id, first_name, last_name) VALUES (@customerId, 'Jane', 'Doe');
INSERT INTO dbo.customer_identifier (customer_id, identifier_country, identifier_type, identifier_value) VALUES (@customerId, 'PL', 'REGON', '456489898')
