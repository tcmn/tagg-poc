package br.com.b3.tagg_poc.modules;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import br.com.b3.tagg_poc.domain.Topics;

public class TaggFixServer {
	
	public static void main(String[] args) {
		Producer<String, String> producer = new KafkaProducer<>(producerProperties());
		
		try {
			int i = 0;
			while (i < 100) {
				producer.send(new ProducerRecord<String, String>(Topics.FIX_MESSAGES, "Fix Message " + i++));
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		producer.close();
	}

	private static Properties producerProperties() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return props;
	}

}
