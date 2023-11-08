FROM openjdk:8

COPY target/natsukashiiz-server-shop-0.0.1-SNAPSHOT.jar server.jar

#java -server -jar $runtime_file_name --spring.profiles.active=test > ./log.out  2>&1 &
ENTRYPOINT ["java","-server", "-jar", "/server.jar", "--spring.profiles.active=test"]