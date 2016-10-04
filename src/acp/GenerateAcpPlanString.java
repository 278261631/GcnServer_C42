package acp;

import org.junit.Test;

public class GenerateAcpPlanString {

	
	
	public static String doGenerate(){
//		#startsetnum 4 ; Persistent set numbering added by ACP
//		#INTERVAL 120
//		#FILTER Luminance                  ; Required if your system has filters
//		M 11
		StringBuffer sb=new StringBuffer();
		sb.append("#INTERVAL 120");
		sb.append("\n");
		sb.append("#FILTER Luminance");
		sb.append("\n");
		sb.append("M 15");
		sb.append("\n");
		return sb.toString();
	}
	public static String doGenerate(String ra,String dec){

		StringBuffer sb=new StringBuffer();
		sb.append("#dir GRB");
		sb.append("\n");
		sb.append("#binning 2");
		sb.append("\n");
		sb.append("#interval 40");
		sb.append("\n");
		sb.append("GRB-40S-1	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 40");
		sb.append("\n");
		sb.append("GRB-40S-2	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 40");
		sb.append("\n");
		sb.append("GRB-40S-3	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 60");
		sb.append("\n");
		sb.append("GRB-60S-1	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 60");
		sb.append("\n");
		sb.append("GRB-60S-2	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 60");
		sb.append("\n");
		sb.append("GRB-60S-3	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-1	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-2	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-3	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-4	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-5	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-6	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-7	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-8	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-9	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-10	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-11	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-12	"+ra+"	"+dec);
		sb.append("\n");
		return sb.toString();
	}
	
	/**
	 * 先去天顶 防止HMT撞墙 （必要的情况下 ）
	 * @param ra 单位 时角 （double） 需要转换成时角，因为plan里用的是时角
	 * @param dec 单位 度 （double） plan里用的是度
	 * @return
	 */
	public static String doGenerateZenithFirst(String ra,String dec,String zenithRa,String zenithDec){
		
		
		
		StringBuffer sb=new StringBuffer();
		sb.append("#dir GRB");
		sb.append("\n");
		sb.append("#binning 2");
		sb.append("\n");
		sb.append("#interval 1");
		sb.append("\n");
		sb.append("GRB-ToZenith	"+zenithRa+"	"+zenithDec);
		sb.append("#interval 40");
		sb.append("\n");
		sb.append("GRB-40S-1	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 40");
		sb.append("\n");
		sb.append("GRB-40S-2	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 40");
		sb.append("\n");
		sb.append("GRB-40S-3	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 60");
		sb.append("\n");
		sb.append("GRB-60S-1	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 60");
		sb.append("\n");
		sb.append("GRB-60S-2	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 60");
		sb.append("\n");
		sb.append("GRB-60S-3	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-1	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-2	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-3	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-4	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-5	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-6	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-7	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-8	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-9	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-10	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-11	"+ra+"	"+dec);
		sb.append("\n");
		sb.append("#interval 90");
		sb.append("\n");
		sb.append("GRB-90S-12	"+ra+"	"+dec);
		sb.append("\n");
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
}



//
//
//#dir GRB
//#binning 2
//#interval 40
//GRB-40S-1	19.58027	21.8799
//#interval 40
//GRB-40S-2	19.58027	21.8799
//#interval 40
//GRB-40S-3	19.58027	21.8799
//#interval 60
//GRB-60S-1	19.58027	21.8799
//#interval 60
//GRB-60S-2	19.58027	21.8799
//#interval 60
//GRB-60S-3	19.58027	21.8799
//#interval 60
//GRB-60S-4	19.58027	21.8799
//#interval 90
//GRB-90S-1	19.58027	21.8799
//#interval 90
//GRB-90S-2	19.58027	21.8799
//#interval 90
//GRB-90S-3	19.58027	21.8799
//#interval 90
//GRB-90S-4	19.58027	21.8799
//#interval 90
//GRB-90S-5	19.58027	21.8799
//#interval 90
//GRB-90S-6	19.58027	21.8799
//#interval 90
//GRB-90S-7	19.58027	21.8799
//#interval 90
//GRB-90S-8	19.58027	21.8799
//#interval 90
//GRB-90S-9	19.58027	21.8799
//#interval 90
//GRB-90S-10	19.58027	21.8799
//#interval 90
//GRB-90S-11	19.58027	21.8799
//#interval 90
//GRB-90S-12	19.58027	21.8799
