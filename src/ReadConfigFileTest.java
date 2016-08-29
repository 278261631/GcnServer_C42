
  
import java.io.File;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;


  
/** 
 * <pre> 
 * Test.java 
 * @author kanpiaoxue<br> 
 * @version 1.0 
 * Create Time 2014年7月6日 下午10:54:37<br> 
 * Description : 读取 *.properties 配置文件 
 * </pre> 
 */  
public class ReadConfigFileTest {  
  
    public static void main(String[] args) {  
    	
    	Configurations configs = new Configurations();
    	try
    	{
    	    Configuration config = configs.properties(new File("usergui.properties"));
    	 
    	    System.out.println("colors.background:"  
    	    		+ config.getString("colors.background"));  
    	    // output : colors.background:#FFFFFF  
    	    
//    	    System.out.println("window.width:" + config.getInt("window.width"));  
//    	    System.out.println("window.width:" + config.getInt("hehe"));  
    	    // output: window.width:500  
    	    
//    	    String[] colors = config.getStringArray("colors.pie");  
//    	    System.out.println("colors.pie array:" + colors);  
    	    // output: colors.pie array:[Ljava.lang.String;@369ca84f  
    	    
//    	    List<Object> colorList = config.getList("colors.pie");  
//    	    System.out.println("colors.pie list:" + colorList);  
    	    System.out.println(config.toString());
    	}
    	catch (ConfigurationException cex)
    	{
    	    // Something went wrong
    		cex.printStackTrace();
    	}
      
  
    }  
}  