FROM openjdk:8

COPY natsukashiiz-server-shop-0.0.1-SNAPSHOT.jar shop-server.jar

#java -server -jar $runtime_file_name --spring.profiles.active=test > ./log.out  2>&1 &
ENTRYPOINT ["java","-server", "-jar", "/shop-server.jar"]