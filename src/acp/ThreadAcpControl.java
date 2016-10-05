package acp;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import util.XML2File;

public class ThreadAcpControl extends Thread {

	private Object lock;

	public ThreadAcpControl(Object lock) {
		super();
		this.lock = lock;
	}

	@SuppressWarnings("unused")
	@Override
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
    	String acpPlanPath = config.getString("acpPlanPath");
    	String python27Path = config.getString("python27Path");
    	String acpUrl = config.getString("acpUrl");
    	String acpUser = config.getString("acpUser");
    	String acpPass = config.getString("acpPass");
    	String filter = config.getString("filter","");
    	String HmtLimitAngle = config.getString("HmtLimitAngle","30");
		
//		String PlanName="BatPlan";
		String copyToPlanFilePath=new File(acpPlanPath,"BatPlan.txt").getPath();
		
		List<Map<String ,String>> paramMap_threadList=new ArrayList<Map<String,String>>();
			while (true) {
					synchronized (lock) {
//						System.out.println("ACP 控制线程：  我也还活着 " +MyAcpCommandList.getParamMap().size());	
						
						if (MyAcpCommandList.getParamMap().size()>0) {
/*							String commandString= MyAcpCommandList.getItem(0);
							MyAcpCommandList.remove();*/	
							Map<String ,String> paramMap_thread=new HashMap<String, String>();
							paramMap_thread.putAll(MyAcpCommandList.getParamMap());
							MyAcpCommandList.clearParam();
							paramMap_threadList.add(paramMap_thread);
							lock.notify();
						}
					}
		

					
					if (paramMap_threadList.size()>0) { 
						Map<String ,String> paramMap_thread =new HashMap<String, String>();
						paramMap_thread.putAll(paramMap_threadList.get(0));
						paramMap_threadList.remove(0);
						
						String stringRa=paramMap_thread.get("stringRa");
						String stringDec=paramMap_thread.get("stringDec");
//						stringRa="@an"+stringRa;
//						stringDec="@an"+stringDec;
//						String planName = paramMap_thread.get("planName");
//						planName="\""+planName+"\"";
						
						
						Map<String, String> statusMap = AcpStatusUpdater.getSystemStatus(acpUrl,acpUser,acpPass);
						String sm_obsStat="";
//						String sm_plnTitle=""; //plan title 在acp5里没有  就考虑用obsOwner 来判定当前是否已经在执行自己的plan
						String sm_obsOwner="";
						String sm_az="";
						String sm_alt="";
						String sm_ra="";
						String sm_dec="";
						try {
							sm_obsStat = URLDecoder.decode(statusMap.get("sm_obsStat"), "UTF-8");
//							sm_plnTitle = URLDecoder.decode(statusMap.get("sm_plnTitle")==null?statusMap.get("sm_plnTitle"):"", "UTF-8"); //acp 5 do not have plan here
							sm_obsOwner = URLDecoder.decode(statusMap.get("sm_obsOwner"), "UTF-8"); //acp 5 do not have plan here
							sm_az = URLDecoder.decode(statusMap.get("sm_az"), "UTF-8");
							sm_alt = URLDecoder.decode(statusMap.get("sm_alt"), "UTF-8");
							sm_ra = URLDecoder.decode(statusMap.get("sm_ra"), "UTF-8");
							sm_dec = URLDecoder.decode(statusMap.get("sm_dec"), "UTF-8");
							System.out.println(">>>>"+statusMap.get("sm_obsOwner"));
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						boolean isSendStopPlan = false;
						boolean isSendRunPlan = false;
//						System.out.println(""+ sm_plnTitle + "----" + "Plan" + planName +"\t\t--"+ sm_plnTitle.equals("Plan " + planName)+ "--"+sm_obsStat);
						if (sm_obsStat != null && sm_obsStat.equals("@anIn use")) { //是否是工作状态 @In use   @anReady

//							if (sm_plnTitle != null && sm_plnTitle.equals("Plan " + planName)) { //是否是因为BAT启动plan  Plan%20%22test2%22 Plan
							if (sm_obsOwner != null && sm_obsOwner.equals("@an" + acpUser)) { //是否是因为BAT启动plan  Plan%20%22test2%22 Plan
								System.out.println("收到了后续的BAT消息，继续执行");

//								if (sm_ra != null && sm_dec != null && sm_ra.equals("@an"+stringRa)
//										&& sm_dec.equals("@an"+stringDec)) { //是否是相同RaDec 目标
//									System.out.println("收到了后续的BAT消息，继续执行");
//								} else {
//
//									//send email 收到了GCN BAT消息(sm_ra sm_dec) 但是已经在执行另外一个不同的 GCN_Plan     (stringRa  stringDec)
//								}
								isSendRunPlan = false;
							} else {
								//发送stop
								isSendStopPlan = true;
								isSendRunPlan = true;
							}
						} else if (sm_obsStat != null && sm_obsStat.equals("@anReady")) {
							//发送一下stop 
//							isSendStopPlan = true;
							isSendRunPlan = true;
						}else {
							System.out.println("ACP 状态不正常\t\t"+sm_obsStat);
							synchronized (lock) {
								if (paramMap_thread!=null) {
									MyAcpCommandList.getParamMap().putAll(paramMap_thread);;
									System.out.println(MyAcpCommandList.getParamMap().size());	
								}
								lock.notify();									
							}
						}
						
						System.out.println("isRun\t\t"+isSendRunPlan + "\t\tisStop\t\t" + isSendStopPlan);
						if (isSendStopPlan) {
							//stop plan
							int stopCounter = 1;
							String stopResult = "";
							while (!stopResult.equals(" ----Received, but there is no script running.----")) {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								stopResult = AcpControl.stopRunPlan(acpUrl,acpUser,acpPass);
								stopCounter++;
								if (stopCounter > 10) {
									break;
								}
							}

						}
						//这里是 手动 操作望远镜 转向 ，因为撞墙的问题 >_< 
						System.out.println("-----"+sm_ra+"-x-"+stringRa+"-x-"+sm_dec+"-x-"+stringDec +"\t\t调整HMT 防止撞墙");
						String scriptPath=new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "python/test.py").getPath();
						Process process;
						String batObjectAltAz="";
						try {
							String param1=stringRa; //ra in Degrees
							String param2=stringDec; //dec
							process = Runtime.getRuntime().exec(python27Path+" "+ scriptPath +" " + param1+" "+param2 );
							InputStreamReader ir = new InputStreamReader(process.getInputStream());  
							LineNumberReader input = new LineNumberReader(ir);  
							String line;  
							while((line = input.readLine()) != null) 
								batObjectAltAz=new String(line);
								System.out.println(line);  
							input.close();  
							ir.close();  
						
						} catch (IOException e) {
						
							e.printStackTrace();
						}
						String batObjectAltInDegree="";
						String batObjectAzInDegree="";
				
						
						if (batObjectAltAz!=null&&batObjectAltAz.contains(",")) {
							batObjectAltInDegree=batObjectAltAz.split(",")[0];
							batObjectAzInDegree=batObjectAltAz.split(",")[1];
						}else {
							System.out.println("Error acp控制程序异常 没有的正确的天顶坐标 "+batObjectAltAz.contains(","));
							return;
						}
						if (Double.parseDouble(batObjectAltInDegree)<Double.parseDouble(HmtLimitAngle)) {
							System.out.println("HMT  Will Hit The Wall :" +batObjectAltAz);
							return;
						}
						
//						if (sm_az.equals(sm_dec)) {
//							System.out.println("不要撞墙啊 HMT");
//						}
						//执行plan
						if (isSendRunPlan) {
							//stop plan
							int statusCounter = 1;
							String sm_obsStatBeforeRun = "";
							while (!(sm_obsStatBeforeRun != null && sm_obsStatBeforeRun.equals("@anReady"))) {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Map<String, String> statusMapBeforeRun = AcpStatusUpdater.getSystemStatus(acpUrl,acpUser,acpPass);
								try {
									sm_obsStatBeforeRun = URLDecoder.decode(statusMapBeforeRun.get("sm_obsStat"), "UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								if (sm_obsStatBeforeRun != null && sm_obsStatBeforeRun.equals("@anReady")) {
									break;
								}
								statusCounter++;
								if (statusCounter > 40) {
									System.out.println("尝试超过40次等待 状态是\t"+sm_obsStatBeforeRun+"而不是期望的 @anReady");
									break;
								}
							}
							if ((sm_obsStatBeforeRun != null && sm_obsStatBeforeRun.equals("@anReady"))) {
								double raInDegree=Double.parseDouble(stringRa);
								double ha= (24*raInDegree)/360;
								System.out.println(">>>>>>ha>>>>>>>>"+ha);
								//生成天顶坐标 Ra （ha） Dec(degree)  //现在生成的是东西两个距离子午线5度 高度角50度的点，据说，这两个点到哪都不会撞墙
								String zenithScriptPath =new File(Class.class.getClass().getResource("/").getPath().replace("%20", " "), "python/getHmtMegicRaDec.py").getPath();
								Process zenithProcess;
								String zenithOutLine="";  
								String zenithOutLineBuffer="";  
								try {
//									String param1=stringRa; //ra in Degrees
//									String param2=stringDec; //dec
									zenithProcess = Runtime.getRuntime().exec(python27Path+" "+ zenithScriptPath  );
									InputStreamReader ir = new InputStreamReader(zenithProcess.getInputStream());  
									LineNumberReader input = new LineNumberReader(ir);  
									while((zenithOutLine = input.readLine()) != null)  
										zenithOutLineBuffer=new String(zenithOutLine);
//										System.out.println(zenithOutLine);  
										input.close();  
										ir.close();  
//										System.out.println(zenithOutLineBuffer);
								} catch (IOException e) {
								
									e.printStackTrace();
								}
								
								String zenithRaEast="";
								String zenithDecEast="";
								String zenithRaWest="";
								String zenithDecWest="";
								
								if (zenithOutLineBuffer!=null&&zenithOutLineBuffer.contains(",")) {
									zenithRaEast=zenithOutLineBuffer.split(",")[0];
									zenithDecEast=zenithOutLineBuffer.split(",")[1];
									zenithRaWest=zenithOutLineBuffer.split(",")[2];
									zenithDecWest=zenithOutLineBuffer.split(",")[3];
								}else {

									System.out.println("Error acp控制程序异常 没有的正确的天顶坐标 "+zenithOutLineBuffer.contains(","));
									return;
								}
								String zenithRa="";
								String zenithDec="";
								if (Double.parseDouble(batObjectAzInDegree)>=180) {
									zenithRa=zenithRaWest;
									zenithDec=zenithDecWest;
								}else {
									zenithRa=zenithRaEast;
									zenithDec=zenithDecEast;
								}
								String planString=GenerateAcpPlanString.doGenerateZenithFirst(Double.toString(ha), stringDec, zenithRa, zenithDec,filter);
								XML2File.writeToPlan(planString, copyToPlanFilePath);
								
							}else {
								System.out.println("Error ");
							}
							

							int statusCheckCounter = 1;
//							String sm_plnTitle_Status = "";
							String sm_obsOwner_Status = "";
//							while (!(sm_plnTitle_Status != null && sm_plnTitle_Status.equals("Plan " + planName))) {
							while (!(sm_obsOwner_Status != null && sm_obsOwner_Status.equals("@an" + acpUser))) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								String runPlanResult = AcpControl.runPlan(copyToPlanFilePath,acpUrl,acpUser,acpPass);
								System.out.println(runPlanResult);
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Map<String, String> statusMapBeforeRun = AcpStatusUpdater.getSystemStatus(acpUrl,acpUser,acpPass);
								try {
									sm_obsOwner_Status = URLDecoder.decode(statusMapBeforeRun.get("sm_obsOwner"), "UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
//								if (sm_plnTitle_Status != null && sm_plnTitle_Status.equals("Plan \"plan\"")) {
//								System.out.println(sm_plnTitle_Status+"\t\t"+ "Plan "+planName);
								if (sm_obsOwner_Status!=null&&sm_obsOwner_Status.equals("@an"+acpUser)) {
									break;
								}
								statusCheckCounter++;
								if (statusCheckCounter > 20) {
									break;
								}
							}
						}
						
					}else {
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			}

	}
}