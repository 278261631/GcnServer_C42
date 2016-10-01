package acp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class AcpStatusUpdater {


	private static  String updateSystemStatus() {
		PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	        	System.out.println( AcpControl.getNowTimeString()+ "更新系统状态 ");
	            URL realUrl = new URL("http://192.168.1.130:81/ac/asystemstatus.asp");
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            
	            
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            String input = "mayong" + ":" + "mayong";
	            Base64 base64 = new Base64();
	            String encoding =  base64.encodeToString(input.getBytes());
	            conn.setRequestProperty("Authorization", "Basic " + encoding);
	            conn.setRequestProperty("connection", "Keep-Alive");
//	            conn.setRequestProperty("user-agent",
//	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
//	            out.print("plan="+filePath);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	       return result;
	        
	        
	} 
	
	public static Map<String,String> acpResultToMap(String resultString){
		Map<String, String> resultMap= new HashMap<String,String>();
		if (resultString!=null && resultString.indexOf(";")>0) {
			resultString=resultString.replace("_s(", "");
			resultString=resultString.replace(")", "");
		 String []	resultStringArray = resultString.split(";");
		 for (String string : resultStringArray) {
			 String stringItem=string.replace("\'", "");
			 String [] itemArray=stringItem.split(",");
			 if (itemArray.length>1) {
				 resultMap.put(itemArray[0], itemArray[1]);
			}
		}
		}
		
		return resultMap;
	}
	
	
	public static  Map<String,String> getSystemStatus() {
		String resultString = AcpStatusUpdater.updateSystemStatus();
//		 System.out.println( resultString);
		Map<String, String> resultMap = AcpStatusUpdater.acpResultToMap(resultString);
		return resultMap;
//		System.out.println(resultMap.get("sm_obsStat"));
//		System.out.println(resultMap.get("sm_obsStat"));
//		System.out.println(resultMap.get("sm_plnTitle"));
//		try {
//			System.out.println(URLDecoder.decode(resultMap.get("sm_utc"),"UTF-8"));
//			System.out.println(URLDecoder.decode(resultString,"UTF-8"));
//		} catch (UnsupportedEncodingException e) {
			//解码错误
//			e.printStackTrace();
//		}
	}   
	

	

}
