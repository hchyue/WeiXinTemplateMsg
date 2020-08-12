package MsgSender;

import java.util.Date;
import java.util.TimerTask;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import iMC.Alarm;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TemplateMsgSender
{
	public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

	private final static String URL_TEMPLATE="https://api.weixin.qq.com/cgi-bin/message/template/send";
	//	private final static String URL_TEMPLATE="https://101.226.90.58/cgi-bin/message/template/send";
	private AccessToken accessToken;
	private OkHttpClient client ;
	/**是否启用白名单开关，默认不启用。 */
//	private boolean receiverWhiteListEnable;
	/** 接受者白名单*/
//	private Vector<String> receiverWhiteList;
	private UserList userlist;
	private String templateId;

	private static int count=0;
	private static int lastday=0;
	
	public TemplateMsgSender(String token)
	{
//		receiverWhiteListEnable=false;
//		String reveiverFlag=ConfigFile.getProperty("receiver_white_list_enable");
//		receiverWhiteListEnable="true".equals(reveiverFlag)? true:false;
//		receiverWhiteList= new Vector<String> ();
//		if(receiverWhiteListEnable)
//		{
//			ConfigFile.getVectorProperty("receiver_white_list",receiverWhiteList);
//		}
		
		
		
		accessToken=AccessToken.getInstance(token);
		client = new OkHttpClient();
		userlist=new UserList(accessToken);
		
		templateId=ConfigFile.getProperty(AccessToken.TEMPLATE_ID);
		
//		
//		if(AccessToken.TOKEN_ZHONGJIANGGUOJI.equals(token))
//		{
//			templateId=ConfigFile.getProperty(AccessToken.TEMPLATE_ID_ZHONGJIANGGUOJI);
//		}
	}
	
	public void send(String group,JSONArray receivers,JSONObject al) {


		String url=URL_TEMPLATE+"?access_token="+accessToken.getAccessToken();
		  System.out.println(Common.Common.getCurrentTime3());
		for(String openid:userlist.getUserlist())
		{
			System.out.println( "userOpenId:"+openid);

			try{

				for(int i=0;i<receivers.length();i++)
				{
					String openIdInList=receivers.getJSONObject(i).getString("openId");
					if(openid.equals(openIdInList))
					{
						System.out.println("send a alarm:"+al.toString());
						String json=buildPostData(group,openid,al);
						RequestBody body = RequestBody.create(JSON, json);
						Request request = new Request.Builder()
								.url(url)
								.post(body)
								.build();
						Response response = client.newCall(request).execute();
						System.out.println( response.body().string());
						count++;
						System.out.println("count:"+count);
						Date d=new Date();
						if(d.getDay()!=lastday)
						{
							count=0;
							lastday=d.getDay();
						}
					}
				}

			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
//		String json="{\"touser\":\"oHx88xKnZN0OY0LeT0DNjXxDc4QY\",\"template_id\":\"TzN9u1iJrRu67ouodzLgZQVZLiE_u4fSjhxfwmc9Ug0\",\"url\":\"http://weixin.qq.com/download\",\"topcolor\":\"#FF0000\",\"data\":{\"first\":{\"value\":\"请注意！\",\"color\":\"#173177\"},\"performance\":{\"value\":\"BSS系统无法正常查询免费资源\",\"color\":\"#FF0000\"},\"time\":{\"value\":\"2013-11-21下午\",\"color\":\"#173177\"},\"remark\":{\"value\":\"请后台人员相互转告，谢谢！\",\"color\":\"#173177\"}}}";
		
	}

	private String buildPostData(String group,String openid, JSONObject al) {
		JSONObject json = new JSONObject();
		try {

			if("所有告警".equals(group))
			{
				group="";
			}
			json.put("touser", openid);
			json.put("template_id", templateId);

			json.put("topcolor", "#FF0000");
			JSONObject datajson = new JSONObject();
			JSONObject datajson1 = new JSONObject();
			datajson1.put("value", group);
			datajson1.put("color", "#FF0000");
			datajson.put("first", datajson1);

//			JSONObject datajson2 = new JSONObject();
//			datajson2.put("value", al.getId());
//			datajson2.put("color", "#173177");
//			datajson.put("id", datajson2);

			JSONObject datajson7 = new JSONObject();
			datajson7.put("value", al.getString("faultTimeDesc"));
			datajson7.put("color", "#173177");
			datajson.put("keyword1", datajson7);

			JSONObject datajson4 = new JSONObject();
			datajson4.put("value", al.getString("app"));
			datajson4.put("color", "#173177");
			datajson.put("keyword2", datajson4);

			JSONObject datajson5 = new JSONObject();
			datajson5.put("value", al.getString("alarmLevelDesc"));
			datajson5.put("color", "#173177");
			datajson.put("keyword3", datajson5);

			JSONObject datajson3 = new JSONObject();
			datajson3.put("value", al.getString("deviceName"));
			datajson3.put("color", "#173177");
			datajson.put("keyword1", datajson3);


			JSONObject datajson6 = new JSONObject();
			datajson6.put("value", al.getString("alarmDesc"));
			datajson6.put("color", "#173177");
			datajson.put("alarmDesc", datajson6);



			JSONObject datajson8 = new JSONObject();
			datajson8.put("value", "");
			datajson8.put("color", "#173177");
			datajson.put("remark", datajson8);

			json.put("data", datajson);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}
	
}
