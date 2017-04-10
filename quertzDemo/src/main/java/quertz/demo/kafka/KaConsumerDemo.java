package quertz.demo.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

public class KaConsumerDemo {
	private final ConsumerConnector consumer; 
	public final static String TOPIC = "test";
	
	 private KaConsumerDemo() {    
         Properties props = new Properties();       
         // zookeeper 配置    
         props.put("zookeeper.connect", "localhost:2181");    
         // 消费者所在组    
         props.put("group.id", "testgroup");    
         props.put("consumer.id", "c.nick");  
         ConsumerConfig config = new ConsumerConfig(props);    
         consumer = (ConsumerConnector) Consumer.createJavaConsumerConnector(config);    
  }  
	 void consume() {    
		 Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
         topicCountMap.put(TOPIC, new Integer(1));
         StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());  
         StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());  
         Map<String, List<KafkaStream<String, String>>> consumerMap = consumer.createMessageStreams(topicCountMap,keyDecoder,valueDecoder);
         KafkaStream<String, String> streams = consumerMap.get(TOPIC).get(0);
         ConsumerIterator<String, String> it = streams.iterator(); 
         int messageCount = 0;    
         while (it.hasNext()){    
             System.out.println(it.next().message());    
             messageCount++;    
             if(messageCount == 100){    
                 System.out.println("Consumer端一共消费了" + messageCount + "条消息！");    
             }    
         }    
     }  
     public static void main(String[] args) {  
         new KaConsumerDemo().consume();    
    }  
}
