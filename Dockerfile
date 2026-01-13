FROM selenium/standalone-chrome:latest
USER root
WORKDIR /app
# Install Maven (no old chromedriver included)
RUN apt-get update && apt-get install -y maven
COPY . .
CMD ["mvn", "test"]