web: java $JAVA_OPTS -Dspring.profiles.active=$PROFILES -Dspring.datasource.url=jdbc:$DATABASE_URL -Dspring.datasource.username=$DB_USER -Dspring.datasource.password=$DB_PASSWORD -Dspring.datasource.driverClassName=org.postgresql.Driver -Dspring.jpa.hibernate.ddl-auto=create-drop -Dserver.port=$PORT -jar interactive-data-spring-sample/target/interactive-data-spring-sample-*.jar