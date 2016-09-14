
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration2.AbstractConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class GCNServer extends ServerSocket {
    private static final int SERVER_PORT = 5348;
	private static GCNServer gcnServer;

    public GCNServer() throws IOException {
        super(SERVER_PORT);

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
        	String tempFilePath=config.getString("tempFilePath",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "temp.xml").getPath());
//        	String[] emails=config.getStringArray("emails");//"278261631@qq.com"
        	String emailString=config.getString("emails");//"278261631@qq.com"
        	String[] emails=emailString.split(",");
        	String resultString=TestSendEmail.emailCheck(emails);
        	if (null!=resultString && resultString.length()>0) {
				System.out.println(resultString);
				return;
			}
//        	String errorLogFile=config.getString("errorLogFile",new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), longDateFormat.format(new Date())+".log").getPath());
      	    System.out.println("tempFilePath : "  + tempFilePath);  
    	    System.out.println("outXmlRootDir : "  + outRootDir);  
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
						xmlOldString=XML2File.xmlStringToFile(xmlOldString, line, tempFilePath,outRootDir,emails);
                }
                System.out.println("Client(" + getName() + ") exit!");
//                printWriter.close();
                bufferedReader.close();
                client.close();
            } catch (IOException e) {
            	e.printStackTrace();
            	XML2File.writeToLog(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
    	SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    	System.out.println("Server V20160914.1  Start  : "+longDateFormat.format(new Date()));
        gcnServer = new GCNServer();
        
        //run in windows command demo :
//        # ####copy *.jar lib/*.jar files to java/jre/lib/ext 
//       and run  java GCNServer 
        
        
        
    }
}