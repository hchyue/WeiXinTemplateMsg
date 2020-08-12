package MsgSender;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import probe.TaskICMP;

public class ConfigFile {

	private final static String FILENAME= "config";

	public static void update(String name, String value) {
		String oldFileName = FILENAME;
		String tmpFileName = FILENAME + ".tmp";

		String regex1 = name + "\\s*=\\s*(\\S+)";
		Pattern p1 = Pattern.compile(regex1);
		Matcher m1;

		try {
			BufferedReader br = new BufferedReader(new FileReader(oldFileName));
			BufferedWriter bw = new BufferedWriter(new FileWriter(tmpFileName));

			String line;
			while ((line = br.readLine()) != null) {
				m1 = p1.matcher(line);
				if (m1.find()) {
					line = line.replace(m1.group(1), value);
				}

				bw.write(line + "\n");
			}

			if (br != null)
				br.close();

			if (bw != null)
				bw.close();

			// Once everything is complete, delete old file..
			File oldFile = new File(oldFileName);
			oldFile.delete();
			// And rename tmp file's name to old file name
			File newFile = new File(tmpFileName);
			newFile.renameTo(oldFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getProperty(String name) {

		String value = "";
		String oldFileName = FILENAME;

		String regex1 = name + "\\s*=(.*)";
		Pattern p1 = Pattern.compile(regex1);
		Matcher m1;

		try {
			BufferedReader br = new BufferedReader(new FileReader(oldFileName));

			String line;
			while ((line = br.readLine()) != null) {
				m1 = p1.matcher(line);
				if (m1.find()) {
					value = m1.group(1).trim();
				}
			}

			if (br != null)
				br.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static void getVectorProperty(String key, Vector<String> valueVec) {
		String icmp_target=ConfigFile.getProperty(key);
		if(icmp_target==null || "".equals(icmp_target.trim()))
		{
			return;
		}
		
		String ip[]=icmp_target.trim().split(";");
		for(String s:ip)
		{
			valueVec.add(s.trim());
		}
	}

	public static void getHashMapProperty(String key, HashMap<String, String> map) {
		String icmp_target=ConfigFile.getProperty(key);
		if(icmp_target==null || "".equals(icmp_target.trim()))
		{
			return;
		}
		
		String ip[]=icmp_target.trim().split(";");
		for(String s:ip)
		{
			s=s.trim();
			map.put(s,s);
		}
		
	}

}
