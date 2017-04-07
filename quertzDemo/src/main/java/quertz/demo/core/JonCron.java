package quertz.demo.core;

/**
 * 一些 常用的 定时任务 粒度
 * @author John
 */

public class JonCron {
	public static final String everyMinute1 = "0 * * * * ?";//每分钟执行一次
	public static final String everyMinute5 = "0 0/5 * * * ?";//每隔5分钟执行一次
	public static final String everyMinute15 ="0 0/15 * * * ?";//每隔15分钟执行一次
	public static final String everyMinute30 ="0 0/30 * * * ?";//每隔30分钟执行一次		
	public static final String everyHour1 ="0 0 * * * ?";//每隔1小时执行一次
	public static final String everyHour2 ="0 0 */2 * * ?";//每隔2小时执行一次
	public static final String everyDay1 ="0 0 1 * * ?";//每天早上1点执行一次
	public static final String everyDay1_30 ="0 30 1 * * ?";//每天早上1点30分执行一次
	public static final String everyDay2 ="0 0 2 * * ?";//每天早上2点执行一次
	public static final String everyMonFirst ="0 0 1 1 * ?";//每月1号1点执行一次
	public static final String everyMonLast ="0 0 23 L * ?";//每月最后一天 23点执行一次
}
