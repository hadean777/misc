psql -U miscmgr misc < rollback_ddl.sql
psql -U miscmgr misc < create_tables_ddl.sql
psql -U miscmgr misc < insert_tables_dml.sql
