web: java $JAVA_OPTS -Dspring.profiles.active=$PROFILES -Dspring.datasource.url=jdbc:$DB_URL -Dspring.datasource.username=$DB_USER -Dspring.datasource.password=$DB_PASSWORD -Dspring.datasource.driverClassName=org.postgresql.Driver -Dspring.jpa.hibernate.ddl-auto=create-drop -Dspring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect -Dserver.port=$PORT -jar interactive-data-spring-sample/target/interactive-data-spring-sample-*.jar