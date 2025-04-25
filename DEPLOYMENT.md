# Deployment Guide for Grocery App API

This guide provides instructions for deploying the Grocery App API to various cloud platforms.

## Prerequisites

- Docker installed on your local machine
- An account on the cloud platform of your choice (Heroku, AWS, Google Cloud, etc.)

## Building the Docker Image

1. Navigate to the project root directory
2. Build the Docker image:
   ```
   docker build -t grocery-app-api .
   ```
3. Test the image locally:
   ```
   docker run -p 8080:8080 grocery-app-api
   ```
   The API should now be accessible at http://localhost:8080

## Deployment Options

### Heroku

1. Install the Heroku CLI and log in:
   ```
   heroku login
   ```

2. Create a new Heroku app:
   ```
   heroku create your-app-name
   ```

3. Log in to Heroku Container Registry:
   ```
   heroku container:login
   ```

4. Build and push the Docker image:
   ```
   heroku container:push web -a your-app-name
   ```

5. Release the container:
   ```
   heroku container:release web -a your-app-name
   ```

Your API will be available at `https://your-app-name.herokuapp.com`

### AWS Elastic Beanstalk

1. Install the AWS CLI and EB CLI
2. Initialize EB CLI:
   ```
   eb init
   ```

3. Create an environment:
   ```
   eb create your-environment-name
   ```

4. Deploy the application:
   ```
   eb deploy
   ```

### Google Cloud Run

1. Install the Google Cloud SDK
2. Authenticate with Google Cloud:
   ```
   gcloud auth login
   ```

3. Set your project:
   ```
   gcloud config set project your-project-id
   ```

4. Build and push the Docker image:
   ```
   gcloud builds submit --tag gcr.io/your-project-id/grocery-app-api
   ```

5. Deploy to Cloud Run:
   ```
   gcloud run deploy grocery-app-api --image gcr.io/your-project-id/grocery-app-api --platform managed
   ```

## Database Considerations

The application currently uses H2 database with file-based storage. For a production environment, consider:

1. Using a managed database service (RDS on AWS, Cloud SQL on GCP)
2. Updating the database connection in `src/main/kotlin/module/initDatabase.kt`
3. Adding environment variables for database credentials

Example configuration for PostgreSQL:
```kotlin
fun initDatabase() {
    val dbUrl = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/grocery"
    val dbUser = System.getenv("DATABASE_USER") ?: "postgres"
    val dbPassword = System.getenv("DATABASE_PASSWORD") ?: "postgres"
    
    Database.connect(
        url = dbUrl,
        driver = "org.postgresql.Driver",
        user = dbUser,
        password = dbPassword
    )
    
    transaction {
        SchemaUtils.create(Products, Users)
    }
}
```

## Environment Variables

The application is configured to use the following environment variables:

- `PORT`: The port on which the server will run (default: 8080)

## Monitoring and Logging

For production deployments, consider setting up:

1. Application monitoring (New Relic, Datadog, etc.)
2. Centralized logging (ELK Stack, Graylog, etc.)
3. Alerting for critical errors

## Scaling

For high-traffic scenarios:

1. Deploy multiple instances behind a load balancer
2. Use a more robust database solution
3. Consider implementing caching for frequently accessed data