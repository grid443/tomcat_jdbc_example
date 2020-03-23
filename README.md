## Embedded Tomcat + JDBC example

### Build project

```
mvn clean package
```

### Start server

```
java -jar target/servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

Start server with debug

```
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -jar servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

By default server runs on 8080 port. You can specify another port 

```
java -jar servlet-app-1.0-SNAPSHOT-jar-with-dependencies.jar 8989
```

### Docker

Build docker

```
docker build -t servlet-app:1.0 .
```

Run docker image

```
docker run -it -p 8080:8080 --name servlet-app servlet-app:1.0
```

Run existing docker container

```
docker container start -ai servlet-app
```

###Check

You can check application using [curl](https://curl.haxx.se/)

Ubuntu:
```
sudo apt-get update
sudo apt-get install curl
curl --version
```
Windows:

1. [Download link](https://curl.haxx.se/windows/)
 
2. Add `%CURL_HOME%/bin` to `%PATH%` variable 

Check application availability

```
curl --request GET http://localhost:8080/ping
```

Load all objects from database

```
curl --request GET http://localhost:8080/person
```

Add new object to database

```
curl --request POST --header "Content-Type: application/json" --data '{"firstName":"Jonh","middleName":"R","lastName":"Smith","age":"27"}' http://localhost:8080/person
```

Add list of objects to database

```
curl --request POST --header "Content-Type: application/json" --data '[{"firstName":"Jonh1","middleName":"R1","lastName":"Smith1","age":"25"},{"firstName":"Jonh2","middleName":"R2","lastName":"Smith2","age":"35"}]' http://localhost:8080/persons
```
