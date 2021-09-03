package br.com.b3.tagg_poc.modules;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import br.com.b3.tagg_poc.domain.Topics;

public class TaggFilter {
	
	

	public static void main(String[] args) {
		Consumer<String, String> consumer = new KafkaConsumer<>(consumerProperties());
		consumer.subscribe(Arrays.asList(Topics.FIX_MESSAGES));
		
		Producer<String, String> producer = new KafkaProducer<>(producerProperties());
		
		try {
			while (true) {
				 ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
		         for (ConsumerRecord<String, String> record : records) {
		             System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
		             String trade = record.value();
		             if (shouldBeAggregated(trade)) {
		            	 producer.send(new ProducerRecord<String, String>(Topics.FIX_MESSAGES_TO_BE_AGGREGATED, trade));
		             } else {
		            	 producer.send(new ProducerRecord<String, String>(Topics.FIX_MESSAGES_NOT_TO_BE_AGGREGATED, trade));
		             }
		         }
			}
		} catch (Throwable e) {
			System.out.println("Error occured while consuming messages:");
			System.out.println(e);
		} finally {
			consumer.close();
			producer.close();
		}
	}

	private static boolean shouldBeAggregated(String trade) {
		return true;
	}

	private static Properties consumerProperties() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group-01");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		return props;
	}
	
	private static Properties producerProperties() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return props;
	}

}
