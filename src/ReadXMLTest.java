import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;


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
	
	/**
	 * 
	 * @param filePath 临时文件路径   tempXml FullPath
	 * @param saveRootPath 存储的根目录		root Directory (save useful XML in it)
	 * @return
	 * @throws IOException 
	 */
	public static boolean saveUsefulMessageType(String filePath,String saveRootPath) throws IOException{
		SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    	
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
			return false; 
		}
		//Kill
		if (PacketType==4) {								
			subFolderName="GcnServerLog";
			System.out.println(longDateFormat.format(new Date())+"    Kill  ");
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
		new File(filePath).renameTo(copyToFile);
		
		return false; //这里应该是输出错误信息
				
	}
}
