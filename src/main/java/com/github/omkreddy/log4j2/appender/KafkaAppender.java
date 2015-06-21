/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.github.omkreddy.log4j2.appender;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.util.Booleans;

@SuppressWarnings("unused")
@Plugin(name = "Kafka", category = "Core", elementType = "appender", printObject = true)
public final class KafkaAppender extends AbstractAppender {

    private KafkaProducer<String, String> producer = null;
	private String topic;
    private boolean syncsend;
    
	protected KafkaAppender(String name, Filter filter, boolean ignoreExceptions, KafkaProducer<String, String> producer, String topic, boolean syncsend) {
		super(name, filter, null, ignoreExceptions);
		this.producer = producer;
		this.topic = topic;
		this.syncsend = syncsend;
	}

	@PluginFactory
	public static KafkaAppender createAppender(@PluginAttribute("name") final String name, @PluginElement("Filter") final Filter filter,
			@PluginAttribute("ignoreExceptions") final String ignore, @PluginAttribute("topic") final String topic,  @PluginAttribute("enable") String enable,
			@PluginAttribute("syncsend") String syncsend, @PluginElement("Properties") final Property[] properties) {
		boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
		boolean enableKafka = Booleans.parseBoolean(enable, true);
		boolean sync = Booleans.parseBoolean(syncsend, false);

		KafkaProducer<String, String> producer = null;
		Map<String, Object> props = new HashMap<String, Object>();
		for (Property property : properties) {
			System.out.println(property.getValue());
			props.put(property.getName(), property.getValue());
		}
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

		if(enableKafka)
		   producer = new KafkaProducer<String, String>(props);
	
		return new KafkaAppender(name, filter, ignoreExceptions, producer , topic, sync);
 
	}

	@Override
	public final void stop() {
		super.stop();
		if (producer != null) {
			producer.close();;
		}
	}

	public void append(LogEvent event) {
		try {
			if (producer != null) {
			    Future<RecordMetadata> result = producer.send(new ProducerRecord<String, String>(topic, event.getMessage().getFormattedMessage()));
			    if(syncsend)
			    	result.get();
			}
		} catch (final Exception e) {
			System.out.println("Error");
			LOGGER.error("Unable to write to kafka for appender [{}].",  this.getName(), e);
			throw new AppenderLoggingException("Unable to write to kafka in appender: " + e.getMessage(), e);
		} finally {
		}
	}

	@Override
	public Layout<? extends Serializable> getLayout() {
		return null;
	}

}
