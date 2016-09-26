package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;



public class TestPython2 {
	public static void main(String[] args) {
		String scriptPath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "python/test.py").getPath();
		Process process;
		try {
			String param1="37.5"; //ra in Degrees
			String param2="89.25"; //dec
			process = Runtime.getRuntime().exec("D:\\FileZilla Server\\FTP_Root\\Python27\\python.exe "+ scriptPath +" " + param1+" "+param2 );
			InputStreamReader ir = new InputStreamReader(process.getInputStream());  
			LineNumberReader input = new LineNumberReader(ir);  
			String line;  
			while((line = input.readLine()) != null)  
				System.out.println(line);  
			input.close();  
//			ir.close();  
		
		} catch (IOException e) {
		
			e.printStackTrace();
		}  
		

		
	}
}
