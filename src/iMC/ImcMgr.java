package iMC;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import Common.Common;
import MsgSender.AccessToken;
import MsgSender.ConfigFile;
import MsgSender.TemplateMsgSender;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import probe.TaskICMP;
import yuehc.FileUtil;
 
public class ImcMgr extends TimerTask
{
	private static String HOST="10.13.0.2";
	private static int PORT=8080;
	
	private static String USERNAME="admin";
	private static String PASSWD="Password123!";
	
	private ArrayList<Alarm> alertVec;
	private TemplateMsgSender sender;
	private int interval;//取当前之前interval的告警,单位（分钟）
	private int maxAlertId;
	private long maxFaultTime;
	/**设置告警级别，紧急、重要。。。 */
	private String alertLevel;
	/**iMC_filter_include_ip */
	private Vector<String> includeIpVec;
	/** iMC_filter_exclude_ip*/
	private Vector<String> excludeIpVec;
	/**iMC_filter_include_desc */
	private Vector<String> includeDescVec;
	/** iMC_filter_exclude_desc*/
	private Vector<String> excludeDescVec;
	
	private HashMap<String,Long> alertMap;

	/**白名单分组 */
	JSONArray groups;
	public ImcMgr(String senderToken)
	{
		HOST=ConfigFile.getProperty("iMC_host");
		PORT=Integer.parseInt(ConfigFile.getProperty("iMC_port"));
		USERNAME=ConfigFile.getProperty("username");
		PASSWD=ConfigFile.getProperty("password");
		alertVec=new ArrayList<Alarm>();
		sender=new TemplateMsgSender(senderToken);
		interval=1;
		alertLevel=ConfigFile.getProperty("iMC_filter_alert_level");
		includeIpVec=new Vector<String> ();
		maxAlertId=0;
		maxFaultTime=System.currentTimeMillis() / 1000 - 60*(interval+1);
		ConfigFile.getVectorProperty("iMC_filter_include_ip",includeIpVec);
		excludeIpVec=new Vector<String> ();
		ConfigFile.getVectorProperty("iMC_filter_exclude_ip",excludeIpVec);

		FileUtil fu=new FileUtil();

		try {
			 groups=new JSONArray(fu.readContent("whiteList.json"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		includeDescVec=new Vector<String> ();
		ConfigFile.getVectorProperty("iMC_filter_include_desc",includeDescVec);
		excludeDescVec=new Vector<String> ();
		ConfigFile.getVectorProperty("iMC_filter_exclude_desc",excludeDescVec);
		alertMap=new HashMap<String,Long>();
	}
	
	public ImcMgr(String senderToken,int time) 
	{
		this(senderToken);
		interval=time;
	}
    public static void main(String[] args) throws Exception {
    	
       
        ImcMgr im=new ImcMgr("a");
//        im.test();
        im.run();
    }
	@Override
	public void run() 
	{

		getAlertsTest();
//		getAlerts();
		recordAlerts();
		sendTemplateMsg();
			
		
	}
	/**
	 * 将告警按每天一个文件的方式进行存储
	 */
	private void recordAlerts() {



		for(int i=0;i<groups.length();i++)
		{
			String DIR="record";
			try {
				JSONObject jsonObject=groups.getJSONObject(i);
				JSONArray alertArray=jsonObject.getJSONArray("alerts");
				String groupName=jsonObject.getString("groupName");
				String groupEname="";
				if(groupName!=null)
				{
					groupName=groupName.trim();

				}
				if(groupName==null || groupName.equals("所有告警"))
				{
					groupEname="allAlerts";
				}
				else if(groupName.equals("挂账"))
				{
					groupEname="guazhang";
				}

				String date=Common.getCurrentTime4();
				DIR=DIR+System.getProperty("file.separator")+groupEname;
				File dir=new File(DIR);
				if(!dir.exists())
				{
					dir.mkdirs();
				}

				String recordFilename=DIR+System.getProperty("file.separator")+date;

				FileUtil fu=new FileUtil();

				for(int j=0;j<alertArray.length();j++)
				{
					JSONObject al= alertArray.getJSONObject(j);
					fu.appString(recordFilename, al.toString());
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}


		
	}


	private void sendTemplateMsg() 
	{
		for(int i=0;i<groups.length();i++)
		{
			try {
				JSONObject jsonObject=groups.getJSONObject(i);
				String groupName=jsonObject.getString("groupName");
				JSONArray alertArray=jsonObject.getJSONArray("alerts");
				JSONArray receivers=jsonObject.getJSONArray("receivers");
				for(int j=0;j<alertArray.length();j++)
				{
					JSONObject al= alertArray.getJSONObject(j);
					sender.send(groupName,receivers,al);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		
	}
	
	/**
	 * 每隔1分钟取前两分钟的事件，根据“告警ID”来确定是否是重复事件，如果是重复事件将不作为新事件
	 * 
	 */
	private void getAlerts() {

		clearGroupAlert();
		try {

			DefaultHttpClient client = new DefaultHttpClient();
			client.getCredentialsProvider().setCredentials(new AuthScope(HOST, PORT, "iMC RESTful Web Services"),
					new UsernamePasswordCredentials(USERNAME, PASSWD));
			long curtime = System.currentTimeMillis() / 1000 - 60*(interval+1);

			//获取未恢复事件

			HttpGet get = new HttpGet("http://"+HOST+":"+PORT+"/imcrs/fault/alarm?operatorName=admin&desc=false&startAlarmTime=" + curtime);
			get.addHeader("accept", "application/xml");
			HttpResponse response = client.execute(get);
			System.out.println(response.getStatusLine());
			Document xml;
			String responseText=EntityUtils.toString(response.getEntity(), "UTF-8");
//			String responseText="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><list><alarm><id>33753</id><OID>1.3.6.1.4.1.25506.4.2.8.2.6.20</OID><originalType>2</originalType><originalTypeDesc>Syslog</originalTypeDesc><deviceId>913</deviceId><deviceIp>172.16.255.31</deviceIp><deviceName>172.16.255.31</deviceName><alarmLevel>4</alarmLevel><alarmLevelDesc>警告</alarmLevelDesc><alarmCategory>1101</alarmCategory><alarmCategoryDesc>Syslog告警</alarmCategoryDesc><faultTime>1470730567</faultTime><faultTimeDesc>2016-08-09 16:16:07</faultTimeDesc><recTime>0</recTime><recTimeDesc></recTimeDesc><recStatus>0</recStatus><recStatusDesc>未恢复</recStatusDesc><recUserName></recUserName><ackTime>0</ackTime><ackTimeDesc></ackTimeDesc><ackStatus>0</ackStatus><ackStatusDesc>未确认</ackStatusDesc><ackUserName></ackUserName><alarmDesc>AttackName:Generic_Password_Blasting_Attempt(HTTP_POST);PolicyName:ips-any-any; 172.16.70.32:53943-&gt;172.16.96.223:9999;protocol:TCP; action:Permit &amp; Logging &amp; Capture。</alarmDesc><paras>*Rule Name=IPS-H3C;Description=AttackName:Generic_Password_Blasting_Attempt(HTTP_POST);PolicyName:ips-any-any; 172.16.70.32:53943-&gt;172.16.96.223:9999;protocol:TCP; action:Permit &amp; Logging &amp; Capture;Syslog Facility=16;Syslog Severity=4;Parameter List=(digest)=(IPS_IPV4_INTERZONE)&amp;(protocol)=(TCP)&amp;(srcAddr)=(172.16.70.32)&amp;(srcPort)=(53943)&amp;(dstAddr)=(172.16.96.223)&amp;(dstPort)=(9999)&amp;(srcZone)=(office)&amp;(dstZone)=(Server)&amp;(pName)=(ips-any-any)&amp;(atkName)=(Generic_Password_Blasting_Attempt(HTTP_POST))&amp;(action)=(Permit &amp; Logging &amp; Capture);Time=2016-08-09 16:16:06;Reason=the count of action(Permit &amp; Logging &amp; Capture) has reached the threshold:1;Source IP=172.16.255.31;*Pos Info=N/A;Trap Level=4;*Device IP=172.16.255.31;*Device Name=172.16.255.31;*Interface Index=0</paras><parentId>0</parentId><somState>0</somState><remark></remark><holdInfo></holdInfo><alarmDetail>http://10.13.0.2:8080/imcrs/fault/alarm/33753</alarmDetail></alarm></list>";
//			String responseText="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><list><alarm><id>83639</id><OID>1.3.6.1.4.1.3375.2.4.6.11</OID><originalType>1</originalType><originalTypeDesc>Trap</originalTypeDesc><deviceId>2340</deviceId><deviceIp>10.142.20.2</deviceIp><deviceName>PLTM0102-I1</deviceName><alarmLevel>5</alarmLevel><alarmLevelDesc>通知</alarmLevelDesc><alarmCategory>1</alarmCategory><alarmCategoryDesc>其它告警</alarmCategoryDesc><faultTime>1539071478</faultTime><faultTimeDesc>2018-10-09 15:51:18</faultTimeDesc><recTime>0</recTime><recTimeDesc></recTimeDesc><recStatus>0</recStatus><recStatusDesc>未恢复</recStatusDesc><recUserName></recUserName><ackTime>0</ackTime><ackTimeDesc></ackTimeDesc><ackStatus>0</ackStatus><ackStatusDesc>未确认</ackStatusDesc><ackUserName></ackUserName><alarmDesc>A service is detected UP.</alarmDesc><paras>bigipNotifyObjMsg=Pool /Common/pool_olp_webp_80 member /Common/10.133.3.194%1:80 monitor status up. [ /Common/tcp: up ]  [ was down for 0hr:0min:19sec ];bigipNotifyObjNode=/Common/10.133.3.194%1;bigipNotifyObjPort=80</paras><parentId>0</parentId><somState>0</somState><remark></remark><holdInfo></holdInfo><repeats>139</repeats><resSourceType>0</resSourceType><alarmDetail>http://syslog:8080/imcrs/fault/alarm/83639</alarmDetail></alarm></list>";
			System.out.println("response:"+responseText);
			Pattern p = Pattern.compile("[^\\u0009\\u000A\\u000D\u0020-\\uD7FF\\uE000-\\uFFFD\\u10000-\\u10FFF]+");
			responseText = p.matcher(responseText).replaceAll("");
			xml = DocumentHelper.parseText(responseText);
			Element root = xml.getRootElement();
			Element e = null;


			for (Iterator i = root.elementIterator(); i.hasNext();) {
				e = (Element) i.next();
				String name = e.getName();

				System.out.println(name);

				if ("alarm".equals(name)) {
					Alarm al = new Alarm();
					for (Iterator j = e.elementIterator(); j.hasNext();)
					{
						Element enm = (Element) j.next();
						name = enm.getName();
						if ("id".equals(name)) {
							al.setId(enm.getTextTrim());

						}
						if ("deviceIp".equals(name)) {
							al.setDeviceIp(enm.getTextTrim());
						}
						if ("deviceName".equals(name)) {
							al.setDeviceName(enm.getTextTrim());
						}
						if ("alarmLevelDesc".equals(name)) {
							al.setAlarmLevelDesc(enm.getTextTrim());
						}
						if ("alarmCategoryDesc".equals(name)) {
							al.setAlarmCategoryDesc(enm.getTextTrim());
						}
						if ("faultTime".equals(name)) {
							al.setFaultTime(Long.parseLong(enm.getTextTrim()));
						}
						if ("faultTimeDesc".equals(name)) {
							al.setFaultTimeDesc(enm.getTextTrim());
						}
						if ("alarmDesc".equals(name)) {
							al.setAlarmDesc(enm.getTextTrim());
						}
						if ("paras".equals(name)) {
							al.setParas(enm.getTextTrim());
						}

					}


					if(isNewAlert(al))
					{
//						System.out.println("alertVec.size():"+alertVec.size());

						System.out.println("alertVec.size():"+alertVec.size());
						complementAlert(al);
						addAlertByFilter(groups,al);
//						maxAlertId=curId;
					}



				}

			}

		} catch (DocumentException e1) {
			e1.printStackTrace();

		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private void getAlertsTest() {
		clearGroupAlert();
		Alarm al=new Alarm ();
		al.setDeviceIp("10.16.255.31");
		al.setAlarmCategoryDesc("直销");
		al.setAlarmDesc("this is a test账户余额不为0");
		al.setAlarmLevelDesc("警告");
		al.setDeviceName("adad");
		al.setAlarmLevelDesc("20171217");
		al.setFaultTimeDesc("20171217");
		al.setFaultTime(1577433252);
		if(isNewAlert(al))
		{
//						System.out.println("alertVec.size():"+alertVec.size());

			System.out.println("alertVec.size():"+alertVec.size());
			complementAlert(al);
			addAlertByFilter(groups,al);
//						maxAlertId=curId;
		}

	}

	/**
	 * 在每个周期开始前,清除groups中的alerts
	 */
	private void clearGroupAlert() {
		for(int i=0;i<groups.length();i++)
		{
			try {
				JSONObject jsonObject=groups.getJSONObject(i);
				if(!jsonObject.has("alerts"))
				{
					JSONArray alertArray=new JSONArray();
					jsonObject.put("alerts",alertArray);
				}
				else
				{
					JSONArray alertArray=jsonObject.getJSONArray("alerts");
					for(int j=alertArray.length()-1;j>=0;j--)
					{
						alertArray.remove(j);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 判断是否是新事件
	 * 新事件的条件：
	 * 	1、如果是新id，则为新事件
	 * 	2、如果id是已存在的，FaultTime更新了，也认为是新事件
	 * @param al
	 * @return
	 */
	private boolean isNewAlert(Alarm al) 
	{
		if(!alertMap.isEmpty() && alertMap.containsKey(al.getId()))
		{
			long lastFaultTime=alertMap.get(al.getId()).longValue();
			if(al.getFaultTime()>lastFaultTime)
			{
				alertMap.put(al.getId(), al.getFaultTime());
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			alertMap.put(al.getId(), al.getFaultTime());
			return true;
		}
		
	}

	/**
	 * 根据alert的字段丰富alarmDesc字段
	 * @param al
	 */
	private void complementAlert(Alarm al) {
		String BIGIP_SERVICE_DOWN="A service is detected DOWN.";
		String BIGIP_SERVICE_UP="A service is detected UP.";
		
		String BIGIP_VIRTUAL_DOWN="A virtual has become unavailable.";
		String BIGIP_VIRTUAL_UP="A virtual has become available.";
		
		if(BIGIP_SERVICE_DOWN.equals(al.getAlarmDesc()) 
				|| BIGIP_SERVICE_UP.equals(al.getAlarmDesc())
				|| BIGIP_VIRTUAL_DOWN.equals(al.getAlarmDesc())
				|| BIGIP_VIRTUAL_UP.equals(al.getAlarmDesc()))
		{
			al.setAlarmDesc(al.getParas());
		}
		
	}

	/**
	 * 根据各个字段的include、exclude来过滤事件,过滤出每个group此周期的告警
	 * 另外按照FaultTime升序来排列事件
	 * includeIpVec和excludeIpVec是公共属性,是每个分组都必须满足的条件
	 *
	 * @param array
	 * @param al
	 */
	private void addAlertByFilter(JSONArray array, Alarm al)
	{
		String ip=al.getDeviceIp();
		String desc=al.getAlarmDesc();
		if(Common.isIpInList(ip,includeIpVec)&& Common.isIpNotInList(ip,excludeIpVec))
		{

			for(int i=0;i<array.length();i++)
			{
				try {
					JSONObject jsonObject=array.getJSONObject(i);
					JSONArray include_desc_array=jsonObject.getJSONArray("include_desc");
					JSONArray exclude_desc_array=jsonObject.getJSONArray("exclude_desc");
					JSONArray alertArray=jsonObject.getJSONArray("alerts");

					if (Common.isDescInList(desc,include_desc_array)&& Common.isDescNotInList(desc,exclude_desc_array))
					{
						if(alertArray.length()==0)
						{
							alertArray.put(al.toJsonObject());
							continue;
						}

						for(int j=alertArray.length()-1;j>=0;j--)
						{
							JSONObject indexAl=alertArray.getJSONObject(j);
							long faultTime=indexAl.getLong("faultTime");
							if(faultTime>al.getFaultTime())
							{
//					System.out.println("alertVec.size:"+alertVec2.size());
								alertArray.put(j+1,indexAl);
								if(j==0)
								{
									alertArray.put(j,al.toJsonObject());
								}
							}
							else
							{
								alertArray.put(j+1,al.toJsonObject());
								break;
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		else
		{
			System.out.println("ip:"+ip+",not in filter. so add no alert!");
		}


	}

	

	public String getAlertLevel() {
		return alertLevel;
	}
	public void setAlertLevel(String alertLevel) {
		this.alertLevel = alertLevel;
	}

	public int getMaxAlertId() {
		return maxAlertId;
	}

	public void setMaxAlertId(int maxAlertId) {
		this.maxAlertId = maxAlertId;
	}

}