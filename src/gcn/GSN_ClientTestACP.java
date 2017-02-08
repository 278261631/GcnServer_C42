package gcn;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Stream;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import util.XML2File;

public class GSN_ClientTestACP {
    public static void main(String[] args) {
        try {

        	Configurations configs = new Configurations();
        	Configuration config = null;
        	
        	try
        	{
        	    config = configs.properties(new File("usergui.properties"));
        	}
        	catch (ConfigurationException cex)
        	{
        	    // Something went wrong
        		XML2File.writeToLog(cex.getMessage());
        		cex.printStackTrace();
        	}
//        	SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyyMMdd");
//        	SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        	((AbstractConfiguration) config).setListDelimiterHandler(new DefaultListDelimiterHandler(','));
         	int SERVER_PORT=config.getInt("SERVER_PORT" , 5348);
        	System.out.println("SERVER_PORT : " +SERVER_PORT);
            Socket socket = new Socket("127.0.0.1", 5348);
           
            socket.setSoTimeout(600000);

         
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
           


            //这里要多加测试： 生成普通plan  BAT plan  BAT 不同方位，不同高度 的plan
            
            String testFilePath1=config.getString("testFilePath1",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "testFilePath1.xml").getPath());
            String testFilePath2=config.getString("testFilePath2",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "testFilePath2.xml").getPath());
            String testFilePath3=config.getString("testFilePath3",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "testFilePath3.xml").getPath());
            String testFilePath4=config.getString("testFilePath4",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "testFilePath4.xml").getPath());
            String testFilePath5=config.getString("testFilePath5",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "testFilePath5.xml").getPath());
            String testFilePath6=config.getString("testFilePath6",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "testFilePath6.xml").getPath());
            List<String> filePathList=new ArrayList<String>();
            filePathList.add(testFilePath1);
            filePathList.add(testFilePath2);
            filePathList.add(testFilePath3);
            filePathList.add(testFilePath4);
            filePathList.add(testFilePath5);
            filePathList.add(testFilePath6);
            while (true) {
            	
            	
            	BufferedReader sysBuff = new BufferedReader(new InputStreamReader(System.in));
//            	printWriter.println(sysBuff.readLine());
            	String s =sysBuff.readLine();
            	String toPrint=XML2File.readLogToString(filePathList.get(Integer.parseInt(s)-1));
            	printWriter.print(toPrint);
//            	printWriter.println(testXml.get(Integer.parseInt(s)-1));
            	// 刷新输出流，使Server马上收到该字符串
            	printWriter.flush();
//            	String result = bufferedReader.readLine()+"\r\n";
//            	if (result.getBytes()[0]==0) {
////            	bufferedReader.skip(4); //服务器发来的每个包消息头部有字符   要查一下原因
//					result=result.substring(result.indexOf('<'));
//				}
//            	XML2File.writeLog(result, "D:\\GCN_log.txt");
//            	System.out.println("Server say : " + result);
//            	System.out.println("Server say : " + BinaryToHexString(result.getBytes()));

            	if (false) {
					break;
				}
			}

            /** 关闭Socket*/
            printWriter.close();
//            bufferedReader.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }
    public static String BinaryToHexString(byte[] bytes){
    	
    	String hexStr =  "0123456789ABCDEF";
    	String result = "";  
    	String hex = "";  
    	for(int i=0;i<bytes.length;i++){  
    		//字节高4位  
    		hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
    		//字节低4位  
    		hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
    		result +=hex+" ";  //这里可以去掉空格，或者添加0x标识符。
    	}  
    	return result;  
    }
}