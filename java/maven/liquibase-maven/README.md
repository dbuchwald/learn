This is example of Maven project using Liquibase plugin to execute database migration procedure. 

The application itself is irrelevant - the main processing is controlled by Liquibase migration scripts contained in `src/main/resources/liquibase`. Execution depends on MySQL server listening on local machine port 3306. Necessary server can be started using scipts in `learn/vagrant` directory.

Use `run.sh` to execute code. *Please note: it will create user schema for the migration, run the migration and drop the schema immediately afterwards.*
