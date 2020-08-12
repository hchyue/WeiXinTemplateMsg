package MsgSender;

import java.util.Timer;

import iMC.ImcMgr;
import probe.ProbeHttp;
import probe.ProbeICMP;
import probe.ProbeTelnet;

public class Main {
	private static int INTERVAL=1;//告警的间隔，分钟
	public static void main(String[] args) 
	{
		INTERVAL=Integer.parseInt(ConfigFile.getProperty("interval"));
		ImcMgr imc=new ImcMgr(AccessToken.TOKEN,INTERVAL);
		
	
    	Timer timer  = new Timer();
    	

		//立即开始后台运行，默认1分钟运行一次
		timer.schedule(imc,0,60*1000*INTERVAL);
		

	}

}
