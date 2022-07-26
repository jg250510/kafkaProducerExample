package producers;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaProducer {

	public static final Logger log = LoggerFactory.getLogger(JavaProducer.class);

	public static void main(String[] args) {
		
		long startTime = System.currentTimeMillis();
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		try(Producer<String,String> producer = new KafkaProducer<>(props);){
			producer.send(new ProducerRecord<String,String>("jm-test", "jm-key","jm-value"));
			for(int i = 0; i<30; i++) {
				producer.send(new ProducerRecord<String,String>("jm-test", String.valueOf(i),"jm-value"));
			}
			producer.flush();
		}
		log.info("Processing time = {} ms ",System.currentTimeMillis()-startTime);

	}

}
