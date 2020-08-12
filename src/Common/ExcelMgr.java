package Common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONObject;

import Config.ResourceConfig;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelMgr {

//	String app[]={"appname","appabbr"};
//	String field[]={"env","domainsubfix","area","ang","desc","vip","vdomainname","service_provide","service_need","cpuCount",
//			"memerySize","diskApp","diskTotal","pvFlag","description","ipAddr","hostname","domain","userName","password",
//			"vmHostname","operDesc"};
	public static void main(String arg[])
	{
		String file="/Users/yuehongchao/Documents/workspace/Automation/资源管理-20161215.xls";
		ExcelMgr ep=new ExcelMgr();
//		ep.parseFile(file);
	}
	

	public JSONArray importFromFile(String file, String[] sheetname, String[] field,int startRow) 
	{
		JSONArray appArray=new JSONArray();
		try
		{			
			InputStream is = new FileInputStream(file);
			
			Sheet rs;
			Workbook rwb = Workbook.getWorkbook(is);
			int sheetNum=rwb.getNumberOfSheets();
			for(String shname:sheetname)
			{
				for(int k=0;k<sheetNum;k++)
				{
					rs = rwb.getSheet(k);
					//String strc00 = c00.getContents();
					
					if(shname.equals(rs.getName().trim()))
					{
						int rownum=rs.getRows();
						int colnum=rs.getColumns();
						
						for(int i=startRow;i<rownum;i++)
						{
							JSONObject obj=new JSONObject();
							appArray.put(obj);
							obj.put(ResourceConfig.APP_NAME, shname);
							
							for(int j=0;j<field.length;j++)
							{
								
								if("".equals(field[j]))
								{
									continue;
								}
								String s=rs.getCell(j, i).getContents().trim();
								obj.put(field[j], s);
//								if("企业服务总线".equals(shname))
//								{
//									System.out.print(s+"\t");	
//								}

							}					
						}
					}
				}
			}
			
			

			rwb.close();
			is.close();

//			System.out.println(appArray.toString());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return appArray;
	}
}
