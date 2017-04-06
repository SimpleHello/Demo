package quertz.demo.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quertz.demo.entity.log.JobRunningLogEntity;
import quertz.demo.service.JobLogService;

@Service
public class TestQuartz3 implements Job{

	@Autowired
	JobLogService logService;


    public void execute(JobExecutionContext jobExecutionContext) {
    	JobRunningLogEntity entity = new JobRunningLogEntity("TestQuartz3","测试 3跑批类","quertz.demo.test.TestQuartz3","","",new Date());
    	try{
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	System.out.println("**********测试 3 跑批类*** " +sdf.format(new Date()));
        	entity.setResult("success");
    	}catch(Exception e){
    		entity.setResult("fail");
    		entity.setMessage(e.getMessage());
    	}
    	logService.addRunningLog(entity);
    }

}