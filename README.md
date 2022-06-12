# Tweet-Repository

## SECTION A - Maven Build:  
1. Tweet Service:  
![image](https://user-images.githubusercontent.com/104539687/173216312-4901d028-74af-4ac3-a1a0-388b5776b6e1.png)
  
2. Authorization Service:  
![image](https://user-images.githubusercontent.com/104539687/173216470-4b7f6667-7599-4345-af8e-95bf324db120.png)
    
## SECTION B - Sonar Build Command:  
mvn clean verify sonar:sonar -Dsonar.projectKey=TweetAppLocal -Dsonar.host.url=http://localhost:9000 -Dsonar.login=4750b9fc8471246357269fb6aa81ed9e9f9c710d  
  
## SECTION C - Maven surefire report commands:  
mvn site  
mvn surefire-report:report  
mvn site -DgenerateReports=false  
  
## SECTION D - Prometheus/Grafana Commands:  
docker run -d -p 9090:9090 -v <absolute_path_to_your_prometheus_file>:/etc/prometheus/prometheus.yml prom/prometheus  
example: docker run -d -p 9090:9090 -v /mydisk/monitoring/src/main/resources/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus  

docker run -d -p 3000:3000 grafana/grafana  

## SECTION E - Docker Steps:  
1. Perform Maven clean and build for all the services as shown in SECTION A  
2. Run the following commands in the respective service folders:  
      a. docker build --tag=tweet-service .  
      b. docker build --tag=authorization .  
3. Run the following command in the outermost TweetRepository folder where docker-compose.yml is present:  
      a. docker compose up  
      b. To Stop: Ctrl + C (and/or) docker compose down  
