package quertz.demo.core;

import java.util.ArrayList;
import java.util.List;

import quertz.demo.entity.quartz.JobEntity;

/**
 * 启动  任务配置
 * @author John
 *
 */
public class JobInitConfig {
	
	private static List<JobEntity> list = new ArrayList<JobEntity>();
	
	public static List<JobEntity> getList(){
		list.add(new JobEntity("TestQuartz2","测试2","0/3 * * * * ?"));//测试 3秒执行1次
		list.add(new JobEntity("TestQuartz3","测试3","0/4 * * * * ?"));//测试 4秒执行1次
		list.add(new JobEntity("TestQuartz4","测试4","0/5 * * * * ?"));//测试 5秒执行1次
		return list;
	}
}
