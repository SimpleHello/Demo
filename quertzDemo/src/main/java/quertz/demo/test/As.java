package quertz.demo.test;

import org.quartz.CronExpression;

public class As {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String cron = "0 0/8 1 * * ?";
		boolean cronOk = CronExpression.isValidExpression(cron);// 验证时间格式 是否完成
		if (!cronOk) {
			System.out.println(cron + ":此时间格式不符合要求");

		}else{
			System.out.println("可以");
		}
	}
}
