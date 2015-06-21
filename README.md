# log4j2-kafka-appender
Kafka appender for log4j2. This appender uses new java kafka producer api for better performance and throughput. 

##Build
./gradlew jar //to generate jar

##Usage
```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="ERROR" packages="com.github.omkreddy.log4j2.appender">
	<Appenders>
		<Kafka name="KAFKALOGGER" topic="TEST">
			<Property name="bootstrap.servers">localhost:9091</Property>
		</Kafka>
	</Appenders>

	<Loggers>
		<Root level="info">
			<AppenderRef ref="KAFKALOGGER" />
		</Root>
	</Loggers>
</Configuration>
```
##Configuaration

###Appender attributes:

topic: topic name

enable : to enable/disable logging to kafka (default : true)

syncsend : to enable sync send (default : false)

```xml
<Appenders>
	<Kafka name="KAFKALOGGER" topic="TEST" enable="true" syncsend="true">
		<Property name="bootstrap.servers">localhost:9091</Property>
	</Kafka>
</Appenders>
```

###Appender Properties:

New java producer configaration properties (https://kafka.apache.org/documentation.html#newproducerconfigs) can be passed using Property tag.


```xml
<Appenders>
	<Kafka name="KAFKALOGGER" topic="TEST" enable="true" syncsend="true">
		<Property name="bootstrap.servers">localhost:9091</Property>
		<Property name="acks">1</Property>
 		<Property name="compression.type">gzip</Property>
	</Kafka>
</Appenders>
```




