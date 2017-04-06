package quertz.demo.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import quertz.demo.entity.log.JobRunningLogEntity;
import quertz.demo.entity.log.JobUpdateLogEntity;

@Repository
public class QuartzJobLogDao {
	@Autowired
	MongoTemplate mongoTemplate;

	
	public void insertRunningLog(JobRunningLogEntity entity) {
		mongoTemplate.save(entity, JobRunningLogEntity.namespace);
	}

	public void insertUpdateLog(JobUpdateLogEntity entity) {
		mongoTemplate.save(entity, JobUpdateLogEntity.namespace);
	}

}
