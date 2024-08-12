A quiz microservice implemented in spring boot where tests, and test questions can be mainted, shared among multiple users. 

Steps to get project up:

1-) Build the jar file in dir/quiz-service: 
  >> cd <yourdir>/quiz-service
  >> mvn clean install

2-) Build a docker image with name "quiz-service" and tag "newer" (for using kubernetes environment).
  >> cd <yourdir>/quiz-service
  >> docker build -t quiz-service:newer .

3-)
  If using local MySQL db instance:
  
      Create a local mysql database instance. 
      Adjust application.properties and DataConfig.java for establishing connection to your db instance.
      Run the spring app in your local machine or create a docker container from the image you have built.

  If wanting to connect to kubernetes environment
  
      Using kind kubernetes:
      Create a cluster using: 
        >> kind create cluster
      Load the docker image into kind cluster:
        >> kind load docker-image quiz-service:newer
      Apply yaml files to create kubernetes pods, deployments and services: 
        >> cd <yourdir>/quiz-service/kubernetes
        >> kubectl apply -f deployment.yaml
        >> kubectl apply -f service.yaml
        >> kubectl apply -f mysql-deployment.yaml
        >> kubectl apply -f mysql-service.yaml
      Create the database tables by either:
          -manually accessing to database instance in mysql-service and executing query definitions in schema.sql OR
          -creating a configmap with the contents of schema.sql
      Port forward to a port in our local machine. You will need to adjust "port" and "targetPort" for connectiong to another port from 8082. 
        >> kubectl port-forward <quiz-service-pod> 8082:8082
        
  4-) Interact via sending HTTP requests to endpoints. 
![Screenshot (89)](https://github.com/user-attachments/assets/dfe02e5f-1279-4bc6-864e-8006667926e0)
![Screenshot (90)](https://github.com/user-attachments/assets/6180af06-20d8-4b92-b309-d67ebc598bd9)
