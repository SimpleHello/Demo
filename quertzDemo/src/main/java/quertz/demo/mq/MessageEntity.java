package quertz.demo.mq;

public class MessageEntity {
	
	private String id;
	private String jobName;
	private String jobDescript;
	private String type;//修改类型。 00=新增 ;01=启动 10=停止 11=删除 状态的修改。 30 = 时间表达式的修改
	private String cron;
	private String mes;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getMes() {
		return mes;
	}
	public void setMes(String mes) {
		this.mes = mes;
	}
	public String getJobDescript() {
		return jobDescript;
	}
	public void setJobDescript(String jobDescript) {
		this.jobDescript = jobDescript;
	}
	
	
}
