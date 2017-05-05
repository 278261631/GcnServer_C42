package util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;


import acp.AcpControl;
import acp.AcpStatusUpdater;
import acp.GenerateAcpPlanString;
import acp.MyAcpCommandList;
import gcn.GCNServer;
import gcn.GcnMailNoticeConverter;
import net.sf.json.JSONObject;



public class ReadXMLTest {

	public static void main(String[] args) {
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<XMLConfiguration> builder =
		    new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
		    .configure(params.xml()
		        .setFileName("GNSTestFile.xml"));
//		        .setValidating(true));
		XMLConfiguration config=null;
		try {
			config= builder.getConfiguration();
			String filePath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "abc.xml").getPath();
			System.out.println(filePath);
		} catch (Exception e) {
			
			e.printStackTrace();
		} 
		
		System.out.println(config.getRootElementName());
		System.out.println(config.getProperty("[@ivorn]"));
		String newFileName="xml";
		String IvornString=config.getString("[@ivorn]");
		if (IvornString.contains("#")) {
			newFileName=IvornString.substring(IvornString.lastIndexOf("#")+1)+".xml";
		}else {
			//error log
		}
		System.out.println(newFileName);
		System.out.println(config.getString("[@ivorn]"));
		System.out.println(config.getProperty("What.Param[@value]"));
		System.out.println(config.getProperty("What.Param[@name]"));
		System.out.println(config.getProperty("What.Description"));
		System.out.println(config.getProperty("Who.Author.shortName"));
		
