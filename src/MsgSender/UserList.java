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
	//	private final static String URL_USER="https://101.226.90.58/cgi-bin/user/get";
	private long lastUpdateTime;
	private Vector<String> userlist;
	private AccessToken token;
	
	public UserList()
	{
		lastUpdateTime=Long.parseLong(ConfigFile.getProperty("lastUpdateTime"));
		setUserlist(new Vector<String>());

	}
	
	public UserList(AccessToken at)
	{
		this();
		this.token=at;
	}

	public Vector<String> getUserlist() {
		long cur=System.currentTimeMillis();
		System.out.println("getUserlist lastUpdateTime:"+lastUpdateTime);
		System.out.println("getUserlist currentTime   :"+cur);
//		if(cur-lastUpdateTime>3600000)
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
			lastUpdateTime=System.currentTimeMillis();
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
		      .url(url)
		      .build();

		  Response response = client.newCall(request).execute();
		  JSONObject json=new JSONObject(  response.body().string());
		  JSONObject jsondata=json.getJSONObject("data");
		  JSONArray ja=jsondata.getJSONArray("openid");
		  userlist.clear();
		  for(int i=0;i<ja.length();i++)
		  {
			  userlist.add(ja.getString(i));
		  }
		 
		  System.out.println("update userlist finished!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void setUserlist(Vector<String> userlist) {
		this.userlist = userlist;
	}
	
}
