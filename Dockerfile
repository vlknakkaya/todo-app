FROM amazoncorretto:17
RUN mkdir /app
WORKDIR /app
COPY target/*.jar todo-app.jar
ENTRYPOINT ["java","-jar","/app/todo-app.jar"]