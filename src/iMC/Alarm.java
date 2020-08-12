package iMC;

import Config.ResourceConfig;
import org.json.JSONException;
import org.json.JSONObject;

public class Alarm 
{
	public static  String DEFAULT_APP="其他";
	private String id;
	private String deviceName;
	private String deviceIp;
	private String alarmCategoryDesc;
	private String alarmLevelDesc;
	private String alarmDesc;
	private String paras;
	private String faultTimeDesc;
	private long faultTime;
	private String app;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getAlarmCategoryDesc() {
		return alarmCategoryDesc;
	}
	public void setAlarmCategoryDesc(String alarmCategoryDesc) {
		this.alarmCategoryDesc = alarmCategoryDesc;
	}
	public String getAlarmLevelDesc() {
		return alarmLevelDesc;
	}
	public void setAlarmLevelDesc(String alarmLevelDesc) {
		this.alarmLevelDesc = alarmLevelDesc;
	}
	public String getAlarmDesc() {
		return alarmDesc;
	}
	public void setAlarmDesc(String alarmDesc) {
		this.alarmDesc = alarmDesc;
		setApp(ResourceConfig.decorate(alarmDesc));
	}
	public String getFaultTimeDesc() {
		return faultTimeDesc;
	}
	public void setFaultTimeDesc(String faultTimeDesc) {
		this.faultTimeDesc = faultTimeDesc;
	}
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	
	
	public String toString()
	{
		String result="";
		result=result+"id:"+id+"\t";
		result=result+"app:"+app+"\t";
		result=result+"deviceIp:"+deviceIp+"\t";
		result=result+"deviceName:"+deviceName+"\t";
		result=result+"alarmCategoryDesc:"+alarmCategoryDesc+"\t";
		result=result+"alarmLevelDesc:"+alarmLevelDesc+"\t";
		result=result+"alarmDesc:"+alarmDesc+"\t";
		result=result+"paras:"+paras+"\t";
		result=result+"faultTimeDesc:"+faultTimeDesc+"\n";
		
		return result;
	}
	public String toString2()
	{
		String result="";
		result=result+faultTimeDesc+"\t";
		result=result+id+"\t";
		result=result+app+"\t";
		result=result+deviceIp+"\t";
		result=result+deviceName+"\t";
		result=result+alarmCategoryDesc+"\t";
		result=result+alarmLevelDesc+"\t";
		result=result+alarmDesc+"\t";
		result=result+paras+"\n";
		
		
		return result;
	}
	public String getParas() {
		return paras;
	}
	public void setParas(String paras) {
		this.paras = paras;
	}
	public long getFaultTime() {
		return faultTime;
	}
	public void setFaultTime(long faultTime) {
		this.faultTime = faultTime;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		System.out.println("app:"+app);
		if(this.app ==null || DEFAULT_APP.equals(this.app))
		{
			this.app = app;
		}
		
	}
	
	public JSONObject toJsonObject() {
		JSONObject jsonObj=new JSONObject();
		try {
			jsonObj.put("id",id);
			jsonObj.put("deviceName",deviceName);
			jsonObj.put("deviceIp",deviceIp);
			jsonObj.put("alarmCategoryDesc",alarmCategoryDesc);
			jsonObj.put("alarmLevelDesc",alarmLevelDesc);
			jsonObj.put("alarmDesc",alarmDesc);
			jsonObj.put("paras",paras);
			jsonObj.put("faultTimeDesc",faultTimeDesc);
			jsonObj.put("faultTime",faultTime);
			jsonObj.put("app",app);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
}
