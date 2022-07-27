package transactional;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TransactionalProducer {
	
	public static final Logger log = LoggerFactory.getLogger(TransactionalProducer.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("transactional.id", "jm-test-producer-id");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		try(Producer<String,String> producer = new KafkaProducer<>(props);){
//			producer.send(new ProducerRecord<String,String>("jm-test", "jm-key","jm-value"));
			
			producer.initTransactions();
			producer.beginTransaction();
			for(int i = 0; i<100000; i++) {
				producer.send(new ProducerRecord<String,String>("jm-test", String.valueOf(i),"jm-value"));
			}
			producer.commitTransaction();
			producer.flush();
		}
		
		log.info("Processing time = {} ms ",System.currentTimeMillis()-startTime);

	}

}
