FROM gradle:8.5-jdk17 as build

WORKDIR /app
COPY . /app/
RUN gradle build --no-daemon

FROM openjdk:17-slim

WORKDIR /app
COPY --from=build /app/build/libs/*.jar /app/app.jar

# Create a directory for the H2 database file
RUN mkdir -p /app/data

# Expose the port the app runs on
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "/app/app.jar"]