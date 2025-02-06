FROM openjdk:17-jdk

WORKDIR /commit-craft

COPY build/libs/commit-craft-0.0.1-SNAPSHOT.jar commit-craft-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "commit-craft-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=dev"]
