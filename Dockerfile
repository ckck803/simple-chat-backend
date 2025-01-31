FROM openjdk:17-jdk-slim
CMD ["./gradle", "clean", "build"]
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} chat-server.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=local", "chat-server.jar"]