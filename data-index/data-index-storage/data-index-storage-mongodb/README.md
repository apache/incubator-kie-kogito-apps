To use MongoDB as storage for data index service:
1. Add `data-index-storage-mongodb` as maven dependency for `data-index-service`:
    ```
        <dependency>
            <groupId>org.kie.kogito</groupId>
            <artifactId>data-index-storage-mongodb</artifactId>
        </dependency>
    ```
2. Add mongo application properties in the `application.properties` of data index service, and use `mongodb` as the configuration profile:
    ```
        # MongoDB server address
        %mongodb.quarkus.mongodb.connection-string=mongodb://localhost:27017
        # MongoDB database name for data index 
        %mongodb.quarkus.mongodb.database=kogito
        # Set data index to use MongoDB for storage
        %mongodb.kogito.persistence.type=mongodb
    ```
3. Run data index service with the `mongodb` configuration profile activated. For example, run data index service in dev mode:
    ```
        mvn clean compile quarkus:dev -Dquarkus.profile=mongodb
    ```