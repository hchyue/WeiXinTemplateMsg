package MsgSender;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserList 
{
	private final static String URL_USER="https://api.weixin.qq.com/cgi-bin/user/get";
	private long lastUpdateTime=-1;
	private Vector<String> userlist;
	private AccessToken token;
	private Config config;

	private static UserList ulObj;
	public static UserList getInstance()
	{
		if(ulObj==null)
		{
			ulObj=new UserList();
		}
		return ulObj;
	}
	public UserList()
	{
		userlist=new Vector<String>();

	}


	public Vector<String> getUserlist() {
		long cur=System.currentTimeMillis();
		System.out.println("getUserlist lastUpdateTime:"+lastUpdateTime);
		System.out.println("getUserlist currentTime   :"+cur);
		if(lastUpdateTime ==-1 && cur-lastUpdateTime>config.getUserListRefreshInterval())
		{
			update() ;
		}
		return userlist;
	}

	private void update() {
		System.out.println("update userlist start!");
		
		String url=URL_USER+"?access_token="+token.getAccessToken()+"&next_openid=";
		try
		{

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
		      .url(url)
		      .build();

			Response response = client.newCall(request).execute();
			JSONObject json=new JSONObject(  response.body().string());
			int total=json.getInt("total");
			System.out.println("userlist total:"+total);
			if(total==0)
			{
				return;
			}
			JSONObject jsondata=json.getJSONObject("data");
			JSONArray ja=jsondata.getJSONArray("openid");
			userlist.clear();
		  	for(int i=0;i<ja.length();i++)
		  	{
			  	userlist.add(ja.getString(i));
		  	}
			lastUpdateTime=System.currentTimeMillis();
		  	System.out.println("update userlist finished!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}


	public void setConfig(Config config) {
		this.config = config;
	}

	public void setToken(AccessToken token) {
		this.token = token;
	}
}
