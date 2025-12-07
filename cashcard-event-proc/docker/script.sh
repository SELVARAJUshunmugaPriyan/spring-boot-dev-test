#!/usr/bin/bash
docker run -d -p 9092:9092 --name kafka apache/kafka:latest
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
docker ps --format 'table {{.Names}}\t{{.ID}}\t{{.Image}}\t{{.Status}}'
#./gradlew bootRun --args="--spring.integration.poller.fixed-delay=5000"

# Kafka
docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic approvalRequest-out-0

# RabbitMQ
docker exec -it rabbitmq sh -c "rabbitmqadmin declare queue name=test-queue && rabbitmqadmin declare binding source=approvalRequest-out-0 destination=test-queue routing_key=#"