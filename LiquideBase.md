#### Generate Migrations from My Entity(Premier Choose a Faire)
./mvnw liquibase:generateChangeLog \
-Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/db.changelog-master.yaml

#### Apply the changes :
./mvnw liquibase:update


#### Generation des update ( any time we make an update in our entity's)
./mvnw liquibase:diff \
-Dliquibase.referenceUrl=hibernate:spring:com.example.yourpackage?dialect=org.hibernate.dialect.MySQLDialect \
-Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/db.changelog-${timestamp}.yaml

####  Apply the changes
./mvnw liquibase:update
