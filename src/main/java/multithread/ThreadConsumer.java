package multithread;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadConsumer implements Runnable {

	private final KafkaConsumer<String, String> consumer;
	private final AtomicBoolean closed = new AtomicBoolean(false);
	private static final Logger log = LoggerFactory.getLogger(ThreadConsumer.class);

	public ThreadConsumer(KafkaConsumer<String, String> consumer) {
		this.consumer = consumer;
	}

	@Override
	public void run() {
		this.consumer.subscribe(Arrays.asList("jm-test"));

		try {
			while (!closed.get()) {
				ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));

				for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
					log.info("Offset = {}, Partition = {}, Key = {}, Value = {} ", consumerRecord.offset(),
							consumerRecord.partition(), consumerRecord.key(), consumerRecord.value());
				}
			}

		} catch (WakeupException e) {
			if (!closed.get())
				throw e;
		} finally {
			consumer.close();
		}

	}

	public void shutdown() {
		closed.set(true);
		consumer.wakeup();
	}

}
