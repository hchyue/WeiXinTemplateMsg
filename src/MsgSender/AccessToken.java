package MsgSender;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccessToken 
{
	public static AccessToken atObj;
	public final static String APPID="appid";
	public final static String SECRET="secret";
	public final static String TOKEN="access_token";
	public final static String TEMPLATE_ID="template_id";
	

	
	
	private final static String URL_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&";
	//	private final static String URL_TOKEN="https://101.226.90.58/cgi-bin/token?grant_type=client_credential&";
	private String accessToken;
	private long lastUpdateTime;
	private String token;
	private String secret;
	private String appid;
	private boolean firstTime;
	public static AccessToken getInstance(String tokenstr)
	{
		if(atObj==null)
		{
			atObj=new AccessToken(tokenstr);
		}
		return atObj;
	}
	private AccessToken(String tokenstr)
	{
		token=tokenstr;
		init();
	}
	private void init() 
	{
		firstTime=true;
		//update() ;
		lastUpdateTime=Long.parseLong(ConfigFile.getProperty("lastUpdateTime"));
		accessToken=ConfigFile.getProperty(token);
		
	
		setSecret(ConfigFile.getProperty(SECRET));
		appid=ConfigFile.getProperty(APPID);
		
		
//		if(TOKEN_ZHONGJIANGGUOJI.equals(token))
//		{
//			setSecret(ConfigFile.getProperty(SECRET_ZHONGJIANGGUOJI));
//			appid=ConfigFile.getProperty(APPID_ZHONGJIANGGUOJI);
//		}
	}
	public void update() 
	{
		try
		{
			System.out.println("update access_token start!");
			String url=URL_TOKEN+"appid="+appid+"&secret="+secret;
			lastUpdateTime=System.currentTimeMillis();
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
		      .url(url)
		      .build();

		  Response response = client.newCall(request).execute();
		  JSONObject json=new JSONObject(  response.body().string());
		  setAccessToken(json.getString("access_token"));
		  updateConfigFile();
		  
		  System.out.println("update access_token finished!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	private void updateConfigFile() 
	{
		ConfigFile.update(token,accessToken);
		ConfigFile.update("lastUpdateTime",lastUpdateTime+"");
		
	}
	public String getAccessToken() {
		long cur=System.currentTimeMillis();
		System.out.println("getAccessToken lastUpdateTime:"+lastUpdateTime);
		System.out.println("getAccessToken currentTime   :"+cur);
		if(firstTime)
		{
			update() ;
			firstTime=false;
			return accessToken;
		}
		if(cur-lastUpdateTime>3600000)
		{
			update() ;
		}
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	
}
