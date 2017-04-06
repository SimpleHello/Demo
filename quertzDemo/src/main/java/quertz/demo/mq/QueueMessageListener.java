package quertz.demo.mq;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import quertz.demo.core.QuartzManager;
import quertz.demo.entity.log.JobUpdateLogEntity;
import quertz.demo.entity.quartz.JobEntity;
import quertz.demo.service.JobLogService;
import quertz.demo.service.JobService;

@Service
public class QueueMessageListener implements MessageListener {

	@Autowired
	QuartzManager quartzManager;
    
	@Autowired
	JobLogService jobLogService;
	
	@Autowired
	JobService jobService;
    //当收到消息后，自动调用该方法
    @Override
    public void onMessage(Message message) {
        // 00=新增 ;01=启动 10=停止 11=删除 状态的修改。 30 = 时间表达式的修改
        TextMessage tm = (TextMessage) message;
        try {
        	MessageEntity entity = JSON.parseObject(tm.getText(),MessageEntity.class);
        	String jobName = entity.getJobName();
        	String type = entity.getType();
        	String cron = entity.getCron();
            System.out.println("QueueMessageListener 监听到了文本消息 id：" + entity.getId());
            System.out.println("QueueMessageListener 监听到了文本消息 jobName：" + jobName);
            System.out.println("QueueMessageListener 监听到了文本消息 Type：" + type);
            System.out.println("QueueMessageListener 监听到了文本消息 Cron：" + cron);
            boolean cronOk = CronExpression.isValidExpression(cron);//验证时间格式 是否完成
            if(!cronOk){
            	System.out.println(cron+":此时间格式不符合要求");
            }
            JobUpdateLogEntity logEntity = new JobUpdateLogEntity(type);
            JobEntity detail = new JobEntity(jobName,"测试","0/3 * * * * ?");
            JobEntity dbEntity = jobService.find(detail);
            if(dbEntity!=null){
            	logEntity.setJobId(detail.getId());
            	logEntity.setBeforeJob(new JobEntity(dbEntity.getJobName(),dbEntity.getJobDescript(),dbEntity.getCron(),dbEntity.getStatus()));
        	}
            if(type==null){
            	System.out.println("type 不能为空");
            }else if(type.equals("00")){
            	if(dbEntity!=null){
            		System.out.println("该任务 已存在 状态："+dbEntity.getStatus());
            	}else{
            		logEntity.setAfterJob(detail);
            		quartzManager.addJob(detail);
                	jobService.addJob(detail);	
            	}	
            }else if(type.equals("30")&&cron!=null&&!"".equals(cron)){
            	if(dbEntity==null){
            		System.out.println("该任务 不存在 请先进行创建");
            	}else{
            		dbEntity.setCron("0/5 * * * * ?");
            		logEntity.setAfterJob(dbEntity);
            		quartzManager.modifyJobTime(dbEntity);
                	jobService.update(dbEntity);
            	}
            }else if(type.equals("01")){
            	boolean ifJobExist  =quartzManager.ifJobExist(detail);//判断当前是否在运行
            	if(dbEntity==null){
            		System.out.println("该任务 不存在");
            	}else if("启动".equals(dbEntity.getStatus())&&ifJobExist){
            		System.out.println(" 该任务 已在运行");
            	}else{
            		dbEntity.setStatus("启动");
            		logEntity.setAfterJob(dbEntity);
            		quartzManager.addJob(dbEntity);
                	jobService.update(dbEntity);
            	}
            }else if(type.equals("10")){
            	if(dbEntity==null){
            		System.out.println("该任务 不存在");
            	}else if("停止".equals(dbEntity.getStatus())){
            		System.out.println(" 该任务 已停止");
            	}else{
            		dbEntity.setStatus("停止");
            		logEntity.setAfterJob(dbEntity);
            		quartzManager.removeJob(dbEntity);
                	jobService.update(dbEntity);
            	}
            }else if(type.equals("11")){
            	if(dbEntity==null){
            		System.out.println("该任务 不存在");
            	}else{
            		quartzManager.removeJob(dbEntity);
                	jobService.deleteJob(dbEntity);
            	}
            }
            jobLogService.addUpdateLog(logEntity);
            //do something ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}