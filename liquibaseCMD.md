# ==========================================================
# LIQUIBASE MAVEN COMMAND REFERENCE
# ==========================================================
# Note: 'process-resources' is included to ensure changelogs
#       are copied to target/classes before Liquibase runs.

# === CORE COMMANDS (for 'dev' profile) ===

# Validate changelog syntax
mvn process-resources liquibase:validate

# Check which changesets have NOT been run
mvn process-resources liquibase:status

# Apply all pending changesets to the database
mvn process-resources liquibase:update

# Roll back the single last changeset
mvn process-resources liquibase:rollback -Dliquibase.rollbackCount=1

# Roll back the last 5 changesets
mvn process-resources liquibase:rollback -Dliquibase.rollbackCount=5

# Roll back all changes applied on a specific date
mvn process-resources liquibase:rollbackToDate -Dliquibase.rollbackDate="2025-11-05"


# === WORKFLOW FOR 'qa' PROFILE (with Vault) ===

# 1. Set your Vault token
export VAULT_TOKEN="root"

# 2. Fetch secrets from Vault (using the LOGICAL path)
export QA_DB_URL=$(vault kv get -field=DB_URL secret/optiroute)
export QA_DB_USER=$(vault kv get -field=DB_USERNAME secret/optiroute)
export QA_DB_PASS=$(vault kv get -field=DB_PASSWORD secret/optiroute)

# 3. Run a command (e.g., update) on 'qa'
mvn process-resources liquibase:update -Pqa \
-Ddb.url=$QA_DB_URL \
-Ddb.username=$QA_DB_USER \
-Ddb.password=$QA_DB_PASS

# 4. Run a different command (e.g., rollback) on 'qa'
mvn process-resources liquibase:rollback -Pqa -Dliquibase.rollbackCount=1 \
-Ddb.url=$QA_DB_URL \
-Ddb.username=$QA_DB_USER \
-Ddb.password=$QA_DB_PASS


# === ADVANCED COMMANDS ===

# Compare your Hibernate entities to your DB schema
# Note: Requires adding 'referenceUrl' back to your pom.xml
mvn process-resources liquibase:diff