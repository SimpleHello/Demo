package quertz.demo.mq;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	@Autowired
	ProducerService producerService;
    /**
     * 定时任务 监听消息队列== 当收到消息后，自动调用该方法
     */
    @Override
    public void onMessage(Message message) {
        TextMessage tm = (TextMessage) message;
        try {
        	Date messageTime = new Date(message.getJMSTimestamp());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	MessageEntity entity = JSON.parseObject(tm.getText(),MessageEntity.class);
        	String jobName = entity.getJobName();
        	String type = entity.getType();
        	String cron = entity.getCron();
            System.out.println("####接收到：消息时间"+sdf.format(messageTime) 
            						+" id：" + entity.getId()+" jobName：" + jobName
            						+"  Type：" + type +"  Cron：" + cron);
            JobUpdateLogEntity logEntity = new JobUpdateLogEntity(type);
            JobEntity query = new JobEntity(jobName,entity.getJobDescript(),entity.getCron());
            JobEntity dbEntity = jobService.find(query);
            if(dbEntity!=null){
            	logEntity.setJobId(dbEntity.getId());
            	logEntity.setBeforeJob(dbEntity.clone());
        	}
            if(type==null){
            	type = "";
            }
            /**
             * type(00=新增 ;01=启动 10=停止 11=删除 状态的修改。 30 = 时间表达式的修改)
             */
            switch(type){
            	case "00":addJob(dbEntity,logEntity,entity);break;//新增 JOB 。。暂时不需要此功能
            	case "01":startJob(dbEntity,logEntity,entity);break;//启动 JOB
            	case "30":updateJob(dbEntity,logEntity,entity);break;//修改 job 的时间粒度
            	case "10":stopJob(dbEntity,logEntity,entity);break;//停止 job 
            	case "11":delJob(dbEntity,logEntity,entity);break;//停止并删除 job 。。暂时不需要此功能
            	default:defaultJob(entity);
            }
            jobLogService.addUpdateLog(logEntity);
            //do something ...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 00 新增 JOB
     */
    private void addJob(JobEntity dbEntity,JobUpdateLogEntity logEntity,MessageEntity entity){	
    	String jobName = entity.getJobName();
    	String cron = entity.getCron();
    	boolean cronOk = CronExpression.isValidExpression(cron);//验证时间格式 是否完成
    	JobEntity detail = new JobEntity(jobName,entity.getJobDescript(),entity.getCron());
    	if(dbEntity!=null||!cronOk){
    		System.out.println("该任务已存在无需创建"+"或 时间粒度不符合规范:"+cronOk);
    	}else{
    		logEntity.setAfterJob(detail);
    		quartzManager.addJob(detail);
        	jobService.addJob(detail);	
    	}
    }
    /**
     * 01 启动 job
     */
    private void startJob(JobEntity dbEntity,JobUpdateLogEntity logEntity,MessageEntity entity){	
    	if(dbEntity==null){
    		entity.setMes("该任务 不存在 请先进行创建 ");
    		producerService.sendMessage(JSON.toJSONString(entity));
    	}else{
    		boolean ifJobExist  =quartzManager.ifJobExist(dbEntity);//判断当前是否在运行
    		if("启动".equals(dbEntity.getStatus())&&ifJobExist){
    			entity.setMes("该任务正在运行 ");
        		producerService.sendMessage(JSON.toJSONString(entity));
    		}else{
    			dbEntity.setStatus("启动");
        		logEntity.setAfterJob(dbEntity);
        		quartzManager.addJob(dbEntity);
            	jobService.update(dbEntity);
    		}
    	}
    }
    /**
     * 30 修改 JOB 时间粒度
     */
    private void updateJob(JobEntity dbEntity,JobUpdateLogEntity logEntity,MessageEntity entity){
    	boolean cronOk = CronExpression.isValidExpression(entity.getCron());//验证时间格式 是否完成
        if(dbEntity==null||!cronOk){
    		entity.setMes("该任务 不存在 请先进行创建"+" 或  时间粒度不符合规范:"+entity.getCron());
    		producerService.sendMessage(JSON.toJSONString(entity));
    	}else{
    		dbEntity.setCron(entity.getCron());
    		logEntity.setAfterJob(dbEntity);
    		quartzManager.modifyJobTime(dbEntity);
        	jobService.update(dbEntity);
    	}
    }
    /**
     * 01 停止  JOB
     */
    private void stopJob(JobEntity dbEntity,JobUpdateLogEntity logEntity,MessageEntity entity){
    	String mes = "";
    	if(dbEntity==null){
    		entity.setMes("该任务 不存在 请先进行创建 ");
    		producerService.sendMessage(JSON.toJSONString(entity));
    	}else{
    		boolean ifJobExist  =quartzManager.ifJobExist(dbEntity);//判断当前是否在运行
    		if(!ifJobExist){
    			entity.setMes(mes+"该任务 不在运行中 ");
        		producerService.sendMessage(JSON.toJSONString(entity));
    		}else{
    			dbEntity.setStatus("停止");
        		logEntity.setAfterJob(dbEntity);
        		quartzManager.removeJob(dbEntity);
            	jobService.update(dbEntity);
    		}
    		
    	}
    }
    /**
     * 11 停止并 删除  JOB
     */
    private void delJob(JobEntity dbEntity,JobUpdateLogEntity logEntity,MessageEntity entity){
    	String mes = "";
    	if(dbEntity==null){
    		entity.setMes("该任务 不存在 无需删除操作");
    		producerService.sendMessage(JSON.toJSONString(entity));
    	}else{
    		boolean ifJobExist  =quartzManager.ifJobExist(dbEntity);//判断当前是否在运行
    		if(!ifJobExist){
    			entity.setMes(mes+"该任务 不在运行中 删除 数据库数据");
        		producerService.sendMessage(JSON.toJSONString(entity));
        		jobService.deleteJob(dbEntity);
    		}else{
    			quartzManager.removeJob(dbEntity);
            	jobService.deleteJob(dbEntity);
    		}
    		
    	}
    }
    /**
     * default 无效操作
     */
    private void defaultJob(MessageEntity entity){
    	entity.setMes("操作类型 无效！");
		producerService.sendMessage(JSON.toJSONString(entity));
    }
}