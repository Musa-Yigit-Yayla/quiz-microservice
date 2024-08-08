
#this will serve as the base image of our container
FROM openjdk:21-jdk

#now we should specify a working directory in the container's filesystem
WORKDIR /app

#pass the jar file to container
COPY target/quiz-service-0.0.1-SNAPSHOT.jar /app/quiz-service.jar

COPY src/main/resources/application.properties /app/application.properties


#expose port which will be listened by our container
EXPOSE 8082

#execute jar
CMD ["java", "-jar", "quiz-service.jar"]