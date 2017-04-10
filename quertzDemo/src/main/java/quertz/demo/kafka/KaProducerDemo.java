package quertz.demo.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KaProducerDemo<K> extends Thread{  
  
    private String topic;  
      
    public  KaProducerDemo(String topic){  
        this.topic = topic;  
    }  
      
      
    @Override  
    public void run() {  
    	KafkaProducer<String, Object> producer = createProducer();  
        int i=0;  
        while(true){  
        	producer.send(new ProducerRecord<String, Object>("test","demo发送:"+i));//topic value
            try {  
                TimeUnit.SECONDS.sleep(1);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
    private KafkaProducer<String, Object> createProducer() {
    	Map<String, Object> configs = new HashMap<String, Object>();
    	configs.put("bootstrap.servers", "localhost:9092");
    	configs.put("client.id", "DemoProducer");
    	configs.put("key.serializer", "kafka.serializer.StringEncoder");
    	configs.put("value.serializer", "kafka.serializer.StringEncoder");;
    	return  new KafkaProducer<String, Object>(configs);

     }  
      
      
    public static void main(String[] args) {  
        new KaProducerDemo("test").start();// 使用kafka集群中创建好的主题 test   
          
    }  
       
}  
  
  