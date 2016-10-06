package acp;

import java.io.File;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Test;

import util.XML2File;

public class GenerateAcpPlanString {

	
	
	public static String doGenerate(){
//		#startsetnum 4 ; Persistent set numbering added by ACP
//		#INTERVAL 120
//		#FILTER Luminance                  ; Required if your system has filters
//		M 11
		StringBuffer sb=new StringBuffer();
		sb.append("#INTERVAL 120");
		sb.append("\r\n");
		sb.append("#FILTER Luminance");
		sb.append("\r\n");
		sb.append("M 15");
		sb.append("\r\n");
		return sb.toString();
	}
	public static String doGenerate(String ra,String dec){

		StringBuffer sb=new StringBuffer();
		sb.append("#dir GRB");
		sb.append("\r\n");
		sb.append("#binning 2");
		sb.append("\r\n");
		sb.append("#interval 40");
		sb.append("\r\n");
		sb.append("GRB-40S-1	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 40");
		sb.append("\r\n");
		sb.append("GRB-40S-2	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 40");
		sb.append("\r\n");
		sb.append("GRB-40S-3	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 60");
		sb.append("\r\n");
		sb.append("GRB-60S-1	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 60");
		sb.append("\r\n");
		sb.append("GRB-60S-2	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 60");
		sb.append("\r\n");
		sb.append("GRB-60S-3	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-1	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-2	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-3	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-4	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-5	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-6	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-7	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-8	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-9	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-10	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-11	"+ra+"	"+dec);
		sb.append("\r\n");
		sb.append("#interval 90");
		sb.append("\r\n");
		sb.append("GRB-90S-12	"+ra+"	"+dec);
		sb.append("\r\n");
		return sb.toString();
	}
	
	/**
	 * 先去天顶 防止HMT撞墙 （必要的情况下 ） 执行完plan 返回预订位置，连接到之前被停止的plan （如果有的话）
	 * @param ra 单位 时角 （double） 需要转换成时角，因为plan里用的是时角
	 * @param dec 单位 度 （double） plan里用的是度
	 * @return
	 */
	public static String doGenerateZenithFirst(String ra,String dec,String zenithRa,String zenithDec,String filterName,String lastPlanPath){
		
		
		
		StringBuffer sb=new StringBuffer();
		sb.append("#dir GRB");
		sb.append("\r\n");
		sb.append("#binning 4");
		sb.append("\r\n");
		if (filterName!=null&&filterName.length()>0) {
			sb.append("#FILTER "+filterName);
			sb.append("\r\n");			
		}
		sb.append("#interval 0.2");
		sb.append("\r\n");
		sb.append("GRB-ToZenith	"+zenithRa+"	"+zenithDec);
		sb.append("\r\n");
		sb.append("#interval 40");
		sb.append("\r\n");
		sb.append("GRB-40S-1	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 40");
//		sb.append("\r\n");
//		sb.append("GRB-40S-2	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 40");
//		sb.append("\r\n");
//		sb.append("GRB-40S-3	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 60");
//		sb.append("\r\n");
//		sb.append("GRB-60S-1	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 60");
//		sb.append("\r\n");
//		sb.append("GRB-60S-2	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 60");
//		sb.append("\r\n");
//		sb.append("GRB-60S-3	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-1	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-2	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-3	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-4	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-5	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-6	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-7	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-8	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-9	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-10	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-11	"+ra+"	"+dec);
//		sb.append("\r\n");
//		sb.append("#interval 90");
//		sb.append("\r\n");
//		sb.append("GRB-90S-12	"+ra+"	"+dec);
//		sb.append("\r\n");
		sb.append("#interval 0.2");
		sb.append("\r\n");
		sb.append("GRB-ToZenith	"+zenithRa+"	"+zenithDec);
		sb.append("\r\n");
		File stopedPlan=new File(lastPlanPath);
		if (lastPlanPath!=null&&lastPlanPath.length()>0&&stopedPlan.exists()&&stopedPlan.isFile()) {
			sb.append("#CHAIN "+lastPlanPath);
			sb.append("\r\n");			
			System.out.println("#chain : last stoped plan file ="+lastPlanPath);
		}else {
			System.out.println("File may not exist, path ="+lastPlanPath);
		}
		return sb.toString();
	}
	
	
	@Test
	public void testDoGenerate(){
		System.out.println(GenerateAcpPlanString.doGenerate());
	}
	
	
	@Test
	public void testDoGenerateBAT(){
		String ra="19.58027";
		String dec="21.8799";
		System.out.println(GenerateAcpPlanString.doGenerate(ra,dec));
	}
	
	@Test 
	public void testFileDir(){
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
    	String acpPlanPath = config.getString("acpPlanPath");
    	String python27Path = config.getString("python27Path");
    	String acpUrl = config.getString("acpUrl");
    	String acpUser = config.getString("acpUser");
    	String acpPass = config.getString("acpPass");
    	String filter = config.getString("filter","");
    	String HmtLimitAngle = config.getString("HmtLimitAngle","30");
    	String HMTlastPlanDir = config.getString("HMTlastPlanDir","");
//		String HMTlastPlanDir="C:\\Users\\Public\\Documents\\ACP Web Data\\Doc Root\\plans";
//		String HMTlastPlanDir="C:\\Users\\Public\\Documents\\ACP Web Data\\Doc Root\\plans\\";
//		String HMTlastPlanDir="C:/Users/Public/Documents/ACP Web Data/Doc Root/plans";
//		String HMTlastPlanDir="C:/Users/Public/Documents/ACP Web Data/Doc Root/plans";
    	System.out.println(HMTlastPlanDir);
		File lastPlanDir=new File(HMTlastPlanDir);
		if (lastPlanDir.isDirectory()&&lastPlanDir.exists()) {
			System.out.println(HMTlastPlanDir);
			File[] planFiles=lastPlanDir.listFiles();
			long lastModifyTime=0;
			for (File file : planFiles) {
				if (file.isFile()&&file.getName().toLowerCase().endsWith(".txt")&&file.lastModified()>lastModifyTime) {
					lastModifyTime=file.lastModified();
					System.out.println(file.getAbsolutePath());
				}
			}
		}
	}
}



