package MsgSender;

import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class TemplateMsgSender
{
	public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

	private final static String URL_TEMPLATE="https://api.weixin.qq.com/cgi-bin/message/template/send";



	private AccessToken accessToken;
	private OkHttpClient client ;

	private UserList userlist;
	/**配置 */
	private Config config;


	public TemplateMsgSender(Config config)
	{
		this.config=config;
		this.accessToken=AccessToken.getInstance();
		this.accessToken.setConfig(this.config);

		this.userlist=UserList.getInstance();
		this.userlist.setConfig(this.config);
		this.userlist.setToken(this.accessToken);

		this.client = new OkHttpClient();


	}
	
	public Vector<String> parse(String inputStr,String regexStr) {

		Vector<String> result=new Vector<String>();
		Pattern p = Pattern.compile(regexStr);
		Matcher m = p.matcher(inputStr);
		while(m.find())
		{
			result.add(m.group(1));
		}

		return result;

	}

	private String buildPostData(String openid, String al) {
		JSONObject json = new JSONObject();
		try {

			String regex1 = "\\{\\{(\\S+).DATA\\}\\}";
			Vector<String> templateVec=parse(config.getTemplate(),regex1);

			String regex2 = "\\{\\{(.*)\\}\\}";
			Vector<String> alarmVec=parse(al,regex2);

			if(templateVec.size()==alarmVec.size())
			{
				json.put("touser", openid);
				json.put("template_id", config.getTemplateId());
				JSONObject datajson = new JSONObject();
				json.put("data", datajson);
				for(int i=0;i<templateVec.size();i++)
				{
					JSONObject item = new JSONObject();
					item.put("value",alarmVec.get(i));
					datajson.put(templateVec.get(i), item);
				}


			}
			else
			{
				System.out.println( "templateVec.size()!=alarmVec.size()");
			}




		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public void send(String alarm) {
		String url=URL_TEMPLATE+"?access_token="+accessToken.getAccessToken();

		for(String openid:userlist.getUserlist())
		{
			System.out.println( "userOpenId:"+openid);

			try{

				System.out.println("send a alarm:"+alarm);
				String json=buildPostData(openid,alarm);
				RequestBody body = RequestBody.create(JSON, json);
				Request request = new Request.Builder()
						.url(url)
						.post(body)
						.build();
				Response response = client.newCall(request).execute();
				System.out.println( response.body().string());


			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