		List<Object> nameList = config.getList("What.Param[@name]");
		List<Object> valueList=config.getList("What.Param[@value]");		
		String valueOfPacket_type=(String) valueList.get(nameList.indexOf("Packet_Type"));
		System.out.println(valueOfPacket_type);

	}
	
	
	public static boolean isSendAliveEmailToday=false;
	public static String   lastSendAliveEmailDay="";
	/**
	 * 
	 * @param filePath 临时文件路径   tempXml FullPath
	 * @param saveRootPath 存储的根目录		root Directory (save useful XML in it)
	 * @param emails email列表 用逗号分隔 
	 * @return
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static boolean saveUsefulMessageType(String filePath,String saveRootPath, String[] emails,String ftp_ip,int ftp_port,String ftp_name,String ftp_pass,String msg_url,String msg_name) throws IOException, InterruptedException{
		SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		SimpleDateFormat lastDayFormat = new SimpleDateFormat("yyyy-MM-dd");
    	
		String copyToFilePath=saveRootPath;
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<XMLConfiguration> builder =
		    new FileBasedConfigurationBuilder<XMLConfiguration>(XMLConfiguration.class)
		    .configure(params.xml()
		        .setFileName(filePath));
		XMLConfiguration config=null;
		try {
			config= builder.getConfiguration();
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
			e.printStackTrace();
		} 
		String IvornString=config.getString("[@ivorn]"); //new file and folder name like [SWIFT#UVOT_Image_685245-940] 
		String newFileName="temp.xml";
		String newSubFolder="unknown"; //maybe from Ivorn 
		List<Object> nameList = config.getList("What.Param[@name]");
		List<Object> valueList=config.getList("What.Param[@value]");	
		String valueOfPacket_type="-1";
		if (nameList.indexOf("Packet_Type")>=0) {
			valueOfPacket_type=(String) valueList.get(nameList.indexOf("Packet_Type"));
			
		}else {
		}
		int PacketType;
		try {
			PacketType = Integer.parseInt(valueOfPacket_type);
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
			PacketType=-1;
		}
		if (IvornString.contains("#")) {
//			newFileName=IvornString.substring(IvornString.lastIndexOf("#")+1)+".xml";
			newFileName=IvornString.substring(IvornString.lastIndexOf("#")+1);
			newFileName=newFileName.replace(":", "").replace(".", "").replace("", "");
			if (PacketType!=3) {
				System.out.println(longDateFormat.format(new Date())+"\t\t\t"+newFileName);				
			}
			newFileName=newFileName+".xml";
			
		}else {
			//error log
			XML2File.writeToLog("Error  :  xml信息没有完全被解析");
			SimpleDateFormat longDateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			copyToFilePath=new File(copyToFilePath,"GcnServerLog").getPath();
			File dirFile=new File(copyToFilePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			copyToFilePath=new File(copyToFilePath,longDateFormatFile.format(new Date())+"_"+new File(filePath).getName()).getPath();
			XML2File.writeToLog(longDateFormatFile.format(new Date())+"\t\t\t"+copyToFilePath);
			System.out.println("Error  :  xml信息没有完全被解析");
			File copyToFile = new File(copyToFilePath);
			new File(filePath).renameTo(copyToFile);
		}
//	以下是某软件的目录列表
//		 AGILE
//		  backup
//		  CALET
//		  Fermi
//		  GRO
//		  HETE
//		  INTEGRAL
//		  IPN
//		  KONUS
//		  MAXI
//		  MOA
//		  SNEWS
//		  SWIFT
		String subFolderName="GcnServerLog"; //默认  default is Log
		//I am alive
		if (PacketType==3) {	
			subFolderName="GcnServerLog";
			System.out.println(longDateFormat.format(new Date())+"                              I am alive  ");
			if (!(lastSendAliveEmailDay.equals(lastDayFormat.format(new Date())))) {
				isSendAliveEmailToday=false;
			}
			if(!isSendAliveEmailToday){
//				isSendAliveEmailToday = TestSendEmail.sendToEmails( "C42 : GCN - I am alive" ,emails ,"GCN send :I am alive");	

//				XML2File.readLogToString(filePath);
				String messageContent=XML2File.readLogToString(filePath);
//				  EmailAttachment attachment = new EmailAttachment();
//				  attachment.setPath(filePath);
//				  attachment.setDisposition(EmailAttachment.INLINE);
//				  attachment.setDescription(newFileName);
//				  attachment.setName("John");
				isSendAliveEmailToday=TestSendEmail.sendToEmails( "C42 : "+newFileName,emails,messageContent);
				
				lastSendAliveEmailDay=lastDayFormat.format(new Date());
			}


			return false; 
		}
		//Kill
		if (PacketType==4) {								
			subFolderName="GcnServerLog";
			System.out.println(longDateFormat.format(new Date())+"    Kill  ");
			String messageContent="Server Kill ";
			isSendAliveEmailToday=false;
			TestSendEmail.sendToEmails( "C42 :  客户端被停止 ",emails,messageContent);
		}

		// IPN_POS 
		if (PacketType==39) {								
			subFolderName="GcnServerLog";
			System.out.println(longDateFormat.format(new Date())+"    IPN_POS : ");
		}

		// The "INTEGRAL" notices (types=51-56: POINTDIR,SPIACS,WAKEUP,REFINED,OFFLINE, and WEAK) are GRB locations as determined by the INTEGAL-IBIS/-SPI instruments. They are discussed in more detail in the INTEGRAL data product.
		if (PacketType>=51&&PacketType<=56) {				
			subFolderName="INTEGRAL";
			System.out.println(longDateFormat.format(new Date())+"    INTEGRAL");
		}

		//The "Swift" notices (types=60-99: positions, lightcurves, spectra, images, etc) are GRB locations/images/etc as determined by the Swift-BAT/-XRT/-UVOT instruments. They are discussed in more detail in the Swift data product.
		if (PacketType>=60&&PacketType<=99) {				
			subFolderName="Swift"; //or maybe you can get name from IVORN
			System.out.println(longDateFormat.format(new Date())+"    Swift");
			if (newFileName.toUpperCase().startsWith("BAT")) { //Swift_BAT_*的事件发送email
//				String messageContent=XML2File.readLogToString(filePath);
				String messageContent=GcnMailNoticeConverter.convertXmlToGcnNotice_All(config, subFolderName, PacketType, filePath);
//				String messageContent=GcnMailNoticeConverter.convertXmlToGcnNotice_Swift_Alert(config, subFolderName,PacketType);
//				  EmailAttachment attachment = new EmailAttachment();
//				  attachment.setPath(filePath);
//				  attachment.setDisposition(EmailAttachment.ATTACHMENT);
//				  attachment.setDescription(newFileName);
//				  attachment.setName("John");
//				String messageXmlContent=XML2File.readLogToString(filePath);
//				messageXmlContent="<pre>"+messageXmlContent.replace(">", "&gt;").replace("<", "&lt;")+"</pre>";
//				messageContent=messageContent+"\r\n\r\n\r\n"+messageXmlContent;
				TestSendEmail.sendToHtmlEmails( "C42 : "+newFileName,emails,messageContent);
				
				String stringRa = config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Value2.C1");
				
				//ra 可能需要转换 xml 里用的是度,acp用的是时角
//				stringRa="21";
				
				String stringDec=config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Value2.C2");	
				System.out.println(newFileName+"\t\tRA:\t"+stringRa+"\t\tDec:\t"+stringDec);
				//-----------------

				TaskData task=new TaskData("HMT_"+longDateFormat.format(new Date()), Double.parseDouble(stringRa), Double.parseDouble(stringDec), "1000");
				JSONObject jsonObject=JSONObject.fromObject(task);
				String textMessage=jsonObject.toString();
				try {
				    ConnectionFactory connectionFactory;
				    Connection connection = null;
				    Session session; 
				    Destination destination;
				    MessageProducer producer; 
				    TextMessage message = null;
			    	connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, msg_url);
					connection = connectionFactory.createConnection();		        
			        connection.start();
			        session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			        destination = session.createTopic(msg_name);
			        producer = session.createProducer(destination);
					message=session.createTextMessage(textMessage);
		            producer.send(message);
		            System.out.println(textMessage);
		            session.commit();
				} catch (JMSException e) {
					e.printStackTrace();
				}finally {
					
				}
				//------------------
				
				
				
//				String planFilePath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "BatPlan.txt").getPath();
//				double raInDegree=Double.parseDouble(stringRa);
//				double ha= (24*raInDegree)/360;
//				System.out.println(">>>>>>ha>>>>>>>>"+ha);
//				String planString=GenerateAcpPlanString.doGenerateZenithFirst(ha, stringDec, zenithRa, zenithDec);
//				XML2File.writeToPlan(planString, planFilePath);
//				File sorcePlanFile = new File(planFilePath);
//				String copyToPlanFilePath=new File("C:/Users/Public/Documents/ACP Web Data/Doc Root/plans/mayong/","BatPlan.txt").getPath();
//				sorcePlanFile.renameTo(new File(copyToPlanFilePath));
//				XML2File.writeToPlan(planString, copyToPlanFilePath);
				//判定当前时间和高度 是否需要拍摄
				//？？？？？？？？？？
//				System.out.println(">>>>>>>>");
				synchronized (GCNServer.lock) {
					MyAcpCommandList.addParam("stringRa", stringRa);
					MyAcpCommandList.addParam("stringDec", stringDec);
					MyAcpCommandList.addParam("planName", "BatPlan");
					GCNServer.lock.notify();
//					System.out.println("<<<<<<<<");
				}
				
				
//				Map<String, String> statusMap=AcpStatusUpdater.getSystemStatus();
//				String sm_obsStat = URLDecoder.decode(statusMap.get("sm_obsStat"),"UTF-8");
//				String sm_plnTitle = URLDecoder.decode(statusMap.get("sm_plnTitle"),"UTF-8");
//				String sm_az = URLDecoder.decode(statusMap.get("sm_az"),"UTF-8");
//				String sm_alt = URLDecoder.decode(statusMap.get("sm_alt"),"UTF-8");
//				String sm_ra = URLDecoder.decode(statusMap.get("sm_ra"),"UTF-8");
//				String sm_dec = URLDecoder.decode(statusMap.get("sm_plnTitle"),"UTF-8");
//				String PlanName= "BatPlan";
//				boolean isSendStopPlan=false;
//				boolean isSendRunPlan=false;
//				
//				if (sm_obsStat!=null&&sm_obsStat.equals("@In use")) { //是否是工作状态 @In use   @anReady
//				
//					if (sm_plnTitle != null&&sm_plnTitle.equals("Plan "+PlanName)) { //是否是因为BAT启动plan  Plan%20%22test2%22 Plan
//						
//						if (sm_ra!=null && sm_dec!=null && sm_ra.equals(stringRa)&&sm_dec.equals(stringDec)) { //是否是相同RaDec 目标
//							System.out.println("收到了后续的BAT消息，继续执行");
//						}else{
//							
//							//send email 收到了GCN BAT消息(sm_ra sm_dec) 但是已经在执行另外一个不同的 GCN_Plan     (stringRa  stringDec)
//						}
//						isSendRunPlan=false;
//					}else {
//						//发送stop
//						isSendStopPlan=true;
//						isSendRunPlan=true;
//					}
//				}else {
//					//发送一下stop 
//					isSendStopPlan=true;
//					isSendRunPlan=true;
//				}
//	
//				if (isSendStopPlan) {
//					//stop plan
//					int stopCounter =1;
//					String stopResult="";
//					while (!stopResult.equals(" ----Received, but there is no script running.----")) {
//						Thread.sleep(2000);
//						stopResult=AcpControl.stopRunPlan();
//						stopCounter++;
//						if (stopCounter>10) {
//							break;							
//						}
//					}
//					
//				}
//				//这里是 手动 操作望远镜 转向 ，因为撞墙的问题 >_< 
//				if (sm_az.equals(sm_dec)) {
//					System.out.println("不要撞墙啊 HMT");
//				}
//				//执行plan
//				if (isSendRunPlan) {
//					//stop plan
//					int statusCounter =1;
//					String sm_obsStatBeforeRun="";
//					while (!(sm_obsStatBeforeRun!=null&&sm_obsStatBeforeRun.equals("@anReady"))) {
//						Thread.sleep(2000);
//						Map<String, String> statusMapBeforeRun=AcpStatusUpdater.getSystemStatus();
//						sm_obsStatBeforeRun = URLDecoder.decode(statusMapBeforeRun.get("sm_obsStat"),"UTF-8");
//						if (sm_obsStatBeforeRun!=null&&sm_obsStatBeforeRun.equals("@anReady")) {
//							break;
//						}
//						statusCounter++;
//						if (statusCounter>20) {
//							break;							
//						}
//					}
//				
//					int statusCheckCounter =1;
//					String sm_plnTitle_Status="";
//					while (!(sm_plnTitle_Status!=null&&sm_plnTitle_Status.equals("Plan "+PlanName))) {
//						Thread.sleep(1000);
//						
//						String runPlanResult=AcpControl.runPlan(copyToPlanFilePath);	
//						System.out.println(runPlanResult);
//						Thread.sleep(5000);
//						Map<String, String> statusMapBeforeRun=AcpStatusUpdater.getSystemStatus();
//						 sm_plnTitle_Status = URLDecoder.decode(statusMapBeforeRun.get("sm_plnTitle"),"UTF-8");
//						if (sm_plnTitle_Status!=null&&sm_plnTitle_Status.equals("Plan \"plan\"")) {
////						if (sm_plnTitle_Status!=null&&sm_plnTitle_Status.equals("Plan "+PlanName)) {
//							break;
//						}
//						statusCheckCounter++;
//						if (statusCheckCounter>20) {
//							break;							
//						}
//					}
//				}
				
			}

		}
		
		// The "AGILE" notices (types=100-102,107-109) contains the GRB positions and spacecraft pointing_direction.
		if (PacketType==100||PacketType==101
				||PacketType==102||PacketType==107
				||PacketType==108||PacketType==109) {		
			subFolderName="AGILE";
			System.out.println(longDateFormat.format(new Date())+"    AGILE");
		}

		//The "Fermi" notices (types=110-115,118-129) contains the GRB positions and spacecraft pointing_direction.
		if ((PacketType>=110&&PacketType<=115)||(PacketType>=110&&PacketType<=115)) {				
			subFolderName="Fermi";
			System.out.println(longDateFormat.format(new Date())+"    Fermi");
		}
		//The "MAXI" notices (types=134-136) contains the Unknown- and Known-source positions and Test notices.
		if (PacketType>=134&&PacketType<=136) {				
			subFolderName="MAXI";
			System.out.println(longDateFormat.format(new Date())+"    MAXI");
		}
		//The "Counterpart" notices (type=45) contains the position of counterparts detected by follow-up observers. They are discussed in more detail in the COUNTERPART data product.
		if (PacketType==45) {				
			subFolderName="Counterpart";
			System.out.println(longDateFormat.format(new Date())+"    Counterpart");
		}
		//The "MOA" notices (type=139) contains the position of gravitational microlensing event detected by the MOA telescope. They are discussed in more detail in the MOA data product.
		if (PacketType==139) {				
			subFolderName="MOA";
			System.out.println(longDateFormat.format(new Date())+"    MOA");
		}
		//DOW_TOD:   The "DOW_TOD" (Day-of-Week Time-of-Day notices (type=48) is a special notice type that allows the receiver to monitor their connectivity (usually cell/pager email-based) by getting a notice at a user-specified time-of-day on user-specified day(s)-of-the-week. They are discussed in more detail in the DOW_TOD notice type.
		if (PacketType==48) {				
			subFolderName="GcnServerLog";
			System.out.println(longDateFormat.format(new Date())+"    DOW_TOD");
		}
//		Misc:   The other types (5-10, 12-21, & 23) are used for internal communications between the various programs which make up the GCN system. GCN customers will never get these types.
		if ((PacketType>=5&&PacketType<=10)||(PacketType>=10&&PacketType<=21)||PacketType==23) {				
			subFolderName="GcnServerLog";
			System.out.println(longDateFormat.format(new Date())+"    Misc");
		}
//		SNEWS: 149   
		if (PacketType==149) {				
			subFolderName="SNEWS";
			System.out.println(longDateFormat.format(new Date())+"    SNEWS");
//			String messageContent=XML2File.readLogToString(filePath);
			String messageContent=GcnMailNoticeConverter.convertXmlToGcnNotice_All(config, subFolderName, PacketType, filePath);
//			String messageContent=GcnMailNoticeConverter.convertXmlToGcnNotice(config, subFolderName);
//			String messageXmlContent=XML2File.readLogToString(filePath);
////			messageXmlContent="<pre>"+messageXmlContent.replace(">", "&gt;").replace("<", "&lt;")+"</pre>";
//			messageContent=messageContent+"\r\n\r\n\r\n"+messageXmlContent;
			
//			TestSendEmail.sendToEmails( "C42 : "+newFileName,emails,messageContent);
			TestSendEmail.sendToHtmlEmails( "C42 : "+newFileName,emails,messageContent);
		}
		//AMON_ICECUBE_COINC			158	AMON_ICECUBE_HESE   AMON_ICECUBE_EHE
		if (PacketType==157||PacketType==158
				||PacketType==169) {					
			subFolderName="AMON_ICECUBE";
//			System.out.println(longDateFormat.format(new Date())+"    	AMON_ICECUBE");
//			TestSendEmail.sendToEmails( "C42 : AMON_ICECUBE",emails,config.toString());
			
//			String messageContent=XML2File.readLogToString(filePath);
			String messageContent=GcnMailNoticeConverter.convertXmlToGcnNotice_All(config, subFolderName, PacketType, filePath);
//			  EmailAttachment attachment = new EmailAttachment();
//			  attachment.setPath(filePath);
//			  attachment.setDisposition(EmailAttachment.ATTACHMENT);
//			  attachment.setDescription(newFileName);
//			  attachment.setName("John");
			TestSendEmail.sendToHtmlEmails( "C42 : "+newFileName,emails,messageContent);
			
//			String planFilePath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "plan.txt").getPath();
//			String planString=GenerateAcpPlanString.doGenerate();
//			XML2File.writeToPlan(planString, planFilePath);
//			File sorcePlanFile = new File(planFilePath);
//			String copyToPlanFilePath=new File("C:/Users/Public/Documents/ACP Web Data/Doc Root/plans/mayong/","plan.txt").getPath();
//			sorcePlanFile.renameTo(new File(copyToPlanFilePath));
//			AcpControl.runPlan(copyToPlanFilePath);
			
		}
		
		SimpleDateFormat longDateFormatFile = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		copyToFilePath=new File(copyToFilePath,subFolderName).getPath();
		File dirFile=new File(copyToFilePath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
//		copyToFilePath=new File(copyToFilePath,longDateFormatFile.format(new Date())+"_"+new File(filePath).getName()).getPath();
		copyToFilePath=new File(copyToFilePath,newFileName).getPath();
		XML2File.writeToLog(longDateFormatFile.format(new Date())+"\t\t\t"+copyToFilePath);
//		System.out.println(copyToFilePath);
		File copyToFile = new File(copyToFilePath);
		//TODO save to FTP subFolderName/copyToFile

		InputStream  in = null ;
		try {
			FTPClient ftp=new FTPClient();
			 ftp.connect(ftp_ip, ftp_port);
			 ftp.login(ftp_name, ftp_pass);
			 String ftpSbuPath=subFolderName;
			 ftp.enterLocalPassiveMode();
			 String ftpFileName =filePath;
			 String destFileName ="";
			 String ftpDestFileName=newFileName;
			 in = new BufferedInputStream(new FileInputStream(ftpFileName));
			
				boolean isSubDirectoryExsit = false;
				FTPFile[] dirs = ftp.listDirectories();
				if (dirs != null && dirs.length > 0) {
				    for (int i = 0; i < dirs.length; i++) {
				        if (dirs[i].getName().equals(ftpSbuPath)) {
				            isSubDirectoryExsit = true;
				        }
				        break;
				    }
				}
				dirs = null;
				if (!isSubDirectoryExsit && !ftpSbuPath.equals("")) {
			ftp.makeDirectory(ftpSbuPath);
			destFileName = ftpSbuPath + "/" + ftpDestFileName;
			}
			if (isSubDirectoryExsit && !ftpSbuPath.equals("")) {
				destFileName = ftpSbuPath + "/" + ftpDestFileName;
			}
			 
			if (!ftp.storeFile(destFileName, in)) {
				throw new IOException("Can't upload file '" + ftpFileName + "' to FTP server. Check FTP permissions and path.");
			}
			in.close();
			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 

        
        
		new File(filePath).renameTo(copyToFile);

		return false; //这里应该是输出错误信息
				
	}
}
