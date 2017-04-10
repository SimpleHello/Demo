package demo.cloud.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
public class SampleController  {
 
    @ResponseBody
    @RequestMapping(value = "/")
    String home() {   
        return "Hello World!";
    }
 
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SampleController.class,  "--server.port=8081");
    }
 
}