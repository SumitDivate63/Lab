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

## Run Jenkins locally with Docker

Jenkins runs on host port `8081`, so it can run alongside the application on port `8080`. These commands assume Linux and a running Docker daemon.

1. Build the Jenkins image from the repository root:

   ```sh
   docker build -f Dockerfile.jenkins -t local-jenkins .
   ```

2. Start Jenkins in the background. The Docker socket lets Jenkins build the application image using the host Docker daemon:

   ```sh
   docker run -d --name jenkins \
     -p 8081:8080 -p 50000:50000 \
     -v jenkins_home:/var/jenkins_home \
     -v /var/run/docker.sock:/var/run/docker.sock \
     --group-add "$(stat -c '%g' /var/run/docker.sock)" \
     local-jenkins
   ```

   The Docker socket grants Jenkins control over the host Docker daemon; use this setup only for trusted local development.

3. Open [http://localhost:8081](http://localhost:8081). Get the initial administrator password with:

   ```sh
   docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
   ```

   Follow the setup wizard and install the suggested plugins.

4. Create a **Pipeline** job in Jenkins. Under **Pipeline**, choose **Pipeline script from SCM**, select **Git**, enter this repository's URL, set **Branches to build** to `*/main`, and leave **Script Path** as `Jenkinsfile`. For a private repository, configure its credentials here.

5. Click **Build Now** for the first run. The job checks out `main` and creates `simple-java-webapp:<build-number>` in the host Docker daemon; it does not replace or restart an already running application container. Afterward, Jenkins polls the configured branch every five minutes and rebuilds when it detects changes.

Stop Jenkins with `docker stop jenkins`. Its configuration remains in the `jenkins_home` volume, so it is available again when you start the container.

## GitHub

Pushes and pull requests run the GitHub Actions workflow in `.github/workflows/docker.yml`, which builds the Docker image to verify the application can be packaged.
