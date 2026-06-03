FROM eclipse-temurin:25-jdk
COPY target/couponservice-0.0.1-SNAPSHOT.jar couponservice.jar
ENTRYPOINT [ "java","-jar","/couponservice.jar" ]