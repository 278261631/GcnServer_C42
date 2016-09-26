package gcn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
//import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
//import java.util.stream.Stream;

public class GSN_ClientTest {
    public static void main(String[] args) {
        try {
            /** 创建Socket*/
            // 创建一个流套接字并将其连接到指定 IP 地址的指定端口号(本处是本机)
//            SERVERS                             VOEVENT_VERSION
//            209.208.78.170 (Atlantic.net)       1.1
//            50.116.49.68   (Linode.com)         1.1
//            68.169.57.253  (eApps.com)          2.0
//            45.58.43.186   (Atlantic.net)       2.0  (Not yet providing LVC)
//        	prot 8099
//            Socket socket = new Socket("119.78.162.23", 5348);
            Socket socket = new Socket("127.0.0.1", 5348);
            // 60s超时
            socket.setSoTimeout(600000);

            /** 发送客户端准备传输的信息 */
            // 由Socket对象得到输出流，并构造PrintWriter对象
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            // 将输入读入的字符串输出到Server

            /** 用于获取服务端传输来的信息 */
            // 由Socket对象得到输入流，并构造相应的BufferedReader对象
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // 输入读入一字符串
            List<String> testXml=new ArrayList<String>();
            testXml.add("<?xml version = '1.0' encoding = 'UTF-8'?>");
            testXml.add("<voe:VOEvent ivorn=\"ivo://nasa.gsfc.gcn/GCN#imalive_2016-05-04T17:46:12.00_000013-050\"  role=\"observation\" version=\"1.1\" xmlns:voe=\"http://www.ivoa.net/xml/VOEvent/v2.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.ivoa.net/xml/VOEvent/v2.0  http://www.ivoa.net/xml/VOEvent/VOEvent-v2.0.xsd\" >");
            testXml.add("<Description>");
            testXml.add("</Description> <What>     <Param name=\"Packet_Type\"  value=\"3\" /> </What>");
            testXml.add("</voe:VOEvent>");
            while (true) {
            	BufferedReader sysBuff = new BufferedReader(new InputStreamReader(System.in));
//            	printWriter.println(sysBuff.readLine());
            	String s =sysBuff.readLine();
            	printWriter.println(testXml.get(Integer.parseInt(s)-1));
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