FROM openjdk:8

COPY server-api-1.0.1.jar server-api.jar

#java -server -jar $runtime_file_name --spring.profiles.active=test > ./log.out  2>&1 &
ENTRYPOINT ["java","-server", "-jar", "/server-api.jar"]