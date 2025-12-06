===================
  INTRODUCTION
===================

0. (on-demand REST API/
      fixed schedule)                                   2. consuming txns>
[SOURCE_APPLICATION] -----------------> [MESSAGE_BROKER]<================>[ENRICHER_APPLICATION]
                    1. (publishing txns)        |       <3. publishing enriched txn
                                    4. consuming|enriched txns
                                                v      5. publishing enriched txn
                                        [SINK_APPLICATION]--------------->[FILE_CONSOLE,DATABASE,ETC.]
===================
  Why Event Driv-
  en Systems?
===================
Today's enterprises are inundated with data events. These events come in all kinds of structures and shapes, or no structure at all. At the heart of event-driven systems are two activities, one where the system generates events, and another that consumes events. The former is generally known as a producer or publisher application, and the latter is called a consumer or receiver application. An event-driven system can also perform both activities in a single application – by consuming on one end, and producing on the other. When doing so, such an application is known as a processor. Almost all of these data event systems require high throughput and low latency.
===================
  A Bit of Archi-
  tecture
===================
Modern microservices architecture allows an application to follow the single responsibility principle, focussing on one core activity. This allows services in event-driven systems to be loosely-coupled allowing them to follow on their part alone. Event-driven microservices communicate through a messaging middleware, known as the message broker.

> Messaging systems: 
    >> RabbitMQ 
    >> Apache Kafka
    >> Apache Pulsar
    >> AWS Kinesis
    >> Google PubSub
    >> Azure EventHubs

[PRODUCER(1..n)] --publishing data--> [MESSAGE_BROKER] --consuming data--> [CONSUMER(1..n)]

The producer application knows nothing about the consumer application and vice versa, and they both operate in an asynchronous manner. In this version, we are scaling the single producer application we saw earlier to multiple running instances, all publishing the events to the same destination on the message broker. Similarly, on the consumer side, the events are delivered to different consumers instead of one.

In a properly designed event-driven system, scaling like this does not require any application level code changes, only configuration or operational changes. Data at rest is batch processing, data in motion is stream processing. In a traffic app, or the video/audio streaming apps, data is constantly flowing in these systems, and the event-driven microservices need to process the information in near real-time, almost processing precisely on a real-time basis. When event-driven applications process data in near real-time regularly, the applications belong to another class of applications known as stream-processing. Stream processing systems: Kafka Streams (Kafka), Apache Flink, and Apache Spark Streaming.

> Common Misconceptions
    >> The network is reliable
    >> Latency is zero
    >> Bandwidth is infinite
    >> The network is secure
    >> The network topology doesn't change
    >> There is only one network system administrator
    >> The transport cost is zero
    >> The network is homogenous

> The CAP Theorem: Consistency, Availability, Partitioning
    >> Consistency (C) - The reads receive the most recent write of the data, thus giving consistency.
    >> Availability (A) - All the nodes in a system can read and write, even when there is a network failure
    >> Partitioning (P) - Partitioning means a network partitioning or failure between members. In other words, the failure of one of more nodes or connections within a network.

When a system provides a CA guarantee, it usually provides consistency by using replication strategies on the broker nodes and making them available for data reads and writes. If a system provides an AP guarantee, it provides high availability on the nodes and the ability to serve them - even when a network failure occurs due to a network partition. Some brokers provide CA (Consistency and High Availability), while others provide AP (High Availability and Partitioning).
===================
  Overview: Spri-
  ng Cloud Stream
===================
Kafka has the low-level Kafka wire protocol that the broker understands so we don't have to.
Apache Kafka also provides language-specific client libraries, such as the Java client. This is still low-level and complex. Spring tools - Spring for AMQP (RabbitMQ), Spring for Apache Kafka and Spring for Apache Pulsar.
At core, Spring Cloud Stream framework abstracts the low-level messaging details of each messaging broker platform. It achieves this by providing a consistent programming model, regardless of an enterprise's messaging platform. Spring Cloud Stream is a Spring Boot-based framework in the family of Spring Cloud projects. Needed dependencies - 
> Spring Cloud Stream core module - spring-cloud-stream
> A broker-specific binder for Spring Cloud Stream - such as spring-cloud-stream-binder-rabbit or spring-cloud-stream-kafka.

    ------------------                                         ------------------
    |   Spring Boot  |                                         |   Spring Boot  |
    |   Application  |                                         |   Application  |
    | -------------- |                                         | -------------- |
    || Spring Cloud ||                                         || Spring Cloud ||
    ||    Stream    ||                                         ||    Stream    ||
    | -------------- |----------->[MESSAGE_BROKER]------------>| -------------- |
    | -------------- |                                         | -------------- |
    ||   Producer   ||                                         ||   Consumer   ||
    ||  Application ||                                         ||  Application ||
    | -------------- |                                         | -------------- |
    ------------------                                         ------------------