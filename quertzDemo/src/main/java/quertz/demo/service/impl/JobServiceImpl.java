package quertz.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import quertz.demo.dao.QuartzJobDao;
import quertz.demo.entity.quartz.JobEntity;
import quertz.demo.service.JobService;

@Service
public class JobServiceImpl implements JobService {

	@Autowired
	QuartzJobDao dao;
	
	@Override
	public void addJob(JobEntity entity) {
		// TODO Auto-generated method stub
		dao.insert(entity);
	}

	@Override
	public void update(JobEntity entity) {
		// TODO Auto-generated method stub
		dao.update(entity);
	}

	@Override
	public void deleteJob(JobEntity entity) {
		// TODO Auto-generated method stub
		dao.delete(entity);
	}

	@Override
	public JobEntity find(JobEntity entity) {
		// TODO Auto-generated method stub
		return dao.find(entity);
	}

	@Override
	public List<JobEntity> findAll() {
		// TODO Auto-generated method stub
		return dao.findAll();
	}

	@Override
	public void updateAllStartOrStop(String status) {
		// TODO Auto-generated method stub
		dao.updateStop(status);
	}


}
