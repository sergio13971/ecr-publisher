# Project developed for testing purposes, exposes a rest service which receive some parameters to build and send a message to a Mqtt Server. To communicate with the service there is a tiny web enabled throug http://localhost:8080/index where the user can set sender id, destination id and command.

Prerrequisites:

-At least Java 1.8
-At least Maven 3.0.5

Build application:

From root path application execute to compile project: mvn -U package
If everything goes right (build successfully) execute to start spring-boot application: mvn spring-boot:run

Regards.
