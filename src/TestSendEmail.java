import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.XMLConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class TestSendEmail {

	public static void main(String[] args) {
//		Email email = new SimpleEmail();
//		email.setHostName("smtp.163.com");
//		email.setSmtpPort(465);
//		//密码是pai(10)a
//		email.setAuthenticator(new DefaultAuthenticator("xxxx", "xxxxxxxxxxx")); //这里是163的客户端登录授权码 不是登录163邮箱的密码
//		email.setSSLOnConnect(true);
//		try {
//			email.setFrom("xxxxx@163.com");
//			email.setSubject("TestMail");
//			email.setMsg("This is a test mail ... :-)");
//			email.addTo("mars.maya.g@gmail.com");
//			email.addTo("xxxxxxx@qq.com");
//			email.send();
//			
//		} catch (EmailException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//==============================================================================
//		FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
//			    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
//			    .configure(new Parameters().properties()
//			        .setFileName("usergui.properties")
//			        .setThrowExceptionOnMissing(true)
//			        .setListDelimiterHandler(new DefaultListDelimiterHandler(','))
//			        .setIncludesAllowed(false));
//			PropertiesConfiguration config = null;
//			try {
//				config = builder.getConfiguration();
//			} catch (ConfigurationException e) {
//				e.printStackTrace();
//			}
		
		
//		Configurations configs = new Configurations();
//		
//		PropertiesConfiguration  config = null;
//    	try
//    	{
//    	    config = configs.properties(new File("usergui.properties"));
//    	}
//    	catch (ConfigurationException cex)
//    	{
//    		XML2File.writeToLog(cex.getMessage());
//    		cex.printStackTrace();
//    	}
//    	config.setListDelimiterHandler(new DefaultListDelimiterHandler(','));
//    	String[] emails=config.getStringArray("emails");//"@qq.com"
//    	String emailString=config.getString("emails");//"@qq.com"
//    	String[] emails=emailString.split(",");
//    	String resultString=TestSendEmail.emailCheck(emails);
//    	if (null!=resultString && resultString.length()>0) {
//			System.out.println(resultString);
//			return;
//		}
//    	===================================================================
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
		    new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
		    .configure(params.xml()
		        .setFileName("usergui.properties")
		        .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
		PropertiesConfiguration config=null;
		try {
			config= builder.getConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	String[] emails = config.getStringArray("emails");
    	String resultString=TestSendEmail.emailCheck(emails);
    	if (null!=resultString && resultString.length()>0) {
			System.out.println(resultString);
			return;
		}
    	//==============================================================
		
	}
	public static SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	public static boolean sendToEmails(String subjectString, String[] emails, String contentString) {
		Email email = new SimpleEmail();
		email.setHostName("smtp.163.com");
		email.setSmtpPort(465);
		//密码是pai(10)a
		email.setAuthenticator(new DefaultAuthenticator("c42123email", "31234123141591232654a")); //这里是163的客户端登录授权码 不是登录163邮箱的密码
		email.setSSLOnConnect(true);
		try {
			email.setFrom("123123@163.com");
			email.setSubject(subjectString);
			email.setMsg(contentString);
			if (emails==null||emails.length<1) {
				email.addTo("xxxxx@qq.com");
				email.setMsg("C42 : email 地址为空"+emails);
			}else {				
				email.addTo(emails);
			}
//			email.addTo("mars.maya.g@gmail.com");
			
			System.out.println(longDateFormat.format(new Date())+"   sending email :"+email.getToAddresses()+"\t"+email.getSubject());
			email.send();
			
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	public static String emailCheck(String[] emails){
		String msg="";
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";  
		 Pattern regex = Pattern.compile(check);  
		 for (String emailString : emails) {
			 Matcher matcher = regex.matcher(emailString);  
			 boolean isMatched = matcher.matches();  
			 System.out.println(isMatched+"\t\t"+emailString);  
			if (!isMatched) {
				msg+="\t"+emailString+"\t"+"Email地址不对";
			}
		}
		 return msg;
	}
}
