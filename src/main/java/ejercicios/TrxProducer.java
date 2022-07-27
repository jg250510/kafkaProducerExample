package ejercicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class TrxProducer {
	

	public static void main(String[] args) {
		
		List<String> movimientos = new ArrayList<>();
		
		movimientos.add("200");
		movimientos.add("100");
		movimientos.add("200");
		movimientos.add("-300");
		movimientos.add("100");
		movimientos.add("250");
		
		
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		
		try(Producer<String,String> producer = new KafkaProducer<>(props);){
			
			for(int i = 0; i<movimientos.size(); i++) {
				producer.send(new ProducerRecord<String,String>("jm-test", (movimientos.get(i).contains("-"))?"Key-1":"Key-2", movimientos.get(i)));
			}
			producer.flush();
		}
		

	}

}
