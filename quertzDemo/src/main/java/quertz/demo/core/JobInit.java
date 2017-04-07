package quertz.demo.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quertz.demo.entity.quartz.JobEntity;
import quertz.demo.service.JobService;

/**
 * 初始化 工具
 * @author John
 *
 */
@Service
public class JobInit {
	@Autowired
	QuartzManager quartzManager;
	@Autowired
	JobService jobService;
	
	/**
	 * 服务启动 加载 预设 跑批任务
	 * 任务JOb数  由 JobInitConfig 设定
	 * 任务Job详情 若 db存在数据 取db数据。
	 */
	@PostConstruct  
    public void  init(){  
		System.out.println("##服务开始 - 修改状态为 启动");
		List<JobEntity> list = JobInitConfig.getList();
		List<JobEntity> dblist = jobService.findAll();
		Map<String,JobEntity> jobMap = new HashMap<String,JobEntity>();
		if(dblist!=null){
			for(JobEntity entity:dblist){
				jobMap.put(entity.getJobName(),entity);
			}
		}
		for(JobEntity entity:list){
			String jobName = entity.getJobName();
			if(jobMap.containsKey(jobName)){
				quartzManager.addJob(jobMap.get(jobName));
			}else{
				quartzManager.addJob(entity);
				jobService.addJob(entity);
			}
			
		}
		jobService.updateAllStartOrStop("启动");
		System.out.println("##服务开始 - 启动 任务 成功");
    }
	
	@PreDestroy  
    public void  dostory(){ 
		System.out.println("服务停止 - 修改状态为 停止");
		jobService.updateAllStartOrStop("停止");
    }
	 
	
}
