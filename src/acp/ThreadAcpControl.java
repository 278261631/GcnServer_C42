package acp;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreadAcpControl extends Thread {

	private Object lock;

	public ThreadAcpControl(Object lock) {
		super();
		this.lock = lock;
	}

	@SuppressWarnings("unused")
	@Override
	public void run() {
//		String PlanName="BatPlan";
		String copyToPlanFilePath=new File("C:/Users/Public/Documents/ACP Web Data/Doc Root/plans/mayong/","BatPlan.txt").getPath();;
		List<Map<String ,String>> paramMap_threadList=new ArrayList<Map<String,String>>();
			while (true) {
					synchronized (lock) {
						System.out.println(MyAcpCommandList.getParamMap().size());	
						
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
						stringRa="@an"+stringRa;
						stringDec="@an"+stringDec;
						String planName = paramMap_thread.get("planName");
						planName="\""+planName+"\"";
						
						
						Map<String, String> statusMap = AcpStatusUpdater.getSystemStatus();
						String sm_obsStat="";
						String sm_plnTitle="";
						String sm_az="";
						String sm_alt="";
						String sm_ra="";
						String sm_dec="";
						try {
							sm_obsStat = URLDecoder.decode(statusMap.get("sm_obsStat"), "UTF-8");
							sm_plnTitle = URLDecoder.decode(statusMap.get("sm_plnTitle"), "UTF-8");
							sm_az = URLDecoder.decode(statusMap.get("sm_az"), "UTF-8");
							sm_alt = URLDecoder.decode(statusMap.get("sm_alt"), "UTF-8");
							sm_ra = URLDecoder.decode(statusMap.get("sm_ra"), "UTF-8");
							sm_dec = URLDecoder.decode(statusMap.get("sm_dec"), "UTF-8");
							System.out.println(">>>>"+statusMap.get("sm_dec"));
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						boolean isSendStopPlan = false;
						boolean isSendRunPlan = false;
						System.out.println(""+ sm_plnTitle + "----" + "Plan" + planName +"\t\t--"+ sm_plnTitle.equals("Plan " + planName)+ "--"+sm_obsStat);
						if (sm_obsStat != null && sm_obsStat.equals("@anIn use")) { //是否是工作状态 @In use   @anReady

							if (sm_plnTitle != null && sm_plnTitle.equals("Plan " + planName)) { //是否是因为BAT启动plan  Plan%20%22test2%22 Plan
								System.out.println("-----"+sm_ra+"-x-"+stringRa+"-x-"+sm_dec+"-x-"+stringDec);
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
						} else {
							//发送一下stop 
//							isSendStopPlan = true;
							isSendRunPlan = true;
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
								stopResult = AcpControl.stopRunPlan();
								stopCounter++;
								if (stopCounter > 10) {
									break;
								}
							}

						}
						//这里是 手动 操作望远镜 转向 ，因为撞墙的问题 >_< 
						if (sm_az.equals(sm_dec)) {
							System.out.println("不要撞墙啊 HMT");
						}
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
								Map<String, String> statusMapBeforeRun = AcpStatusUpdater.getSystemStatus();
								try {
									sm_obsStatBeforeRun = URLDecoder.decode(statusMapBeforeRun.get("sm_obsStat"), "UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
								if (sm_obsStatBeforeRun != null && sm_obsStatBeforeRun.equals("@anReady")) {
									break;
								}
								statusCounter++;
								if (statusCounter > 20) {
									break;
								}
							}

							int statusCheckCounter = 1;
							String sm_plnTitle_Status = "";
							while (!(sm_plnTitle_Status != null && sm_plnTitle_Status.equals("Plan " + planName))) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}

								String runPlanResult = AcpControl.runPlan(copyToPlanFilePath);
								System.out.println(runPlanResult);
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								Map<String, String> statusMapBeforeRun = AcpStatusUpdater.getSystemStatus();
								try {
									sm_plnTitle_Status = URLDecoder.decode(statusMapBeforeRun.get("sm_plnTitle"), "UTF-8");
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								}
//								if (sm_plnTitle_Status != null && sm_plnTitle_Status.equals("Plan \"plan\"")) {
								System.out.println(sm_plnTitle_Status+"\t\t"+ "Plan "+planName);
								if (sm_plnTitle_Status!=null&&sm_plnTitle_Status.equals("Plan "+planName)) {
									break;
								}
								statusCheckCounter++;
								if (statusCheckCounter > 20) {
									break;
								}
							}
						}
						
					}else {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			}

	}
}