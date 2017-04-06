package quertz.demo.service;

import quertz.demo.entity.log.JobRunningLogEntity;
import quertz.demo.entity.log.JobUpdateLogEntity;

/**
 * job 日志保存
 * @author John
 *
 */
public interface JobLogService {
		/**
		 * job 修改 日志保存
		 * @param entity
		 * @param nameSpace
		 */
		void addUpdateLog(JobUpdateLogEntity entity);
		
		/**
		 * job 运行结果 日志保存
		 * @param entity
		 * @param nameSpace
		 */
		void addRunningLog(JobRunningLogEntity entity);
		
}
