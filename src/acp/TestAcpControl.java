package acp;




public class TestAcpControl {
	public static void main(String[] args) {
//		 runPlan();
//		 stopRunPlan();
	    }

	@org.junit.Test
	public   void runPlan() {
		String filePath="C:/Users/Public/Documents/ACP Web Data/Doc Root/plans/mayong/test2.txt";
		System.out.println( AcpControl.runPlan(filePath));
	        
	}    
	
	@org.junit.Test
	public  void stopRunPlan() {
		 System.out.println( AcpControl.stopRunPlan());
	}    
	
	
	// ----[lba info]You are already running the AcquireImages.js script now. When it finishes you can start your run.----
  	// ----Received. Script will be interrupted as soon as possible.----
	// ----Received, but there is no script running.----
	// ----Run started successfully. See [[System Status]] for ongoing info. @@You can close this item@@ now.----
	
	@org.junit.Test
	public  void updateSystemStatus() {
		 System.out.println( AcpStatusUpdater.updateSystemStatus());
	}    
//	_s('sm_obsStat','@anIn%20use');_s('sm_plnTitle','Plan%20%22test2%22');
//	_s('sm_obsStat','@anReady'); _s('sm_plnTitle','Plan');
	
	//启动一个线程来操作ACP  启动前 先检查ACP操控线程是否存在，如果存在 还要检查当前的启动的plan 是不是已经在运行，如果没运行（
	//这是有可能能的 因为BAT警报密集的时候会两分钟内出发多次警报，而两分钟内又没有执行完上一个plan，这个时候，怎么办？）
	public void startNewThread(){
		
	}

}
