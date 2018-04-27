package com.kafka.examples;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class KafkaProducerExample {
	private final static String TOPIC = "my-example-topic";
	private static String BOOTSTRAP_SERVERS = "localhost:9092";

	private static KafkaProducer<Long,String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class.getName());
		return new KafkaProducer(props);
	}


	static void runProducer(final int sendMessageCount) throws Exception {
		final KafkaProducer<Long, String> producer = createProducer();
		long time = System.currentTimeMillis();
		try {
			for (long index = time; index < time + sendMessageCount; index++) {
				final ProducerRecord<Long, String> record =
						new ProducerRecord<>(TOPIC, 1l,
								"Hello  " + 1l);
				RecordMetadata metadata = producer.send(record).get();
				long elapsedTime = System.currentTimeMillis() - time;
				System.out.printf("sent record(key=%s value=%s) " +
						"meta(partition=%d, offset=%d) time=%d\n",
						record.key(), record.value(), metadata.partition(),
						metadata.offset(), elapsedTime);
			}
		} finally {
			producer.flush();
			producer.close();
		}
	}



	public static void main(String... args) throws Exception {
		if (args.length == 1) {
			BOOTSTRAP_SERVERS = args[0];
		} else if (args.length == 2){
			BOOTSTRAP_SERVERS = args[0];
			runProducer(Integer.parseInt(args[1]));
		} else {
			System.out.println("Provide proper arguments, Exiting");
		}
	}
}