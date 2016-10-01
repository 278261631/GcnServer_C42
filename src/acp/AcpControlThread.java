package acp;

import java.util.List;

public class AcpControlThread implements Runnable{

	 String taskName;
	    
	    public AcpControlThread(String taskName){
	        this.taskName=taskName;
	    }

	    private int productNum = 1000000;

	    private String customerName  ;

	    public String getCustomerName() {
			return customerName;
		}

		public void setCustomerName(String customerName) {
			this.customerName = customerName;
		}

		public void run() {

	        for (int i = 0; i < productNum; i++) {
	            
               try {
				Thread.sleep(2000);
				System.out.println(taskName + " ->      "+ getCustomerName());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
                productNum -= 1;
	            
	        }

	    }
}





