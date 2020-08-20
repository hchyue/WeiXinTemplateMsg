package MsgSender;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

public class AccessToken
{
	private final static String URL_TOKEN="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&";
	private String accessToken;
	private long lastUpdateTime=-1;
	private static AccessToken atObj;

	private Config config;
	public static AccessToken getInstance()
	{
		if(atObj==null)
		{
			atObj=new AccessToken();
		}
		return atObj;
	}


	private void update()
	{
		try
		{
			System.out.println("update access_token start!");
			String url=URL_TOKEN+"appid="+config.getAppid()+"&secret="+config.getSecret();

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
		      .url(url)
		      .build();

		  	Response response = client.newCall(request).execute();
			JSONObject json=new JSONObject(  response.body().string());
			accessToken=json.getString("access_token");
			lastUpdateTime=System.currentTimeMillis();
		  System.out.println("update access_token finished!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getAccessToken() {
		long cur=System.currentTimeMillis();
		System.out.println("getAccessToken lastUpdateTime:"+lastUpdateTime);
		System.out.println("getAccessToken currentTime   :"+cur);
		if(lastUpdateTime==-1 || cur-lastUpdateTime>config.getAccessTokenRefreshInterval())
		{
			update();

		}

		return accessToken;
	}


	public void setConfig(Config config) {
		this.config = config;
	}
}
