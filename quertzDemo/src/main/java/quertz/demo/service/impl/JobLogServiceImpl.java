package quertz.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quertz.demo.dao.QuartzJobLogDao;
import quertz.demo.entity.log.JobRunningLogEntity;
import quertz.demo.entity.log.JobUpdateLogEntity;
import quertz.demo.service.JobLogService;

@Service
public class JobLogServiceImpl implements JobLogService {
	
	@Autowired
	QuartzJobLogDao dao;
	
	@Override
	public void addUpdateLog(JobUpdateLogEntity entity) {
		// TODO Auto-generated method stub
		dao.insertUpdateLog(entity);
	}

	@Override
	public void addRunningLog(JobRunningLogEntity entity) {
		// TODO Auto-generated method stub
		dao.insertRunningLog(entity);
	}

}
