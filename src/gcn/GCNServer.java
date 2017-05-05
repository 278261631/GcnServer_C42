package gcn;


import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.List;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

import acp.ThreadAcpControl;
import util.TestSendEmail;
import util.XML2File;

public class GCNServer extends ServerSocket {
    private static int SERVER_PORT = 5348;
	private static GCNServer gcnServer;

    public GCNServer(int SERVER_PORT_param) throws IOException  {
        super(SERVER_PORT_param);

        try {
            while (true) {
                Socket socket = accept();
                new CreateServerThread(socket);//当有请求时，启一个线程处理
            }
        } catch (IOException e) {
        	XML2File.writeToLog(e.getMessage());
        } finally {
            close();
        }
    }

    //线程类
    class CreateServerThread extends Thread {
        private Socket client;
        private BufferedReader bufferedReader;
//        private PrintWriter printWriter;

        public CreateServerThread(Socket s) throws IOException {
            client = s;

            bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
//            printWriter = new PrintWriter(client.getOutputStream(), true);
            System.out.println("Client(" + getName() +s.getInetAddress()+" : "
            		+s.getPort()+ ") come in...");
            
            start();
        }

        public void run() {
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
        	String outRootDir=config.getString("outXmlRootDir",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "),"outDir").getPath());
        	SERVER_PORT=config.getInt("SERVER_PORT" , 5348);
        	System.out.println("SERVER_PORT : " +SERVER_PORT);
        	String tempFilePath=config.getString("tempFilePath",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "temp.xml").getPath());
        	boolean isUseAcpControl = config.getBoolean("isUseAcpControl", false);
        	String acpPlanPath = config.getString("acpPlanPath");
        	String python27Path = config.getString("python27Path");
        	String acpUrl = config.getString("acpUrl");
        	String acpUser = config.getString("acpUser");
        	String acpPass = config.getString("acpPass");
        	String filter = config.getString("filter","");
        	String HmtLimitAngle = config.getString("HmtLimitAngle","30");
        	String HMTlastPlanDir = config.getString("HMTlastPlanDir","");
//        	String[] emails=config.getStringArray("emails");
        	String emailString=config.getString("emails");
        	String ftp_ip=config.getString("ftp_ip");
        	String ftp_name=config.getString("ftp_name");
        	String ftp_pass=config.getString("ftp_pass");
        	String ftp_port=config.getString("ftp_port");
        	String msg_name=config.getString("msg_name");
        	String msg_url=config.getString("msg_url");
        	String[] emails=emailString.split(",");
        	String resultString=TestSendEmail.emailCheck(emails);
        	if (null!=resultString && resultString.length()>0) {
				System.out.println(resultString);
				return;
			}
//        	String errorLogFile=config.getString("errorLogFile",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), longDateFormat.format(new Date())+".log").getPath());
      	    System.out.println("tempFilePath : "  + tempFilePath);  
    	    System.out.println("outXmlRootDir : "  + outRootDir);  
    	    System.out.println("isUseAcpControl : "+isUseAcpControl);
    	    System.out.println("acpPlanPath : "+acpPlanPath);
    	    System.out.println("HMTlastPlanDir : "+HMTlastPlanDir);
    	    System.out.println("python27Path : "+python27Path);
    	    System.out.println("acpUrl : "+acpUrl);
    	    System.out.println("acpUser : "+acpUser);
    	    System.out.println("acpPass : "+acpPass);
    	    System.out.println("filter : "+filter);
    	    System.out.println("HmtLimitAngle : "+HmtLimitAngle);
    	    System.out.println("ftp to : "+ftp_ip+"   "+ftp_port+"   "+ftp_name+"   "+ftp_pass);
    	    System.out.println("message to : "+msg_url+"   "+msg_name);
    		File dirFile=new File(outRootDir);
    		if (!dirFile.exists()) {
    			dirFile.mkdirs();
    		}
        	
        	
            try {
                String line = "";
                	
                String xmlOldString="";
                while ("".equals("")) {
                    	line = bufferedReader.readLine();
//                    	System.out.println(line);
                    	//save xmlFile first , Then Copy to other place
						xmlOldString=XML2File.xmlStringToFile(xmlOldString, line, tempFilePath,outRootDir,emails, ftp_pass, ftp_name, Integer.parseInt(ftp_port), ftp_ip,msg_url,msg_name);
                }
                System.out.println("Client(" + getName() + ") exit!");
//                printWriter.close();
                bufferedReader.close();
                client.close();
            } catch (IOException e) {
            	e.printStackTrace();
            	XML2File.writeToLog(e.getMessage());
            } catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }

    public static Object lock = new Object();
    public static void main(String[] args) throws IOException {
    	SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    	System.out.println("Server V20161007.1  Start  : "+longDateFormat.format(new Date()));
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
    	SERVER_PORT=config.getInt("SERVER_PORT" , 5348);
//    	ThreadAcpControl a = new ThreadAcpControl(lock);
//    	a.start();
    	//TODO  test FTP
        setGcnServer(new GCNServer(SERVER_PORT));
        
        //run in windows command demo :
//        # ####copy *.jar lib/*.jar files to java/jre/lib/ext 
//       and run  java GCNServer 
        
        
        
    }

	public static GCNServer getGcnServer() {
		return gcnServer;
	}

	public static void setGcnServer(GCNServer gcnServer) {
		GCNServer.gcnServer = gcnServer;
	}
}