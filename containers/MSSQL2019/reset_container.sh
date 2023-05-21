docker exec mssql_2019 /opt/mssql-tools/bin/sqlcmd \
            -S localhost \
            -U SA \
            -P "MSSQL_Strong_P@ssw0rd" \
            -i /tmp/reset_database.sql