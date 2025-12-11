#!/usr/bin/bash
docker run -d -p 9092:9092 --name kafka apache/kafka:latest
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:management
docker ps --format 'table {{.Names}}\t{{.ID}}\t{{.Image}}\t{{.Status}}'
#gradle bootRun --args="--spring.integration.poller.fixed-delay=5000"
#gradle cashcard-transaction-enricher:bootRun --args="--spring.cloud.stream.bindings.enrichTransaction-in-0.destination=approvalRequest-out-0"
#gradle cashcard-transaction-sink:bootRun --args="--spring.cloud.function.definition=sinkToConsole;cashCardTransactionFileSink --spring.cloud.stream.bindings.cashCardTransactionFileSink-in-0.destination=enrichTransaction-out-0 --spring.cloud.stream.bindings.sinkToConsole-in-0.destination=enrichTransaction-out-0"

# Kafka
docker exec -it kafka /opt/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic approvalRequest-out-0

# RabbitMQ
docker exec -it rabbitmq sh -c "rabbitmqadmin declare queue name=test-queue && rabbitmqadmin declare binding source=approvalRequest-out-0 destination=test-queue routing_key=#"