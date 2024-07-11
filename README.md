# todo-app: Basic Spring to-do application REST API

### Tech Stack
- Java 17
- Spring Boot
- Spring Data Couchbase
- Spring Security
- OpenAPI v3
- MapStruct
- Maven
- Docker
- JUnit and Mockito

### Build
To build the project, run the following command in the project directory:
```
$ mvn install -f pom.xml
```

Add `-DskipTests=true` to command above, for skipping test while building.

### Deploy to Docker
To run the app on Docker, run the following commands respectively after successfully build:
```
$ docker-compose build
```

Or pull the project from DockerHub:
```
$ docker pull vlknakkaya/todoapp:latest
```

> [!IMPORTANT]
> Because of to-do app is using Couchbase, a Couchbase container with the configurations 
> below must be set.

### Couchbase Installation
to-do app uses Couchbase, so a Couchbase Server must be running alongside it. Here are 
the instructions to set up a Couchbase Server that application needs:

> [!NOTE]
> Container name must be `database`

After deploy and run the Couchbase image to Docker:
1. Go to the Couchbase Web UI using like `http://localhost:8091/ui/index.html`
2. Click `Setup New Cluster`
3. Create a new cluster with these parameters:
```
Cluster Name: todoapp
Admin Username: Administrator
Create Password: 123456
```
4. Accept the terms and conditions
5. Click `Finish With Defaults`
6. Go to `Buckets` from the left menu
7. Click `Add Bucket` and create a new bucket with the name `todoapp`

> These all manual configurations and instructions can be parametrized in next versions of project.

### Run
After Couchbase Server is set as properly, application can be run safety.
```
$ docker-compose up
```

### The REST API
After the startup of application, the REST API can be reachable on:
> http://localhost:8080/api-docs
> 
> or
> 
> http://localhost:8080/swagger-ui/index.html

to-do app has a security configuration, so a credential must be used when calling requests.

To create a user:
> http://localhost:8080/auth/signup

To login and take a token:
> http://localhost:8080/auth/login
