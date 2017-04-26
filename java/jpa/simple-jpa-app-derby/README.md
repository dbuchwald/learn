This is simple JPA application using local instance of embedded Derby database, Liquibase and Hibernate Entity Manager.

Project contains several components:
 * Scripts to create and destroy database user and schema used in processing,
 * Liquibase migration scripts that populate the database in created user schema,
 * Java application that connects to database and retrieves list of Customer entities,
 * Script used to execute all the components in correct order.

*Please note: application uses local Derby database created in db folder. Correct execution in IDE might require working directory manipulation.*

Use `run_locally.sh` script to run the process.
