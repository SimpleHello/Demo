package quertz.demo.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class quartzServiceonfig {
	public static void main(String[] args) throws Exception {  
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[] { "classpath:conf/applicationContext.xml" });  
        context.start();  
        System.in.read();  
    } 
}

