package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;


//import org.python.util.PythonInterpreter;

//import jodd.datetime.JulianDateStamp;

public class Ha2HcJava {

	public static void main(String[] args) throws IOException, InterruptedException {
	
		Calendar caNow=Calendar.getInstance();

		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd : HH mm ss");
		System.out.println("Today is:"+format.format(caNow.getTime()));
		double raInDegrees=37.5;
		double decIndegrees=89.25;
		double raInRadians=Math.toRadians(raInDegrees );
		double decInRadians=Math.toRadians(decIndegrees);
		System.out.println("RA ran   "+ raInRadians + "Dec ran   " + decInRadians);
		gps_to_lst(caNow,0);
		String pythonPath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "temp.xml").getPath();
//		String scriptPath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "python/test.py").getPath();
		String scriptPath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "python/celestial.py").getPath();
		String libPath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "python/").getPath();
//		String libPathInPython= " D:\\FileZilla Server\\FTP_Root\\Python27\\Lib\\site-packages";
		String libPathInPython= " D:\\FileZilla Server\\FTP_Root\\Python27\\Lib";
		System.out.println(pythonPath);
		System.out.println(scriptPath);
//		 Process proc = Runtime.getRuntime().exec("\"" + pythonPath + "\" \"" +scriptPath + "\"");  
//		 Process proc = Runtime.getRuntime().exec("python test.py");  
//		 Process proc = Runtime.getRuntime().("python test.py");  
		
		 Properties props = new Properties();
//	        props.put("python.home", libPath);
	        props.put("python.console.encoding", "UTF-8");
//	        props.setProperty("python.path", libPath);
//	        D:\FileZilla Server\FTP_Root\Python27\Lib\site-packages
	        props.setProperty("python.path", libPathInPython);
	        
	        props.put("python.security.respectJavaAccessibility", "false");
	        
	        props.put("python.import.site", "false");

	        Properties preprops = System.getProperties();

//	        PythonInterpreter.initialize(preprops, props, new String[0]);
//	        PythonInterpreter interpreter = new PythonInterpreter();  
//	        interpreter.exec("import sys");
//	        interpreter.exec("import sys.path");
//	        interpreter.exec("print sys.path");
//	        interpreter.execfile(scriptPath );
//	        
//	        interpreter.close();
		  
//         BufferedReader in = new BufferedReader(new  
//                 InputStreamReader(proc.getInputStream()));  
//         String line;  
//         while ((line = in.readLine()) != null) {  
//             System.out.println(line);  
//         }  
//         in.close();  
//         proc.waitFor();  
//         System.out.println("end");  

		
		
		
//		localtime = time.asctime( time.localtime(time.time()) )
//				print "本地时间为 :", localtime
//				print time.strftime("%Y-%m-%d %H:%M:%S", time.localtime()) 
//				# hor = tranFormer.equatorial_to_horizontal(latitude, longitude, timestamp, right_ascension, declination)
//				raInDegrees=37.5
//				decIndegrees=89.25
//				# raInDegrees=172.5
//				# decIndegrees=3.66
//				raInRadians=math.radians(raInDegrees);
//				decInRadians=math.radians(decIndegrees);
//				print 'Ra In Radians    ' +repr(raInRadians)
//				print 'Dec In Radians    '+ repr(decInRadians)
//				# hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), raInDegrees,decIndegrees) #sun
//				hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), raInRadians,decInRadians) #sun
//				# hor = tranFormer.equatorial_to_horizontal(43.8, 87.5, time.time(), 285.6, 18) #moon
//
//
//				print hor
//				print numpy.rad2deg(hor[0])
//				print numpy.rad2deg(hor[1]) 
//				print angles.radians_to_hours(hor[0])
//				print angles.radians_to_hours(hor[1])
	}
	
	static  Calendar gps_to_lst(Calendar calendar, double longitude){
//	    """Convert a GPS timestamp to lst

//	    :param timestamp: GPS timestamp in seconds.
//	    :param longitude: location in degrees, east positive.
//	    :return: decimal hours in LST.

//	    """
//	    utc_timestamp = gps_to_utc(timestamp)
//		2、取得时间偏移量：
		int zoneOffset = calendar.get(java.util.Calendar.ZONE_OFFSET);
//		3、取得夏令时差：
		int dstOffset = calendar.get(java.util.Calendar.DST_OFFSET);
		calendar.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		JulianDateStamp jds=new JulianDateStamp(calendar.getTimeInMillis());
		System.out.println(jds);
		
//	    		
//	    utc = datetime.datetime.utcfromtimestamp(utc_timestamp)
//	    return utc_to_lst(utc, longitude)
		return null;
	}
}
