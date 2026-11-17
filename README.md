# Simple Java Web Application

A small Java 17 web application with no external dependencies. It serves a welcome page at `/` and a health check at `/health`.

## Run locally with Docker

You will need Docker installed and running.

1. Build the image from the repository root:

   ```sh
   docker build -t simple-java-webapp .
   ```

2. Start the container:

   ```sh
   docker run --name simple-java-webapp -p 8080:8080 simple-java-webapp
   ```

3. Open [http://localhost:8080](http://localhost:8080) in your browser. The health check is available at [http://localhost:8080/health](http://localhost:8080/health).

4. Stop and remove the container with `Ctrl+C`, then run:

   ```sh
   docker rm simple-java-webapp
   ```

To run it in the background, add `-d` to the `docker run` command. Then stop it with `docker stop simple-java-webapp` before removing it.

## GitHub

Pushes and pull requests run the GitHub Actions workflow in `.github/workflows/docker.yml`, which builds the Docker image to verify the application can be packaged.
