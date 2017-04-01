package quertz.demo.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TestQuartz2 implements Job{

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println("**********测试 2 跑批类*** " +sdf.format(new Date()));
    }
}