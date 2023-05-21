DECLARE @customerId UNIQUEIDENTIFIER;

SET @customerId = NEWID();
INSERT INTO dbo.customers (id, first_name, last_name) VALUES (@customerId, 'John', 'Smith');
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 101, '48283749873')
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 103, '482-837-49-87')

SET @customerId = NEWID();
INSERT INTO dbo.customers (id, first_name, last_name) VALUES (@customerId, 'Jane', 'Smith');
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 101, '12387298273')

SET @customerId = NEWID();
INSERT INTO dbo.customers (id, first_name, last_name) VALUES (@customerId, 'John', 'Doe');
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 102, '728179713')
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 103, '123-151-42-33')

SET @customerId = NEWID();
INSERT INTO dbo.customers (id, first_name, last_name) VALUES (@customerId, 'Jack', 'Smith');
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 101, '59894303284')
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 102, '834758937')
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 103, '324-123-14-14')

SET @customerId = NEWID();
INSERT INTO dbo.customers (id, first_name, last_name) VALUES (@customerId, 'Jane', 'Doe');
INSERT INTO dbo.customer_identifiers (customer_id, identifier_id, identifier_value) VALUES (@customerId, 102, '456489898')
