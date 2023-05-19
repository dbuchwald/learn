-- Create a new database called 'appdb'
-- Connect to the 'master' database to run this snippet
USE master
GO
-- Create the new database if it does not exist already
IF NOT EXISTS (
    SELECT [name]
        FROM sys.databases
        WHERE [name] = N'appdb'
)
CREATE DATABASE appdb
GO

-- Select newly created database
USE appdb;
GO

-- Creates the login appadmin with password 'AppAdminP@ssw0rd'.  
CREATE LOGIN appadmin
    WITH PASSWORD = 'AppAdminP@ssw0rd';
GO

-- Creates a database user for the login created above.  
CREATE USER appadmin FOR LOGIN appadmin;
GO

-- Creates the login appuser with password 'AppUserP@ssw0rd'.  
CREATE LOGIN appuser
    WITH PASSWORD = 'AppUserP@ssw0rd';
GO

-- Creates a database user for the login created above.  
CREATE USER appuser FOR LOGIN appuser;
GO

-- Create application database schema
CREATE SCHEMA appdb;
GO

-- Add users to existing roles
ALTER ROLE db_ddladmin
  ADD MEMBER appadmin;
GO

ALTER ROLE db_datawriter
  ADD MEMBER appadmin;
GO

ALTER ROLE db_datareader
  ADD MEMBER appadmin;
GO

ALTER ROLE db_datawriter
  ADD MEMBER appuser;
GO

ALTER ROLE db_datareader
  ADD MEMBER appuser;
GO

