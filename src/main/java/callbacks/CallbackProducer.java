package callbacks;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CallbackProducer {
	
	public static final Logger log = LoggerFactory.getLogger(CallbackProducer.class);

	public static void main(String[] args) {
		

		// TODO Auto-generated method stub
		long startTime = System.currentTimeMillis();
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
	
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		try(Producer<String,String> producer = new KafkaProducer<>(props);){
//			producer.send(new ProducerRecord<String,String>("jm-test", "jm-key","jm-value"));
			
			
			for(int i = 0; i<10000; i++) {
				producer.send(new ProducerRecord<String,String>("jm-test", String.valueOf(i),"jm-value"), new Callback() {
					
					@Override
					public void onCompletion(RecordMetadata metadata, Exception exception) {
						
						if(exception != null) {
							log.info("There was an error {}", exception.getMessage());
						}else {
							log.info("Offset = {} , Partition = {}, Topic = {}", metadata.offset(),metadata.partition(),metadata.topic());
						}
						
					}
				});
			}
		
			producer.flush();
		}
		
		log.info("Processing time = {} ms ",System.currentTimeMillis()-startTime);

	}

}
