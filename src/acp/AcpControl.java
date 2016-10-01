package acp;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class AcpControl {


	public static  String runPlan(String filePath) {
		PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	        	System.out.println( getNowTimeString()+ "准备启动 ");
	            URL realUrl = new URL("http://192.168.1.130:81/ac/aacqplan.asp");
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
//	            out.print("plan=C:\\Users\\Public\\Documents\\ACP Web Data\\Doc Root\\plans\\mayong\\test2.txt");
//	            out.print("plan=C:/Users/Public/Documents/ACP Web Data/Doc Root/plans/mayong/test2.txt");
	            out.print("plan="+filePath);
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
	

	

	static SimpleDateFormat longDateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static String getNowTimeString (){
		return longDateFormatFile.format(new Date()) +" :\t\t";
	}
	public static String stopRunPlan() {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			System.out.println( getNowTimeString()+ "尝试停止 ");
//			http://192.168.1.130:81/ac/astopscript.asp
			URL realUrl = new URL("http://192.168.1.130:81/ac/astopscript.asp");
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
			out.print("Command=StopScript");
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
		return  result;
	}    
	
	


}
