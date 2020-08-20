package Common;

import org.json.JSONArray;
import org.json.JSONException;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class Common
{
	
//	public static final String DATASOURCE="java:/comp/env/jdbc/ncoadmin";
//	public static final String DATASOURCE=(new File("c:")).exists()? "java:/comp/env/jdbc/ncoadmin":"jdbc/ncoadmin";
	public static final String DATASOURCE="/".equals(System.getProperty("file.separator")) ?"jdbc/ncoadmin":"java:/comp/env/jdbc/ncoadmin";
	
	/** 任务类别，用于区分多线程的任务*/
	public static final String TASKTYPE_BUILDPROCESS="OPTYPE_NEW_VS";
	
	public static final String TASKTYPE_BUILDPROCESS4AddPoolMemberRefExistMember="buildProcess4AddPoolMemberRefExistMember";
	public static final String TASKTYPE_REPLACEADDR="replaceAddr";
	
	/** 5种集合关系常量定义*/
	public static final String IRRELEVANT="irrelevant";
	public static final String INTERSECT="intersect";
	public static final String CONTAIN="contain";
	public static final String EQUAL="equal";
	public static final String CONTAINED="contained";
	


	
	/** buildaction，用于步骤生成*/
	public static final String ACTION_NO="0";
	public static final String ACTION_NEW="1";
	public static final String ACTION_EXIST_NOT_MATCH = "5";
	
	/** 建立一个3维的表，每个维度分别有三种可能：CONTAIN、EQUAL和CONTAINED。
	 * 对应表里的内容为这三种集合交集状态的综合结果，
	 * 可能为CONTAIN、EQUAL/CONTAINED或者INTERSECT*/
	public static final String TABLE[][][]=
	{
		{{CONTAIN,CONTAIN,INTERSECT},{CONTAIN,CONTAIN,INTERSECT},{INTERSECT,INTERSECT,INTERSECT}},
		{{CONTAIN,CONTAIN,INTERSECT},{CONTAIN,EQUAL,CONTAINED},{INTERSECT,CONTAINED,CONTAINED}},
		{{INTERSECT,INTERSECT,INTERSECT},{INTERSECT,CONTAINED,CONTAINED},{INTERSECT,CONTAINED,CONTAINED}}
	};
	
	/** 建立一个2维的表，每个维度分别有三种可能：CONTAIN、EQUAL和CONTAINED。
	 * 对应表里的内容为这三种集合交集状态的综合结果，
	 * 可能为CONTAIN、EQUAL/CONTAINED或者INTERSECT*/
	public static final String TABLE2[][]=
	{
		{CONTAIN,CONTAIN,INTERSECT},{CONTAIN,EQUAL,CONTAINED},{INTERSECT,CONTAINED,CONTAINED}
	};
	public static String Service_Predefined_File="Sevice-Predefined.txt";
	public static String OK="OK";
	public static String FAIL="FAIL";
//	public static int Service_Predefined_Num=0;
	public final static String ComSep="#";
	public final static String ExcelSep=";";
	public final static String ANDSep="&";
	public final static String TELNET_PORT="3023";

	/** 双十一b2c扩容场景，新增member的数据库monitor需要依赖已有member的数据库monitor，因此在接口xml文件中增加此描述*/
	public static final String EXIST_MEMBER_MONITOR_DB = "EXIST_MEMBER_MONITOR_DB";
	

	public static int getBySep(String s,int i,String sep)
	{
		if(i<0 || s==null || !s.contains(sep))
			return -1;
		
		for(int j=0;j<i;j++)
		{
			if(s.contains(sep))
			{
				if(s.indexOf(sep)==s.lastIndexOf(sep))
				{
					return -1;
				}
				s=s.substring(s.indexOf(sep)+1);
			}
		}
		return Integer.parseInt(s.substring(0,s.indexOf(sep)));
	}

	public static String getStringBySep(String s,int i,String sep)
	{
		if(i<0 || s==null || s.equals(""))
			return "";
		if(!s.contains(sep))
		{
			return s;
		}
		for(int j=0;j<i;j++)
		{
			if(s.contains(sep))
			{
				if(s.indexOf(sep)==s.lastIndexOf(sep))
				{
					return "";
				}
				s=s.substring(s.indexOf(sep)+1);
			}
		}
		return s.substring(0,s.indexOf(sep));
	}
//	public static int getNumbySep(String s,String sep)
//	{
//		if(s==null || s.equals(""))
//			return 0;
//		if(!s.contains(sep))
//			return 1;
//		int i=0;
//		while(s.contains(sep) && s.indexOf(sep)!=s.lastIndexOf(sep))
//		{
//			////System.out.println("get num:"+s);
//			i++;
//			s=s.substring(s.indexOf(sep)+1);
//		}
//		
//		
//		return ++i;
//	}
	public static int getNumbySep(String s,String sep)
	{
		if(s==null || s.equals(""))
			return 0;
		if(!s.contains(sep))
			return 1;
		int i=0;
		while(s.contains(sep) && s.indexOf(sep)!=s.lastIndexOf(sep))
		{
			////System.out.println("get num:"+s);
			i++;
			s=s.substring(s.indexOf(sep)+1);
		}
		
		if(!s.endsWith(sep))
		{
			i++;
		}
		return ++i;
	}
	/*	public static int getNumbySep(String s,String sep)
	{
		if(s==null || !s.contains(sep))
			return 0;
		
		int i=0;
		int next=0;
		while(s.contains(sep))
		{
			////System.out.println("get num:"+s);
			i++;
			next=s.indexOf(sep);
			if(next==s.lastIndexOf(sep))
			{
				break;
			}
			s=s.substring(next+1);
		}
		
		return ++i;
	}
	*/
	public static boolean isIPaddr(String s)
	{
		if(s==null)
			return false;
		if(s.length()>15 || s.length()<7)
			return false;
		for(int i=0;i<s.length();i++)
		{
			if(s.charAt(i)!='.' )
			{
				if(s.charAt(i)<'0'||s.charAt(i)>'9')
				return false;
			}
		}
		//还需更精确的判断
		return true;
	}
	public static boolean isPortFormat(String s)
	{
		if(s==null || s.equals(""))
			return false;
		
		if(s.contains("-"))
		{
			if(s.indexOf("-")!=s.lastIndexOf("-"))
				return false;//more than one "-"
			if(s.startsWith("-"))
				return false;//start with "-"
			if(s.endsWith("-"))
				return false;//end with "-"
		}
		
		char ch;
		for(int i=0;i<s.length();i++)
		{//verify each char
			ch=s.charAt(i);
			if((ch>='0' && ch<='9') ||ch=='-')
				continue;
			else
				return false;
		}
		return true;
	}
	public static boolean isDigital(String s)
	{
		if(s==null || s.equals(""))
			return false;
		
	
		char ch;
		for(int i=0;i<s.length();i++)
		{//verify each char
			ch=s.charAt(i);
			if((ch>='0' && ch<='9'))
				continue;
			else
				return false;
		}
		return true;
	}
	
	/**
	 * 在s中增加n，确保s中的元素不重复，但不考虑排序
	 * @param s
	 * @param n
	 * @param sep
	 * @return
	 */
	public static String appUni(String s,int n,String sep)
	{
		
		if(s.length()==0)
		{
			s+=n;
			s+=sep;
		}
		else
		{
			int i=0;
			for(;i<Common.getNumbySep(s,sep);i++)
			{
				if(Common.getBySep(s, i,sep)==n)
				{
					break;
				}
			}
			if(i==Common.getNumbySep(s,sep))
			{
				s+=n;
				s+=sep;
			}
		}
	//	//System.out.println("appUnis:"+s);
		return s;
	}
	
	/**
	 * 对原来的方法进行改进，使字符串既unique又sorted,从小到大的顺序排列
	 * @param s
	 * @param n
	 * @param sep
	 * @return
	 */
	public static String appUniSorted(String s,int n,String sep)
	{
		String tmps=s;
		String sa[];
		if(s.length()==0)
		{
			s+=n;
			s+=sep;
			return s;
		}
		else
		{
			if(tmps.endsWith(sep))
			{
				tmps=tmps.substring(0,tmps.lastIndexOf(sep));

				sa=tmps.split(sep);
				int a[]=new int[sa.length];
				for(int j=0;j<a.length;j++)
				{
					a[j]=Integer.parseInt(sa[j]);
				}
				int i=0;
				for(;i<a.length;i++)
				{
					if(n<=a[i])
						break;
				}
				if(i==sa.length)
				{
					s=s+n+sep;
					return s;
				}
				if(n==a[i])
				{//相等
					return s;
				}
				tmps="";
				for(int j=0;j<=a.length;j++)
				{
					if(j<i)
					{
						tmps=tmps+a[j]+sep;
					}
					if(j==i)
					{
						tmps=tmps+n+sep;
					}
					if(j>i)
					{
						tmps=tmps+a[j-1]+sep;
					}
				}
				s=tmps;
			}
		}

		return s;
	}
	//分隔符作为一个参数
	public static String appUni(String s,String n,String sep)
	{
		if(s.length()==0)
		{
			s+=n;
			s+=sep;
		}
		else
		{
			int i=0;
			for(;i<Common.getNumbySep(s,sep);i++)
			{
				if(Common.getStringBySep(s, i,sep).equals(n))
				{
					break;
				}
			}
			if(i==Common.getNumbySep(s,sep))
			{
				s+=n;
				s+=sep;
			}
		}
	//	//System.out.println("appUnis:"+s);
		return s;
	}
	public static String getCurrentTime()
	{
		long time=0;
	//	SimpleDateFormat simDateFormat = new SimpleDateFormat("hh:mm:ss-SSS");
		SimpleDateFormat simDateFormat = new SimpleDateFormat("HH:mm:ss-SSS");
		time=System.currentTimeMillis();
		Date d=new Date(time);
		return simDateFormat.format(d);
	}
	public static String getCurrentTime2()
	{
		long time=0;
	//	SimpleDateFormat simDateFormat = new SimpleDateFormat("hh:mm:ss-SSS");
		SimpleDateFormat simDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		time=System.currentTimeMillis();
		Date d=new Date(time);
		return simDateFormat.format(d);
	}
	public static String getCurrentTime3()
	{
		long time=0;
	//	SimpleDateFormat simDateFormat = new SimpleDateFormat("hh:mm:ss-SSS");
		SimpleDateFormat simDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		time=System.currentTimeMillis();
		Date d=new Date(time);
		return simDateFormat.format(d);
	}
	public static String getCurrentTime4()
	{
		long time=0;
	//	SimpleDateFormat simDateFormat = new SimpleDateFormat("hh:mm:ss-SSS");
		SimpleDateFormat simDateFormat = new SimpleDateFormat("yyyyMMdd");
		time=System.currentTimeMillis();
		Date d=new Date(time);
		return simDateFormat.format(d);
	}
	public static int hashFunction(String s)
	{
		int hash=0;
		
		if(s==null)
		return hash;
		
		for(int i=0;i<s.length();i++)
		{
			hash+=s.charAt(i);
		}
		
		return hash;
	}

	public static String readLine(String s)
	{
		if(s==null)
			return null;
		if(!s.contains("\n"))
			return s;
		if(s.contains("\n"))
		{
			return s.substring(0,s.indexOf("\n"));
		}
		
		return null;
	}
	
	public static String int2dotMask(String s)
	{
		//String 必须是一个数
		if(!isDigital(s))
			return null;
		
		int bits=Integer.parseInt(s)-1;
		int segment=bits/8;
		int offset=bits%8;
		////System.out.println("segment:"+segment);
		////System.out.println("offset:"+offset);
		String maskStr="";
		int tmp=0;
		for(int i=0;i<segment;i++)
		{
			maskStr+="255.";
		}
		for(int i=0;i<=offset;i++)
		{
			tmp+=(int)Math.pow(2,(7-i));
		}
		maskStr+=tmp;
		for(int i=segment+1;i<4;i++)
		{
			maskStr+=".0";
			
		}
		return maskStr;
	}
	//public static String dot2intMask(String)
	//{
		
	//}
	public static String parseTokens(String cmdline)
	{
		//命令行为空格（多个），或者是注释字符开头返回"";
		if(cmdline==null || cmdline.trim().equals("")
				||cmdline.trim().startsWith("//")||cmdline.trim().startsWith("#"))
			return "";
		
		
		String tmpStr="";
		cmdline=cmdline.trim();
		//清楚改行后面的注释
		if(cmdline.contains("//"))
			cmdline=cmdline.substring(0,cmdline.indexOf("//"));
		//int count=0;
		while(cmdline.length()>0 )
		{//count++;
		////System.out.println("cmdline:"+cmdline);
			if(cmdline.startsWith("//")||cmdline.trim().startsWith("#"))
				break;
			else if(cmdline.startsWith("\""))
			{
				tmpStr+=cmdline.substring(0,cmdline.indexOf("\"", 1)+1);
				tmpStr+=ComSep;
				cmdline=cmdline.substring(cmdline.indexOf("\"", 1)+1).trim();
			}
			else
			{
				if(cmdline.contains(" "))
				{
					tmpStr+=cmdline.substring(0,cmdline.indexOf(" "));
					tmpStr+=ComSep;
					cmdline=cmdline.substring(cmdline.indexOf(" ")).trim();
				}
				else
				{
					tmpStr+=cmdline;
					tmpStr+=ComSep;
					break;
				}
			}
		}
		return tmpStr;
	}
	
	//parse   84.10.31.0/24;84.10.95.0/24
	//seperator is ";"
	//注意：最后没有sep
/*	public static int getNumbySep(String s,String sep)
	{
		if(s==null)
			return 0;
		if(!s.contains(sep))
			return 1;
		
		int i=0;
		while(s.contains(sep) && s.indexOf(sep)!=s.lastIndexOf(sep))
		{
			////System.out.println("get num:"+s);
			i++;
			s=s.substring(s.indexOf(sep)+1);
		}
		
		return i+2;
	}

	public static String getbySeq(String s,int i,String sep)
	{
	}
	*/
	
	//判断以ComSep分割的s中是否包含i
	public static boolean contain(String s,int i)
	{
		//System.out.println("s:"+s);
		//System.out.println("i:"+i);
		int num=getNumbySep(s,ComSep);
		int tmp;
		for(int j=0;j<num;j++)
		{
			tmp=getBySep(s,j,ComSep);
			if(tmp==i)
			return true;
			
		}
		
		return false;
	}
	
	/**
	 * 判断i是否包含在标准格式地址s中
	 * @param s 格式：start1#end1#start2#end2#....
	 * @param i
	 * @return
	 */
	public static boolean contain(String s,long i)
	{
		
		int num=getNumbySep(s,ComSep);
		long start=0;
		long end=0;
		for(int j=0;2*j+1<num;j++)
		{
			start=Long.parseLong(getStringBySep(s,2*j,ComSep));
			end=Long.parseLong(getStringBySep(s,2*j+1,ComSep));
			
			if(start<=i && i<=end)
			return true;
			
		}
		
		return false;
	}
	/**
	 * 判断i是否与标准格式地址s相同
	 * @param s 格式：start1#end1#start2#end2#....
	 * @param i
	 * @return
	 */
	public static boolean eqAddr(String s,long i)
	{
		
		int num=getNumbySep(s,ComSep);
		if(num!=2)
		{
			return false;
		}
		
		long start=0;
		long end=0;
	
		start=Long.parseLong(getStringBySep(s,0,ComSep));
		end=Long.parseLong(getStringBySep(s,1,ComSep));
			
			
		if(start==end && start==i)
		return true;
		
		return false;
	}
	/**
	 * 取IP的每个部分的数字
	 * @param ip
	 * @param i
	 * @return
	 */
	public static int oct(String ip,int i)
	{
		if(i<0||i>3 || !Common.isIPaddr(ip))
		{
			return -1;
		}
		
		if(i==0)
			return Integer.parseInt(ip.substring(0,ip.indexOf(".")));
		if(i==1)
		{
			ip=ip.substring(ip.indexOf(".")+1);
			return Integer.parseInt(ip.substring(0,ip.indexOf(".")));
		}
		if(i==2)
		{
			ip=ip.substring(ip.indexOf(".")+1);
			ip=ip.substring(ip.indexOf(".")+1);
			return Integer.parseInt(ip.substring(0,ip.indexOf(".")));
		}
		if(i==3)
		{
			return Integer.parseInt(ip.substring(ip.lastIndexOf(".")+1));
		}
	
		return -1;
	}

	/**
	 * 根据数轴原理合并
	 * @param a
	 * @param len
	 * @param start
	 * @param end
	 * @return 合并后数值的数值的有效长度
	 */
	public static int sortMerge(long[] a, int len,long start, long end)
	{
		if(a==null || len<0 || len>a.length)
			return 0;
		
		//初始化start和end在a中位置，-1表示在a[0]之前
		int starti=-1,endi=-1;
		//(1)下面的算法在a中查找start和end的位置
		for(int i=0;i<len;i++)
		{
			//Log.record(Log.INFO, Log.getLineInfo(), "a["+i+"]:"+a[i]);
			if(a[i]<start)
			{
				starti=i;
			//	continue;
			}
			if(a[i]<end)
			{
				endi=i;
				continue;
			}
			break;
		}
		
		//Log.record(Log.INFO, Log.getLineInfo(), "start:"+start+" end:"+end);
		//Log.record(Log.INFO, Log.getLineInfo(), "starti:"+starti+" endi:"+endi);
		//(2)根据starti和endi奇偶组合分为四种情况
		
		//(2.1)奇奇---大体上分为2种情况：相等和不等
			//(2.1.1)奇奇-start和end相等
			//(2.1.2)奇奇-start和end不相等
		
		if(starti%2!=0 && endi%2!=0)
		{
			if(starti==endi)
			{//(2.1.1)奇奇-start和end相等
				//(2.1.1.1)奇奇-start和end相等且在开头
				//(2.1.1.2)奇奇-start和end相等且在最后
				//(2.1.1.3)奇奇-start和end相等且在中间
				if(starti==-1)
				{//(2.1.1.1)奇奇-start和end相等且在开头
					//处理：(2.1.1.1.1)len=0,此时把start和end放在前面
					//	   (2.1.1.1.2)不可就后合并,此时a中所有元素后移2个位置，start和end放在前面
					//     (2.1.1.1.3)需要就后合并，此时修改a[0]为start
					if(len==0)
					{//(2.1.1.1.1)len=0,此时把start和end放在前面
						a[0]=start;
						a[1]=end;
						len=2;
						return len;
					}
					if(a[0]>end+1)
					{//(2.1.1.1.2)不可就后合并,此时a中所有元素后移2个位置，start和end放在前面
						for(int i=len-1;i>=0;i--)
						{
							a[i+2]=a[i];
						}
						a[0]=start;
						a[1]=end;
						len=len+2;
						return len;
					}
					if(a[0]==end || a[0]==end+1)
					{//(2.1.1.1.3)需要就后合并，此时修改a[0]为start
						a[0]=start;
						return len;
					}
				}
				
				if(starti==len-1)
				{//(2.1.1.2)奇奇-start和end相等且在最后
					//	   (2.1.1.2.1)不可就前合并
					//     (2.1.1.2.2)需要就前合并
					
					if(a[endi]<start-1)
					{//	   (2.1.1.2.1)不可就前合并
						a[endi+1]=start;
						a[endi+2]=end;
						len=len+2;
						return len;
					}
					if(a[endi]==start-1)
					{//     (2.1.1.2.2)需要就前合并
						a[endi]=end;
						return len;
					}
				}
				
				if(starti>-1 && endi<len)
				{//(2.1.1.3)奇奇-start和end相等且在中间
					//(2.1.1.3.1)不可合并
					//(2.1.1.3.2)需要就前合并
					//(2.1.1.3.3)需要就后合并
					//(2.1.1.3.4)既需要就前合并，也需要就后合并
					
					if(a[starti]<start-1 && a[endi+1]>end+1)
					{//(2.1.1.3.1)不可合并
						for(int i=len-1;i>starti;i--)
						{
							a[i+2]=a[i];
						}
						a[starti+1]=start;
						a[starti+2]=end;
						len=len+2;
						return len;
					}
					if(a[starti]==start-1 && a[endi+1]>end+1)
					{//(2.1.1.3.2)需要就前合并
						
						a[starti]=end;
						return len;
					}
					if(a[starti]<start-1 && (a[endi+1]==end || a[endi+1]==end+1))
					{//(2.1.1.3.3)需要就后合并
						
						a[endi+1]=start;
						return len;
					}
					if(a[starti]==start-1 && (a[endi+1]==end || a[endi+1]==end+1))
					{//(2.1.1.3.4)既需要就前合并，也需要就后合并
						
						for(int i=endi+2;i<len;i++)
						{
						//	a[starti]=a[endi+2];
							a[i-2]=a[i];
						}
						len=len-2;
						return len;
					}
				}
			}
			else
			{//(2.1.2)奇奇-start和end不相等,这种情况肯定要合并
				//(2.1.2.1) starti=-1 endi=len-1
				//(2.1.2.2) starti=-1 endi<len-1
				//(2.1.2.3) starti>-1 endi<len-1
				//(2.1.2.4) starti>-1 endi=len-1
				if(starti==-1 && endi==len-1)
				{//(2.1.2.1) starti=-1 endi=len-1
					a[0]=start;
					a[1]=end;
					len=2;
					return len;
				}
				if(starti==-1 && endi<len-1)
				{//(2.1.2.2) starti=-1 endi<len-1
					if(a[endi+1]>end+1)
					{//(2.1.2.2.1)不可就后合并

						a[0]=start;
						a[1]=end;
						for(int i=endi+1;i<len;i++)
						{
							a[i-(endi+1)+2]=a[i];
						}
						len=len-(endi-starti)+2;
						return len;
					}
					if(a[endi+1]==end || a[endi+1]==end+1)
					{//(2.1.2.2.2)需要就后合并
						a[0]=start;
						for(int i=endi+2;i<len;i++)
						{
							a[i-(endi+2)+1]=a[i];
						}
						len=len-(endi-starti);
						return len;
					}
				}
				if(starti>-1 && endi<len-1)
				{//(2.1.2.3) starti>-1 endi<len-1
					//(2.1.2.3.1)不可合并
					//(2.1.2.3.2)需要就前合并
					//(2.1.2.3.3)需要就后合并
					//(2.1.2.3.4)既需要就前合并，也需要就后合并
					if(a[starti]<start-1 && a[endi+1]>end+1)
					{//(2.1.2.3.1)不可合并
						a[starti+1]=start;
						a[starti+2]=end;
						for(int i=endi+1;i<len;i++)
						{
							a[starti+3+i-(endi+1)]=a[i];
						}
						len=len-(endi-starti)+2;
						return len;
					}
					if(a[starti]==start-1 && a[endi+1]>end+1)
					{//(2.1.2.3.2)需要就前合并
						a[starti]=end;
						for(int i=endi+1;i<len;i++)
						{
							a[starti+1+i-(endi+1)]=a[i];
						}
						len=len-(endi-starti);
						return len;
					}
					if(a[starti]<start-1 && (a[endi+1]==end || a[endi+1]==end+1))
					{//(2.1.2.3.3)需要就后合并
						a[starti+1]=start;
						for(int i=endi+2;i<len;i++)
						{
							a[starti+2+i-(endi+2)]=a[i];
						}
						len=len-(endi-starti);
						return len;
					}
					if(a[starti]==start-1 && (a[endi+1]==end || a[endi+1]==end+1))
					{//(2.1.2.3.4)既需要就前合并，也需要就后合并
						a[starti]=a[endi+2];
						for(int i=endi+3;i<len;i++)
						{
							a[starti+1+i-(endi+3)]=a[i];
						}
						len=len-(endi-starti)-2;
						return len;
					}
				}
				if(starti>-1 && endi==len-1)
				{//(2.1.2.4) starti>-1 endi=len-1
					//(2.1.2.4.1)不可就前合并
					//(2.1.2.4.2)需要就前合并
					if(a[starti]<start-1)
					{//(2.1.2.4.1)不可就前合并
						a[starti+1]=start;
						a[starti+2]=end;
						len=len-(endi-starti)+2;
						return len;
					}
					if(a[starti]==start-1)
					{//(2.1.2.4.2)需要就前合并
						a[starti]=end;
						len=len-(endi-starti);
						return len;
					}
				}
			}
			
			
		}
		//(2.2)奇偶
			//(2.2.1)starti==-1
			//(2.2.2)starti>-1
		if(starti%2!=0 && endi%2==0)
		{//(2.2)奇偶
			
			if(starti==-1)
			{//(2.2.1)starti==-1
				a[0]=start;
				for(int i=endi+1;i<len;i++)
				{
					a[1+i-(endi+1)]=a[i];
				}
				len=len-(endi-starti-1);
				return len;
			}
			if(starti>-1)
			{//(2.2.2)starti>-1
				//(2.2.2.1)不可就前合并
				//(2.2.2.2)需要就前合并
				
				if(a[starti]<start-1)
				{//(2.2.2.1)不可就前合并
					a[starti+1]=start;
					for(int i=endi+1;i<len;i++)
					{
						a[starti+2+i-(endi+1)]=a[i];
					}
					len=len-(endi-starti-1);
					return len;
				}
				if(a[starti]==start-1)
				{//(2.2.2.2)需要就前合并
					for(int i=endi+1;i<len;i++)
					{
						a[starti+i-(endi+1)]=a[i];
					}
					len=len-(endi-starti+1);
					return len;
				}
			}
		}
		//(2.3)偶奇
			//(2.3.1)endi==len-1
			//(2.3.2)endi<len-1
		if(starti%2==0 && endi%2!=0)
		{//(2.3)偶奇
			if(endi==len-1)
			{//(2.3.1)endi==len-1
				a[starti+1]=end;
				len=len-(endi-starti-1);
				return len;
			}
			if(endi<len-1)
			{//(2.3.2)endi<len-1
				//(2.3.2.1)不可就后合并
				//(2.3.2.2)需要就后合并
				if(a[endi+1]>end+1)
				{//(2.3.2.1)不可就后合并
					a[starti+1]=end;
					for(int i=endi+1;i<len;i++)
					{
						a[starti+2+i-(endi+1)]=a[i];
					}
					len=len-(endi-starti-1);
					return len;
				}
				if(a[endi+1]==end || a[endi+1]==end+1)
				{//(2.3.2.2)需要就后合并
					a[starti+1]=a[endi+2];
					for(int i=endi+3;i<len;i++)
					{
						a[starti+2+i-(endi+3)]=a[i];
					}
					len=len-(endi-starti+1);
					return len;
				}
			}
		}
		
		//(2.4)偶偶
			//(2.4.1)偶偶-start和end相等
			//(2.4.2)偶偶-start和end不相等
		if(starti%2==0 && endi%2==0)
		{//(2.4)偶偶
			
			if(starti==endi)
			{//(2.4.1)偶偶-start和end相等
				return len;
			}
			if(starti!=endi)
			{//(2.4.2)偶偶-start和end不相等,要合并
				for(int i=endi+1;i<len;i++)
				{
					a[starti+1+i-(endi+1)]=a[i];
				}
				len=len-(endi-starti);
				return len;
			}
		}
		return -1;
	}
	
	/**
	 * 获得一个网络的第一个地址的值
	 * @param network
	 * @param netmask like 255.255.0.0
	 * @return
	 */
	public static long getFirstAddrValue(String network, String netmask)
	{
		long tmp=0;
		long ValueStart=0;
		for(int i=0;i<4;i++)
		{
			tmp=Common.oct(network, i)&Common.oct(netmask, i);
			ValueStart=ValueStart+tmp*(long)Math.pow(2, (3-i)*8);
		}
		return ValueStart;
	}

	/**
	 * 从一个数值，得到字符串格式的IP地址
	 * @param value
	 * @return
	 */
	public static String value2StrAddr(long value)
	{
		String s="";
		for(int i=0;i<4;i++)
		{
			s=s+value/(long)Math.pow(2, (3-i)*8);
			if(i<3)
			{
				s+=".";
			}
			value=value%(long)Math.pow(2, (3-i)*8);
		}
		
		return s;
	}
	
	/**
	 * 把地址从value格式的字符串变成ip地址范围表示的字符串
	 * @param value
	 * @return
	 */
	public static String value2StrAddr(String value)
	{
		if(value.equals(""))
			return "";
		if(value.endsWith(ComSep))
			value=value.substring(0,value.lastIndexOf(ComSep));
		
		String tmp[]=value.split(ComSep);
		String s="";
		
		for(int i=0;2*i+1<tmp.length;i++)
		{
			long start=Long.parseLong(tmp[2*i]);
			long end=Long.parseLong(tmp[2*i+1]);
			
			if(start==end)
			{
				s=s+value2StrAddr(start)+ExcelSep;
			}
			else
			{
				s=s+value2StrAddr(start)+"-"+value2StrAddr(end)+ExcelSep;
			}
		}
		if(s.endsWith(ExcelSep))
		{
			s=s.substring(0,s.lastIndexOf(ExcelSep));
		}
		return s;
	}
	
	/**
	 * 把端口从value标准格式的字符串变成数值范围表示的可读格式
	 * @param value
	 * @return
	 */
	public static String value2StrPort(String value)
	{
		if(value.equals(""))
			return "";
		if(value.endsWith(ComSep))
			value=value.substring(0,value.lastIndexOf(ComSep));
		
		String tmp[]=value.split(ComSep);
		String s="";
		
		for(int i=0;2*i+1<tmp.length;i++)
		{
			int start=Integer.parseInt(tmp[2*i]);
			int end=Integer.parseInt(tmp[2*i+1]);
			
			if(start==end)
			{
				s=s+start+ExcelSep;
			}
			else
			{
				s=s+start+"-"+end+ExcelSep;
			}
		}
		if(s.endsWith(ExcelSep))
		{
			s=s.substring(0,s.lastIndexOf(ExcelSep));
		}
		return s;
	}
	/**
	 * 从字符串格式的IP地址，得到一个数值
	 * @param value
	 * @return
	 */
	public static long StrAddr2value(String network)
	{
		long tmp=0;
		long Value=0;
		for(int i=0;i<4;i++)
		{
			tmp=Common.oct(network, i);
			Value=Value+tmp*(long)Math.pow(2, (3-i)*8);
		}
		return Value;
	}
	/**
	 * 获得一个网络的地址的偏移量//的确是offset!
	 * @param netmask 255.255.255.0
	 * @return
	 */
	public static long getAddrOffset(String netmask)
	{
		long tmp=0;
		long offset=0;
		for(int i=0;i<4;i++)
		{
			tmp=Common.oct(netmask, i)^ 255;
			offset=offset+tmp*(long)Math.pow(2, (3-i)*8);
		}
		return offset;
	}
	/**
	 * 获得一个网络的第一个地址的值
	 * @param network
	 * @param netmask
	 * @return
	 */
	public static long getLastAddrValue(String network,String mask)
	{
		long first=getFirstAddrValue(network, mask);
		long offset=getAddrOffset(mask);
		return first+offset;
	}
	/**
	 * 获得一个网络的地址的偏移量
	 * @param netmask 24
	 * @return
	 */
	public static long getAddrOffset(int netmask)
	{
		
		return (long)Math.pow(2, 32-netmask)-1;
	}
	
	
	
	
	/**
	 * 结合运算，判断一个集合是否包含另一个
	 * 例如：判断setA是否包含setB，集合运算
	 * @param setA  a0#a1#a2#a3#...
	 * @param setB		i0#i1#....
	 * @return
	 */
	public static boolean setContain(String setA, String setB)
	{//如果ip==""，是查询条件为空，这时会返回true
		
		
		if(setA==null || setA.equals(""))
			return false;
		if(setB==null || setB.equals(""))
			return true;
		
		int lena=getNumbySep(setA,ComSep);
		int lenipa=getNumbySep(setB,ComSep);
		long a[]=new long[lena];
		long ipa[]=new long[lenipa];
		for(int i=0;i<lena;i++)
		{
			a[i]=Long.parseLong(getStringBySep(setA,i,ComSep));
		}
		for(int i=0;i<lenipa;i++)
		{
			ipa[i]=Long.parseLong(getStringBySep(setB,i,ComSep));
		}
		if(ipa[lenipa-1]<a[0] || ipa[0]>a[lena-1])
			return false;
		
		
		//(1)下面的算法在a中查找start和end的位置
		for(int i=0;2*i+1<lenipa;i++)
		{
			//初始化start和end在a中位置，-1表示在a[0]之前
			int startj=-1,endj=-1;
			
			int j=0;
			for(j=0;j<lena;j++)
			{
				if(a[j]<ipa[2*i])
				{
					startj=j;
				//	continue;
				}
				if(a[j]<ipa[2*i+1])
				{
					endj=j;
					continue;
				}
				break;
			}
			//Log.record(Log.INFO, Log.getLineInfo(), "ipa["+2*i+"]:"+ipa[2*i]+" ipa["+(2*i+1)+"]"+ipa[2*i+1]);
			//Log.record(Log.INFO, Log.getLineInfo(), "lena:"+lena);
			//Log.record(Log.INFO, Log.getLineInfo(), "lenipa:"+lenipa);
			//Log.record(Log.INFO, Log.getLineInfo(), "startj:"+startj+" endj:"+endj);
			//Log.record(Log.INFO, Log.getLineInfo(), "i:"+i);
			//如果ipa中每段都在a中，就继续
			if(startj==endj)
			{
				if(endj%2==0)
				{//ipa[2*i]和ipa[2*i+1]同时落在a的有效段中，且不会等于a的这一段，因为a的最小值不会再ipa中
					continue;
				}
				if(endj+1<lena && ipa[2*i]==a[endj+1])
				{
					continue;
				}
				
			}
			if(endj-startj==1)
			{
				if(endj%2==0 && ipa[2*i]==a[endj+1])
				{
					continue;
				}
			}
			
				return false;//只有不包含的情况下才走这里
		}
		return true;
	}
	
	/**
	 * 根据标准格式，算出两个集合的交集，
	 * @param setA
	 * @param setB
	 * @return 交集也是标准格式 1#2#4#4#...
	 */
	public static String getIntersect(String setA,String setB)
	{
		
		String intersect="";
		long inter[]=new long[1024];
		int leninter=0;
		if(setA==null || setA.equals("")||setB==null || setB.equals(""))
			return "";
		
		while(setA.endsWith(ComSep))
		{
			setA=setA.substring(0,setA.lastIndexOf(ComSep));
		}
		
		while(setB.endsWith(ComSep))
		{
			setB=setB.substring(0,setB.lastIndexOf(ComSep));
		}
		String sA[]=setA.split(ComSep);
		String sB[]=setB.split(ComSep);
		int lenA=sA.length;
		int lenB=sB.length;
		long A[]=new long[lenA];
		long B[]=new long[lenB];
		
		for(int i=0;i<sA.length;i++)
		{
			A[i]=Long.parseLong(sA[i]);
		}
		for(int i=0;i<sB.length;i++)
		{
			B[i]=Long.parseLong(sB[i]);
		}
		//开始判断
		if(lenA>=2 && lenB>=2 )
		{//把很容易判断交集的情况先列出来，有很多是这种情况
			if(A[lenA-1]<B[0] || A[0]>B[lenB-1])
			{
//				Log.record(Log.INFO, Log.getLineInfo(), "A["+(lenA-1)+"]:"+A[lenA-1]+" B[0]:"+B[0]+" A[0]:"+A[0]+" B["+(lenB-1)+"]:"+B[lenB-1]);
				return "";
			}
		}
		
		//(1)下面的算法在a中查找start和end的位置
		String com="";
		String coms[];
		long start,end;
		for(int i=0;2*i+1<lenB;i++)
		{
			for(int j=0;2*j+1<lenA;j++)
			{
				com=getCom(A[2*j],A[2*j+1],B[2*i],B[2*i+1]);
				if(!com.equals(""))
				{
					while(com.endsWith(ComSep))
					{
						com=com.substring(0,com.lastIndexOf(ComSep));
					}
					coms=com.split(ComSep);
					start=Long.parseLong(coms[0]);
					end=Long.parseLong(coms[1]);
					leninter=sortMerge(inter,leninter,start,end);
				}
			}
		}
		for(int i=0;i<leninter;i++)
		{
			intersect=intersect+inter[i]+ComSep;
		}
		
		return intersect;
	}
	
	/**
	 * 根据非标准格式，算出两个集合的交集，
	 * @param setA
	 * @param setB
	 * @return 交集也是非标准格式 1#2#3#4#...
	 */
	public static String getIntersect1(String setA,String setB)
	{
		
		String intersect="";
		if(setA==null || setA.equals("")||setB==null || setB.equals(""))
			return "";
		
		while(setA.endsWith(ComSep))
		{
			setA=setA.substring(0,setA.lastIndexOf(ComSep));
		}
		
		while(setB.endsWith(ComSep))
		{
			setB=setB.substring(0,setB.lastIndexOf(ComSep));
		}
		String sA[]=setA.split(ComSep);
		String sB[]=setB.split(ComSep);
		int lenA=sA.length;
		int lenB=sB.length;
		long A[]=new long[lenA];
		long B[]=new long[lenB];
		
		for(int i=0;i<sA.length;i++)
		{
			A[i]=Long.parseLong(sA[i]);
		}
		for(int i=0;i<sB.length;i++)
		{
			B[i]=Long.parseLong(sB[i]);
		}
		for(int i=0;i<A.length;i++)
		{
			for(int j=0;j<B.length;j++)
			{
				if(A[i]==B[j])
				{
					intersect=intersect+A[i]+ComSep;
				}
			}
			
			
		}
		return intersect;
	}
	
	/**
	 * 获取s1#e1# 和s2#e2#的交集
	 * @param s1
	 * @param e1
	 * @param s2
	 * @param e2
	 * @return 如果没有交集，返回""；否则返回 interstart#interend#
	 */
	public static String getCom(long s1,long e1,long s2,long e2)
	{
		String com="";
		//1
		if(e1<s2)
		{
			return com;
		}
		//2
		if(e1>=s2 && e1<= e2 && s1<=s2)
		{
			com=s2+ComSep+e1+ComSep;
			return com;
		}
		//3
		if(s1<=s2 && e1>=e2)
		{
			com=s2+ComSep+e2+ComSep;
			return com;
		}
		//4
		if(e1>=s2 && e1<= e2 && s1>=s2)
		{
			com=s1+ComSep+e1+ComSep;
			return com;
		}
		//5
		if(s1>=s2 && s1<= e2 && e1>=e2)
		{
			com=s1+ComSep+e2+ComSep;
			return com;
		}
		//6
		if(s1>e2)
		{
			return com;
		}
		
		return com;
	}

	/**
	 * 获取2个集合的关系
	 * @param setA  标准格式 1#2#4#4#...
	 * @param setB  标准格式 1#2#4#4#...
	 * @return 返回5种关系
	 */
	public static String getSetRelation(String setA, String setB)
	{
	//	if(setA==null || setA.equals("")|| setB==null || setB.equals(""))
	//	return IRRELEVANT;
		if(setA==null || setA.equals(""))
		{
			if(setB==null || setB.equals(""))
				return EQUAL;
		}
		if(setA==null || setA.equals(""))
		{
			if(setB!=null && !setB.equals(""))
				return CONTAINED;
		}
		if(setA!=null && !setA.equals(""))
		{
			if(setB==null || setB.equals(""))
				return CONTAIN;
		}
		
		String intersect=Common.getIntersect(setA,setB);
		if(intersect.equals(""))
		{
			return IRRELEVANT;
		}
		if(intersect.equals(setA) && intersect.equals(setB))
		{
			return EQUAL;
		}
		if(intersect.equals(setA) )
		{
			return CONTAINED;
		}
		if(intersect.equals(setB))
		{
			return CONTAIN;
		}
	
		return INTERSECT;
	}
	/**
	 * 获取2个集合的关系
	 * @param setA  非标准格式 1#2#3#4#...
	 * @param setB  非标准格式 1#2#3#4#...
	 * @return 返回5种关系
	 */
	public static String getSetRelation1(String setA, String setB)
	{
	//	if(setA==null || setA.equals("")|| setB==null || setB.equals(""))
	//	return IRRELEVANT;
		if(setA==null || setA.equals(""))
		{
			if(setB==null || setB.equals(""))
				return EQUAL;
		}
		if(setA==null || setA.equals(""))
		{
			if(setB!=null && !setB.equals(""))
				return CONTAINED;
		}
		if(setA!=null && !setA.equals(""))
		{
			if(setB==null || setB.equals(""))
				return CONTAIN;
		}
		
		String intersect=Common.getIntersect1(setA,setB);
		if(intersect.equals(""))
		{
			return IRRELEVANT;
		}
		if(intersect.equals(setA) && intersect.equals(setB))
		{
			return EQUAL;
		}
		if(intersect.equals(setA) )
		{
			return CONTAINED;
		}
		if(intersect.equals(setB))
		{
			return CONTAIN;
		}
	
		return INTERSECT;
	}
	
	
	/**
//	 * 获取2个集合的关系，同时返回交集。关系和交集之间使用&来分割，格式：EQUAL&1#2#4#4#...&
	 * @param setA  标准格式 1#2#4#4#...
	 * @param setB  标准格式 1#2#4#4#...
	 * @return 返回5种关系 A 关系B
	 */
	public static String getSetRelation2(String setA, String setB)
	{
		String sep=ANDSep;
	//	if(setA==null || setA.equals("")|| setB==null || setB.equals(""))
	//	return IRRELEVANT;
		if(setA==null || setA.equals(""))
		{
			if(setB==null || setB.equals(""))
				return EQUAL+sep+""+sep;
		}
		if(setA==null || setA.equals(""))
		{
			if(setB!=null && !setB.equals(""))
				return CONTAINED+sep+""+sep;
		}
		if(setA!=null && !setA.equals(""))
		{
			if(setB==null || setB.equals(""))
				return CONTAIN+sep+""+sep;
		}
		
		String intersect=Common.getIntersect(setA,setB);
	
		if(intersect.equals(""))
		{
			return IRRELEVANT+sep+""+sep;
		}
		if(intersect.equals(setA) && intersect.equals(setB))
		{
			return EQUAL+sep+intersect+sep;
		}
		if(intersect.equals(setA) )
		{
			return CONTAINED+sep+intersect+sep;
		}
		if(intersect.equals(setB))
		{
			return CONTAIN+sep+intersect+sep;
		}
	
		return INTERSECT+sep+intersect+sep;
	}
	
	/**
	 * 根据TABLE表来得到参数组合对应的值
	 * @param s1
	 * @param s2
	 * @param s3
	 * @return
	 */
	public static String getFromTable(String s1,String s2,String s3)
	{
		if(s1==null||s1.equals("")||s2==null||s2.equals("")||s3==null||s3.equals(""))
			return IRRELEVANT;
		int index1=0,index2=0,index3=0;
		if(s1.equals(CONTAIN))
		{
			index1=0;
		}
		else if(s1.equals(EQUAL))
		{
			index1=1;
		}
		else if(s1.equals(CONTAINED))
		{
			index1=2;
		}	
		
		if(s2.equals(CONTAIN))
		{
			index2=0;
		}
		else if(s2.equals(EQUAL))
		{
			index2=1;
		}
		else if(s2.equals(CONTAINED))
		{
			index2=2;
		}
		
		if(s3.equals(CONTAIN))
		{
			index3=0;
		}
		else if(s3.equals(EQUAL))
		{
			index3=1;
		}
		else if(s3.equals(CONTAINED))
		{
			index3=2;
		}	
		
		return TABLE[index1][index2][index3];
		
	}
	
	/**
	 * 根据TABLE2表来得到参数组合对应的值
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static String getFromTable(String s1,String s2)
	{
		if(s1==null||s1.equals("")||s2==null||s2.equals(""))
			return IRRELEVANT;
		int index1=0,index2=0;
		if(s1.equals(CONTAIN))
		{
			index1=0;
		}
		else if(s1.equals(EQUAL))
		{
			index1=1;
		}
		else if(s1.equals(CONTAINED))
		{
			index1=2;
		}	
		
		if(s2.equals(CONTAIN))
		{
			index2=0;
		}
		else if(s2.equals(EQUAL))
		{
			index2=1;
		}
		else if(s2.equals(CONTAINED))
		{
			index2=2;
		}
		
		return TABLE2[index1][index2];
		
	}
	
	/**
	 * 两个参数是value标准格式，求little和large的比率
	 * @param large
	 * @param little
	 * @return
	 */
	public static double getContainRate(String large,String little)
	{
	//	Log.record(Log.INFO, Log.getLineInfo(), "large:"+large+" little:"+little);
		double d=0;
		if(large.endsWith(ComSep))
			large=large.substring(0,large.lastIndexOf(ComSep));
		if(little.endsWith(ComSep))
			little=little.substring(0,little.lastIndexOf(ComSep));
		if(large.equals("")||little.equals(""))
			return d;
		
		String largeStr[]=large.split(ComSep);
		String littleStr[]=little.split(ComSep);
		double largeCount=0,littleCount=0;
		
		for(int i=0;2*i+1<largeStr.length;i++)
		{
			largeCount=largeCount+Long.parseLong(largeStr[2*i+1])-Long.parseLong(largeStr[2*i])+1;
	//		Log.record(Log.INFO, Log.getLineInfo(), "Long.parseLong(largeStr[2*i+1]):"+Long.parseLong(largeStr[2*i+1]));
	//		Log.record(Log.INFO, Log.getLineInfo(), "Long.parseLong(largeStr[2*i]):"+Long.parseLong(largeStr[2*i]));
	//		Log.record(Log.INFO, Log.getLineInfo(), "largeCount:"+largeCount);
		}
		for(int i=0;2*i+1<littleStr.length;i++)
		{
			littleCount=littleCount+Long.parseLong(littleStr[2*i+1])-Long.parseLong(littleStr[2*i])+1;
			//Log.record(Log.INFO, Log.getLineInfo(), "littleCount:"+littleCount);
		}
	//	Log.record(Log.INFO, Log.getLineInfo(), "largeCount:"+largeCount+" littleCount:"+littleCount);
		if(largeCount!=0)
		{
			d=littleCount/largeCount;
		}
		return d;
	}
	public static String htmlColor(String s,String c)
	{
		String tmp="<font style='color:"+c+"'>";
		tmp=tmp+s+"</font>";
		return tmp;
	}

	/**
	 * 把\n转化为<br>
	 * @param tmpStr
	 * @return
	 */
	public static String n2HTMLbr(String tmpStr)
	{
		if(tmpStr==null)
			return "";
		tmpStr=tmpStr.replace("\n", "<br>");
		return tmpStr;
	}

	/**
	 * 从indexset中查找index，并去除index
	 * @param s 1#111#...
	 * @param n   1
	 * 
	 * @return 111#...
	 */
	public static String removeIndex(String s, int n)
	{
		String subs=n+ComSep;
		if(s==null)
			return "";
		if(s.equals(subs))
		{
			return "";
		}
		if(s.startsWith(n+ComSep))
		{
			return s.substring(subs.length());
		}
		
		if(s.contains(ComSep+subs))
		{
			return s.replaceAll(ComSep+subs, ComSep);
		}
		//没有匹配则原样返回
		return s;
	}

	/**
	 * 计算出 valueA-valueB
	 * @param valueA value的标准格式
	 * @param valueB value的标准格式
	 * @return
	 */
/*	public static String getSetMinus(String valueA,String valueB)
	{
		
		String intersection=getIntersect(valueA,valueB);
		
		//1.intersection为空，直接返回A
		if(intersection.equals(""))
			return valueA;
		//2.intersection=valueA，直接返回A
		if(intersection.equals(valueA))
			return "";
		//3.intersection真包含于valueA,且都不为空
		
		for(int i=0;(2*i+1)<getNumbySep(valueB,ComSep);i++)
		{
			for(int j=0;(2*j+1)<getNumbySep(valueA,ComSep);j++)
			{
				if(getStringBySep(valueB,2*i,ComSep).equals(getStringBySep(valueA,2*j,ComSep)))
				{
					
				}
			}
		}
		 
		return null;
	}
*/
	/**
	 * 去除元素名字首尾的引号
	 * @param name
	 * @return
	 */
	public static String removeQuote(String name)
	{
		if(name==null)
		return "";
		if(name.startsWith("\"") && name.endsWith("\""))
		{
			name=name.substring(1,name.lastIndexOf("\""));
		}
		return name;
	}
	public static void main(String args[])
	{
//		String a="443#443#";
//		String b="1521#1521#";
//		String s=getSetRelation2(a,b);
//		System.out.println("getSetRelation2(a,b):"+s);
//		long a=2013265919;
//		String s=value2StrAddr(a);
//		System.out.println(s);
//		String a1="84.20.1.0";
//		String mask="24";
//		String a1="10.10.10.251";
////		System.out.println(StrAddr2value(a));
//		System.out.println(value2StrAddr(getFirstAddrValue(a1, int2dotMask(mask))));
//		
//		mask="28";
//		System.out.println(value2StrAddr(getFirstAddrValue(a1, int2dotMask(mask))));
//		System.out.println(getLastAddrValue(a1, int2dotMask(mask)));
//		String s=translateAddrFromHumanToStd("80.0.0.0/4#96.0.0.0/4#112.0.0.0/5#121.0.0.0/8#122.0.0.0/8#123.0.0.0/8#10.240.0.0/16#10.241.0.0/16#10.242.0.0/16#10.243.0.0/16#83.13.0.0/16#","#");
//		System.out.println(s);
//		int i=dotMask2Int("0.255.255.255");
//		System.out.println(i);
		String str = "F-EN";
//		 System.out.println(getPinyin(str));
	}

	/**
	 * 将value格式的port改成易读的格式
	 * @param reqRowPort
	 * @return
	 */
	public static String convertPortFromValue2Str(String reqRowPort)
	{
		String s="";
		int num=getNumbySep(reqRowPort, Common.ComSep);
		for(int i=0;2*i+1<num;i++)
		{
			String tmp1=getStringBySep(s, 2*i, Common.ComSep);
			String tmp2=getStringBySep(s, 2*i+1, Common.ComSep);
			if(tmp1.equals(tmp2))
			{
				s=s+tmp1+";";
			}
			else
			{
				s=s+tmp1+"-"+tmp2+";";
			}
			
		}
		if(s.endsWith(";"))
		{
			s=s.substring(0,s.length()-1);
		}
		return s;
	}
	
	
	/**
	 * 检查地址和掩码是否匹配，即：network and !mask==0
	 * @param network
	 * @param mask
	 * @return
	 */
	public static boolean isAddrMaskPair(String network, String mask)
	{
		long networkvalue=StrAddr2value(network);
		long maskvalue=StrAddr2value(mask);
		long max=(long) Math.pow(2, 32);
		long antiMaskvalue=max-1-maskvalue;
		if((networkvalue & antiMaskvalue) ==0)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 解析地址，将人常用的6中格式（excel表格中）转变为标准格式。
	 * 所谓标准格式：start1#end1#start2#end2#...
	 * 
	 * @param addr 人常用的6中格式（excel表格中）
	 * @return
	 */
	public static String translateAddrFromHumanToStd(String addr,String sep)
	{
//		System.out.println("addr:"+addr+" sep:"+sep);
		String result="";
		Pattern pattern;
		Matcher matcher;
		String regex;
		String regex1;
		String regex2;
		String regex3;
		String regex4;
		String prefix="";
		String postfix="";
		String ip="";
		
		if(addr.equals(""))
			return "";
		String a[];
		a=addr.split(sep);
		
		
	   
		long tmpvec[]=new long[1024];
		int len=0;
		for(int i=0;i<a.length;i++)
		{
			long start=0L,end=0L;
			a[i]=a[i].trim();
			
			
			//格式5.ip1|ip2的尾数|ip3的尾数(如83.10.15.24|36|46)。
			regex="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.)(\\d{1,3}) *(\\| *\\d{1,3})+";
			regex2=" *\\| *(\\d{1,3})";
			pattern = Pattern.compile(regex);
			String s=a[i];
			matcher = pattern.matcher(s);
			if (matcher.find())
			{
				prefix=matcher.group(1);
				ip=prefix+matcher.group(2);
				start=Common.StrAddr2value(ip);
				end=start;

				len=Common.sortMerge(tmpvec,len,start,end);

				s=s.substring(s.indexOf(ip)+ip.length());
				pattern = Pattern.compile(regex2);
				matcher = pattern.matcher(s);
				while (matcher.find())
				{
					postfix = matcher.group(1);
					ip = prefix + postfix;
					start = Common.StrAddr2value(ip);
					end = start;

					len = Common.sortMerge(tmpvec, len, start, end);
				}
				continue;
			}
			
			regex="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) */ *(\\d{1,2})";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(a[i]);
			if (matcher.find())
			{
				start=Common.StrAddr2value(matcher.group(1));
				end=start+Common.getAddrOffset(Integer.parseInt(matcher.group(2)));
				len=Common.sortMerge(tmpvec,len,start,end);
				continue;
			}
			
			regex="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) *- *(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(a[i]);
			if (matcher.find())
			{
				start=Common.StrAddr2value(matcher.group(1));
				end=Common.StrAddr2value(matcher.group(2));
				len=Common.sortMerge(tmpvec,len,start,end);
				continue;
			}
			
			regex="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.)(\\d{1,3}) *- *(\\d{1,3})";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(a[i]);
			if (matcher.find())
			{
				String startStr=matcher.group(1)+matcher.group(2);
				start=Common.StrAddr2value(startStr);
				String endStr=matcher.group(1)+matcher.group(3);
				end=Common.StrAddr2value(endStr);
				len=Common.sortMerge(tmpvec,len,start,end);
				continue;
			}
			//格式6.网段.*(如*.*.*.*或83.*.*.*或83.10.*.*或83.10.15.*)
			{
				regex1="\\*\\.\\*\\.\\*\\.\\*";
				regex2="(\\d{1,3})\\.\\*\\.\\*\\.\\*";
				regex3="(\\d{1,3}\\.\\d{1,3})\\.\\*\\.\\*";
				regex4="(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})\\.\\*";
			
				pattern = Pattern.compile(regex1);
				matcher = pattern.matcher(s);
				if (matcher.find())
				{
					ip = "0.0.0.0";
					start = Common.StrAddr2value(ip);
					ip = "255.255.255.255";
					end = Common.StrAddr2value(ip);

					len = Common.sortMerge(tmpvec, len, start, end);
					continue;
				}
				pattern = Pattern.compile(regex2);
				matcher = pattern.matcher(s);
				if (matcher.find())
				{
					ip=matcher.group(1)+".0.0.0";
					start = Common.StrAddr2value(ip);
					ip =matcher.group(1)+".255.255.255";
					end = Common.StrAddr2value(ip);

					len=Common.sortMerge(tmpvec,len,start,end);
					continue;
				}
				pattern = Pattern.compile(regex3);
				matcher = pattern.matcher(s);
				if (matcher.find())
				{
					ip=matcher.group(1)+".0.0";
					start = Common.StrAddr2value(ip);
					ip =matcher.group(1)+".255.255";
					end = Common.StrAddr2value(ip);

					len=Common.sortMerge(tmpvec,len,start,end);
					continue;
				}
				pattern = Pattern.compile(regex4);
				matcher = pattern.matcher(s);
				if (matcher.find())
				{
					ip=matcher.group(1)+".0";
					start = Common.StrAddr2value(ip);
					ip =matcher.group(1)+".255";
					end = Common.StrAddr2value(ip);

					len=Common.sortMerge(tmpvec,len,start,end);
					continue;
				}
			}
			regex="(\\d{1,3}\\.){3}\\d{1,3}";
			pattern = Pattern.compile(regex);
			matcher = pattern.matcher(a[i]);
			if (matcher.find())
			{

				start=Common.StrAddr2value(matcher.group());
				end=start;
				len=Common.sortMerge(tmpvec,len,start,end);
				continue;
			}

		}
		for(int i=0;i<len;i++)
		{
			result+=tmpvec[i];
			result+=Common.ComSep;
		}
//		System.out.println("addr:"+addr+" result:"+result);
		return result;
	}
	

	/**
	 * 将格式：212.231.9.1/32#118.2.66.9/32#
	 * 改为：212.231.9.1/32;<br>118.2.66.9/32
	 * @param srcAddr
	 * @return
	 */
	public static String comSep2Html(String srcAddr) 
	{
		if(srcAddr==null)
		{
			srcAddr="";
		}
		if(srcAddr.endsWith(Common.ComSep))
		{
			srcAddr=srcAddr.substring(0,srcAddr.lastIndexOf(Common.ComSep));
		}	
		
		srcAddr=srcAddr.replaceAll(Common.ComSep, Common.ExcelSep+"<br>");
		return srcAddr;
	}
	
	/**
	 * network:81.16.49.1/32或者81.16.49.2/31或者81.16.49.2/32
	 * @return
	 */
	public static String network2Value(String network)
	{
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			String addr=m.group(1);
			String mask=m.group(2);
			long ip1Value=Common.StrAddr2value(addr);
			int maskInt=Integer.parseInt(mask);
			long maskValue=(long) (Math.pow(2, 32)-Math.pow(2, (32-maskInt)));
			
			long firstAddrValue=ip1Value&maskValue;
			long lastAddrValue=(long) (firstAddrValue+Math.pow(2, (32-maskInt))-1);
			result=firstAddrValue+Common.ComSep+lastAddrValue+Common.ComSep;
		}
		return result;
	}

	/**
	 * 192.168.1.0/24 to 192.168.1.0 255.255.255.0
	 * @param network
	 * @return
	 */
	public static String network2network(String network)
	{
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			String addr=m.group(1);
			String mask=m.group(2);
			
			String dotmask=int2dotMask(mask);
			result=addr+" "+dotmask;
		}
		return result;
	}
	/**
	 *  192.168.1.0 255.255.255.0 to 192.168.1.0/24
	 * @param network
	 * @param dotmask
	 * @return
	 */
	public static String network2network(String network,String dotmask)
	{
		String result="";
	
		long addrvalue=StrAddr2value(network);
		long maskvalue=StrAddr2value(dotmask);
		long networkvalue=addrvalue & maskvalue;
		String networkValid=value2StrAddr(networkvalue);
		int masklen=dotMask2Int(dotmask);
		result=networkValid+"/"+masklen;
		
		return result;
	}
	/**
	 * 255.255.255.0 to 24
	 * @param dotmask
	 * @return
	 */
	public static int dotMask2Int(String dotmask) 
	{
		long value=StrAddr2value(dotmask);
		int count=0;
		for(int i=0;i<32;i++)
		{
			if(value/(long)Math.pow(2, 31-i)==1)
			{
				count++;
				value=value%(long)Math.pow(2, 31-i);
			}
			else
			{
				return count;
			}
		}
		return count;
	}

	/**
	 * 查看addr 是否满足 掩码大于等于24的地址的应用位是否与appbit一致，如果一致就返回
	 * @param addr  格式： 83.10.15.0/24
	 * @param appbit
	 * @return
	 */
	public static boolean isAddrWithAppBit(String addr, String appbit) 
	{
		String addrtmp=addr.replaceAll("\"", "");
		if(Common.isNetworkStdFormat(addrtmp))
		{
			
			
			int maskLen=Common.getMaskLenByNetworkWithMask(addrtmp);
			if(maskLen<24)
			{
				return false;
			}
			
			String networkThird=Common.getThirdOctByNetworkWithMask(addrtmp);
			int networkThirdint=Integer.parseInt(networkThird);
			int appbitInt=Integer.parseInt(appbit);
			if(networkThirdint%16==appbitInt)	
			{
				return true;
			}
		}
		
		
		return false;
	}

	private static boolean isNetworkStdFormat(String network) 
	{
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			
			

			return true;
		}
		return false;
	}

	/**
	 *  从 83.10.15.3/28  中获取第三个数:15
	 * @param network 83.10.15.3/28 
	 * @return
	 */
	private static String getThirdOctByNetworkWithMask(String network) 
	{
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.(\\d{1,3})\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			
			String thirdOct=m.group(2);

			return thirdOct;
		}
		return result;
	}

	/**
	 * 获取子网掩码长度 
	 * @param network  83.10.15.3/28 
	 * @return
	 */
	private static int getMaskLenByNetworkWithMask(String network) 
	{
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			
			String maskLen=m.group(2);

			return Integer.parseInt(maskLen);
		}
		return -1;
	}

	/**
	 * 把 122.8.79.192/32转为host 122.8.79.192
	 * 把 122.8.79.0/24转为122.8.79.0 255.255.255.0
	 * @param string
	 * @return
	 */
	public static String convertObjGrpItemFromNetwork2Real(String network) {
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			String addr=m.group(1);
			String mask=m.group(2);
//			long ip1Value=Common.StrAddr2value(addr);
			int maskInt=Integer.parseInt(mask);
			if(maskInt==32)
			{
				result="host "+addr;
			}
			else
			{
				String maskStr=int2dotMask(mask);
				result=addr+" "+maskStr;
			}
			
		}
		return result;
	}

	/**
	 * 
	 * @param ip
	 * @param network  format like 192.168.0.0/16
	 */
	public static boolean isIpInNetwork(String ip, String network) 
	{
		long ipValue=Common.StrAddr2value(ip);
		String result="";
		String regex;
		Pattern p;
		Matcher m;
		
		regex = "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})/(\\d{1,2})";
		p = Pattern.compile(regex);
		m = p.matcher(network);
		if (m.find()) 
		{
			String addr=m.group(1);
			String mask=m.group(2);
			long ip1Value=Common.StrAddr2value(addr);
			int maskInt=Integer.parseInt(mask);
			long maskValue=(long) (Math.pow(2, 32)-Math.pow(2, (32-maskInt)));
			
			long firstAddrValue=ip1Value&maskValue;
			long lastAddrValue=(long) (firstAddrValue+Math.pow(2, (32-maskInt))-1);
			
			if(ipValue>=firstAddrValue && ipValue<=lastAddrValue)
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	/**
	 * ip是否是在ip（包含network）的列表中
	 * 如果networklist为空，认为是任意
	 * @param ip
	 *  @param networklist like:1.1.1.1;192.168.1.0/24
	 * @return
	 */
	public static boolean isIpInList(String ip,Vector<String> networklist) 
	{
		if(networklist.isEmpty())
		{
			return true;
		}
		
		for(String s:networklist)
		{
			if(s.contains("/"))
			{
				if( Common.isIpInNetwork(ip,s))
				{
					return true;
				}
					
			}
			else
			{
				if(s.equals(ip))
				{
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * ip不在networklist（包含network）的列表中
	 * 如果networklist为空，认为是“不在列表中”
	 * @param ip
	 *  @param networklist like:1.1.1.1;192.168.1.0/24
	 * @return
	 */
	public static boolean isIpNotInList(String ip,Vector<String> networklist) 
	{
		if(networklist.isEmpty())
		{
			return true;
		}
		
		for(String s:networklist)
		{
			if(s.contains("/"))
			{
				if( Common.isIpInNetwork(ip,s))
				{
					return false;
				}
					
			}
			else
			{
				if(s.equals(ip))
				{
					return false;
				}
			}
		}
		return true;
	}
//	/**
//	 * 将汉字转为拼音
//	 * @param src
//	 * @return
//	 */
//	public static String getPinyin(String src)
//	{
//		if(src!=null && !src.trim().equalsIgnoreCase(""))
//		{
//			 char[] srcChar ;
//			 srcChar=src.toCharArray();
//			 //汉语拼音格式输出类
//			 HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
//	
//			 //输出设置，大小写，音标方式等
//			 hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE); 
//			 hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//			 hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//	   
//			 String[][] temp = new String[src.length()][];
//			 for(int i=0;i<srcChar.length;i++){
//				 char c = srcChar[i];
//				 //是中文或者a-z或者A-Z转换拼音(我的需求，是保留中文或者a-z或者A-Z)
//				 if(String.valueOf(c).matches("[\\u4E00-\\u9FA5]+")){
//					 try{
//						 temp[i] = PinyinHelper.toHanyuPinyinStringArray(srcChar[i], hanYuPinOutputFormat);
//					 }catch(BadHanyuPinyinOutputFormatCombination e) {
//						 e.printStackTrace();
//					 }
//				 }else if(((int)c>=65 && (int)c<=90) || ((int)c>=97 && (int)c<=122)){
//					 temp[i] = new String[]{String.valueOf(srcChar[i])};
//				 }else{
//					 temp[i] = new String[]{""};
//				 }
//			 }
//			 StringBuilder str = new StringBuilder();
//			 for(int i =0;i<temp.length;i++){
//				 str.append(temp[i][0]);
//			 }
//			 return str.toString();
//		 }
//		 return "";
//	}

	/**
	 * 如果desc包含includeDescVec中的关键字，则为true
	 * 如果includeDescVec为空，返回true
	 * @param desc
	 * @param includeDescVec
	 * @return
	 */
	public static boolean isDescInList(String desc, Vector<String> includeDescVec) {
		if(includeDescVec.isEmpty())
		{
			return true;
		}
		
		for(String s:includeDescVec)
		{
			if(desc.contains(s))
			{
				
				return true;
				
			}
	
		}
		return false;
	}

	/**
	 * 如果desc包含includeDescArray中的关键字，则为true
	 * 如果includeDescArray为空，返回true
	 * @param desc
	 * @param includeDescArray
     * @return
     */
	public static boolean isDescInList(String desc, JSONArray includeDescArray) {
		if(includeDescArray.length()==0)
		{
			return true;
		}

		for(int i=0;i<includeDescArray.length();i++)
		{
			String s= null;
			try {
				s = includeDescArray.getString(i);
				if(desc.contains(s))
				{
					return true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	/**
	 * 如果desc包含excludeDescArray中的关键字，则为true
	 * 如果excludeDescArray为空，返回true
	 * @param desc
	 * @param excludeDescArray
     * @return
     */
	public static boolean isDescNotInList(String desc, JSONArray excludeDescArray) {
		if(excludeDescArray.length()==0)
		{
			return true;
		}

		for(int i=0;i<excludeDescArray.length();i++)
		{
			String s= null;
			try {
				s = excludeDescArray.getString(i);
				if(desc.contains(s))
				{
					return false;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return true;
			}
		}

		return true;
	}

	public static boolean isDescNotInList(String desc, Vector<String> excludeDescVec) {
		if(excludeDescVec.isEmpty())
		{
			return true;
		}

		for(String s:excludeDescVec)
		{
			if(desc.contains(s))
			{

				return false;

			}

		}
		return true;
	}
 
	
}
