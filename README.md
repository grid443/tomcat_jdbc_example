## Embedded Tomcat + JDBC example

Build project:

```
mvn clean package
```

```
cd target
```

Start server

```
java -jar servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Start server with debug

```
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

By default server runs on 8080 port. You can specify another port 


```
java -jar servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar 8989
```

Check application

```
curl --request GET http://localhost:8080/ping
```

Load all objects from database:

```
curl --request GET http://localhost:8080/person
```

Add new object to database:

```
curl --header "Content-Type: application/json" --request POST --data '{"firstName":"Jonh","middleName":"R","lastName":"Smith","age":"27"}' http://localhost:8080/person
```