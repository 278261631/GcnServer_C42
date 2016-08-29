

import java.io.*;
import java.text.SimpleDateFormat;
//import java.text.SimpleDateFormat;
import java.util.*;

public class XML2File {
	
	//完成写入就返回"" 否则返回拼接之后xmlString
	public static String xmlStringToFile(String xmlOldString,String xmlNewString,String filePath, String saveRootPath, String[] emails) throws IOException{
		String startXmlString="<?xml version = '1.0' encoding = 'UTF-8'?>";
		String endXmlString="</voe:VOEvent>";

		if (xmlNewString.endsWith(endXmlString)) {
			if (xmlOldString.startsWith(startXmlString)) {
				//if is the end of XML,save to temp 
				writeLogCoverExist(xmlOldString+"\r\n"+xmlNewString+"\r\n",filePath);				
				//check message type, copy to dest Path if needed
				ReadXMLTest.saveUsefulMessageType(filePath, saveRootPath,emails);
				
				//else 
			}else {
				System.out.println("未完成的xml 1 " +xmlOldString+"\r\n"+xmlNewString);
				writeToLog("未完成的xml 1 " +xmlOldString+"\r\n"+xmlNewString);
			}
			return "";
		}else if(xmlNewString.startsWith(startXmlString)){
			if (xmlOldString.length()>0) {
				System.out.println("未完成的xml 2 " +xmlOldString+"\r\n"+xmlNewString);
				writeToLog("未完成的xml 2 " +xmlOldString+"\r\n"+xmlNewString);
			}else {
				return xmlNewString;
			}
		}else if(xmlNewString.length()>0) {
			if(xmlOldString.startsWith(startXmlString)){
				return xmlOldString+"\r\n"+xmlNewString;
			}else {
				System.out.println("未完成的xml 3 " +xmlOldString+"\r\n"+xmlNewString);
				writeToLog("未完成的xml 3 " +xmlOldString+"\r\n"+xmlNewString);
				return "";
			}
		}
		return "";
		
	}
    public static void writeToLog(String str)
    {
        try
        {
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        File file=new File(new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "),
        		shortDateFormat.format(new Date())+".log").getPath());
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file,true); //
        str="\r\n"+longDateFormat.format(new Date())+"\r\n"+str;
        out.write(str.getBytes("utf-8"));
        out.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
    }    
    public static void writeLogCoverExist(String str,String filePath)
    {
    	try
    	{
    		File file=new File(filePath);
    		if(!file.exists())
    			file.createNewFile();
    		FileOutputStream out=new FileOutputStream(file,false); //
    		out.write(str.getBytes("utf-8"));
    		out.close();
    	}
    	catch(IOException ex)
    	{
    		System.out.println(ex.getStackTrace());
    	}
    }    
    public static List<String> readLog(String path)
    {
    	List<String> stringList= new ArrayList<String>();  
//        StringBuffer sb=new StringBuffer();
        String tempstr=null;
        try
        {
//            String path="/root/test/testfilelog.log";
            File file=new File(path);
            if(!file.exists())
                throw new FileNotFoundException();            
//            BufferedReader br=new BufferedReader(new FileReader(file));            
//            while((tempstr=br.readLine())!=null)
//                sb.append(tempstr);    
           
            FileInputStream fis=new FileInputStream(file);
            BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            
            while((tempstr=br.readLine())!=null){
            	stringList.add(tempstr);
            }
            br.close();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getStackTrace());
        }
        
        return stringList;
    }
    public static String readLogToString(String path)
    {
    	String stringFromXML= "";  
    	String tempstr=null;
    	try
    	{
    		File file=new File(path);
    		if(!file.exists()){
    			throw new FileNotFoundException();            
    		}
    		FileInputStream fis=new FileInputStream(file);
    		BufferedReader br=new BufferedReader(new InputStreamReader(fis,"UTF-8"));
    		
    		while((tempstr=br.readLine())!=null){
    			stringFromXML+=tempstr+"\r\n";
    		}
    		br.close();
    	}
    	catch(IOException ex)
    	{
    		System.out.println(ex.getStackTrace());
    	}
    	
    	return stringFromXML;
    }
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//        writeLog("this is a test log");
//        writeLog("");
//        System.out.println(readLog());
//    }

}