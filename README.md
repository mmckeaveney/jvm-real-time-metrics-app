JVM Real Time Metrics System

The JVM Real Time Metrics system is a Linux based, real time metrics system created using Spring Boot and React JS. To get it set up and to start collecting metrics from your Docker environment, there are some things you have to do first. 

Software requirements
- Java 8
- Maven 3.x
- Docker

Your docker daemon must be running on a TCP port. Run the docker daemon using the following command

- docker daemon -H tcp://0.0.0.0:2376

Then run your docker images using the typical docker run command. Now that you have the environment up and running, you can now use the JVM Real Time Metrics System.

- run mvn spring-boot:run in the jvm-real-time-metrics-app directory.
- Go to localhost:8090 in the browser. (obviously this won't be localhost if cloud hosted)

You are ready to go! Enjoy using the JVM Real Time Metrics System.

