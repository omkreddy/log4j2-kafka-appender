# log4j2-kafka-appender
Kafka appender for log4j2. This appender uses new java kafka producer api. 

##Build
#./gradlew jar //This will generate jar

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

