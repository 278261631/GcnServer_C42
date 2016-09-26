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
	
	
	@Test
	public void testDoGenerate(){
		System.out.println(GenerateAcpPlanString.doGenerate());
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
