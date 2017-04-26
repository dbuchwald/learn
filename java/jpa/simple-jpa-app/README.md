This is simple JPA application using local instance of MySQL database, Liquibase and Hibernate Entity Manager.

Project contains several components:
 * Scripts to create and destroy database user and schema used in processing,
 * Liquibase migration scripts that populate the database in created user schema,
 * Java application that connects to database and retrieves list of Customer entities,
 * Script used to execute all the components in correct order.

*Please note: local instance of MySQL database listening on port 3306 is required for correct operation. Necessary server can be set up using `learn/vagrant` scripts.*

Use `run_locally.sh` script to run the process.
