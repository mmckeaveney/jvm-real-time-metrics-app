JVM Real Time Metrics System

The JVM Real Time Metrics system is a Linux based, real time metrics system created using Spring Boot and React JS. To get it set up and to start collecting metrics from your Docker environment, there are some things you have to do first. 

UBUNTU
- Your docker daemon must be running on the same host as your containers. Edit this as follows:

sudo vim (or preferred text editor) /etc/docker/default

