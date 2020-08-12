package Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Common.ExcelMgr;
import iMC.Alarm;
import iMC.ImcMgr;



public class ResourceConfig 
{
	public static String APP_NAME="appName";
	private static JSONArray config;
	
	static
	{
		String file="ResourceConfig.xls";
		ExcelMgr em=new ExcelMgr();
//		String sheetname[]={"互联网核心","企业服务总线","三方支付","资金存管系统","直销银行系统","小贷系统","综合后援中心","基础技术平台","基础设施","代理服务","应用监控系统","通用数据传输平台"};
		String sheetname[]={"互联网核心","企业服务总线","三方支付","三方支付-借贷宝","标准资金存管","资金存管-借贷宝","直销银行","线上贷款平台","开放服务平台","邮件系统","统一信息平台","基础设施","代理服务","集中监控平台","日志集中管理平台","通用数据传输平台","应用日志服务","密码服务平台","数据库"};
		String field[]={"envName","envDomainSubfix","","appNodeGroupType","description","vip","domain","","","cpuCount","memerySize","diskApp","diskTotal","pvFlag","description","ipAddr","hostname","domain","userName","password","vmHostname","operDesc"};
		config=em.importFromFile(file, sheetname, field, 4);
	}
	public static String decorate(String toDecorate)
	{
		System.out.println("toDecorate:"+toDecorate);
		String app=Alarm.DEFAULT_APP;
		
		String ENV="生产环境";
		String field[]={"appName","ipAddr","hostname"};
		
		int maxcommonCount_appName=0;//记录toDecorate和appname的公共子串的最大长度
		String appNameMostSimilarByAppName=null;//记录与maxcommonCount_appName对应的最相思的应用名称
		int maxcommonCount_ipAddr=0;//记录toDecorate和ipAddr的公共子串的最大长度
		String appNameMostSimilarByIpAddr=null;//记录与maxcommonCount_ipAddr对应的最相思的应用名称
		int maxcommonCount_hostname=0;//记录toDecorate和hostname的公共子串的最大长度
		String appNameMostSimilarByHostname=null;//记录与maxcommonCount_hostname对应的最相思的应用名称
		for(int i=0;i<config.length();i++)
		{
			try {
				JSONObject js=config.getJSONObject(i);
				String env=js.getString("envName");
				String appName=js.getString(APP_NAME);
				String ipAddr=js.getString("ipAddr");
				String hostname=js.getString("hostname");
				
				if(ENV.equals(env))
				{//只关注生产环境
					int commonCount_appName=getLongestCommonSubstring(appName,toDecorate);
					int commonCount_ipAddr=getLongestCommonSubstring(ipAddr,toDecorate);
					int commonCount_hostname=getLongestCommonSubstring(hostname,toDecorate);
//					if("线上贷款平台".equals(appName))
//					{
//						System.out.println("appName:"+appName+" commonCount_appName:"+commonCount_appName);
//					}
					if(commonCount_appName>=4 && commonCount_appName>maxcommonCount_appName)
					{//按照appname进行遍历寻找最相似的，“直销银行”至少有4个字符相同
						appNameMostSimilarByAppName=appName;
						maxcommonCount_appName=commonCount_appName;
						System.out.println("appName:"+appName+" maxcommonCount_appName:"+maxcommonCount_appName);
					}
					
					if(commonCount_hostname>=7 && commonCount_hostname>maxcommonCount_hostname)
					{//按照ipAddr进行遍历寻找最相似的，dbsrd01至少有7个字符相同
						appNameMostSimilarByHostname=appName;
						maxcommonCount_hostname=commonCount_hostname;
						System.out.println("appName:"+appName+" maxcommonCount_hostname:"+maxcommonCount_hostname);
					}
					if(commonCount_ipAddr>=10 && commonCount_ipAddr>maxcommonCount_ipAddr)
					{//按照ipAddr进行遍历寻找最相似的，“10.129.0.1”至少有10个字符相同
						appNameMostSimilarByIpAddr=appName;
						maxcommonCount_ipAddr=commonCount_ipAddr;
						System.out.println("appName:"+appName+" maxcommonCount_ipAddr:"+maxcommonCount_ipAddr);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		System.out.println("appNameMostSimilarByAppName:"+appNameMostSimilarByAppName);
		System.out.println("appNameMostSimilarByHostname:"+appNameMostSimilarByHostname);
		System.out.println("appNameMostSimilarByIpAddr:"+appNameMostSimilarByIpAddr);
		if(appNameMostSimilarByAppName !=null)
		{
			return appNameMostSimilarByAppName;
		}
		if(appNameMostSimilarByHostname !=null)
		{
			return appNameMostSimilarByHostname;
		}
		if(appNameMostSimilarByIpAddr !=null)
		{
			return appNameMostSimilarByIpAddr;
		}
		return app;
	}
	
	
	public static int getLongestCommonSubstring(String a, String b){
		int m = a.length();
		int n = b.length();
	 
		int max = 0;
	 
		int[][] dp = new int[m][n];
	 
		for(int i=0; i<m; i++){
			for(int j=0; j<n; j++){
				if(a.charAt(i) == b.charAt(j)){
					if(i==0 || j==0){
						dp[i][j]=1;
					}else{
						dp[i][j] = dp[i-1][j-1]+1;
					}
	 
					if(max < dp[i][j])
						max = dp[i][j];
				}
	 
			}
		}
	 
		return max;
	}
	 public static void main(String[] args)  {
	    	
//	       String test="icds_bpm2 [PICDS2APP03] bpm2_count";
	       String test="linux_disk_io [PLOGSCHI101-sdb] iops";
	       
	       ResourceConfig.decorate(test);
	
	    }
}
