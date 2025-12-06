#!/usr/bin/bash
docker run -d -p 9092:9092 --name kafka apache/kafka:latest
docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic approvalRequest-out-0

#./gradlew bootRun --args="--spring.integration.poller.fixed-delay=5000"