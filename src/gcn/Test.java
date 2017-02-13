package gcn;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.mhuss.AstroLib.AstroDate;

public class Test {

	public static void main(String[] args) {
		
	}
	
	@org.junit.Test
	public void testEnum(){
		TYPE_MACRO_NAME xx= TYPE_MACRO_NAME.GRB_COORDS;
		
//		System.out.println(TYPE_MACRO_NAME);
		System.out.println(xx);
		System.out.println(TYPE_MACRO_NAME.GRB_COORDS);
		System.out.println(TYPE_MACRO_NAME.GRB_COORDS.getValue());
		System.out.println(TYPE_MACRO_NAME.GRB_COORDS.valueOf(1));
	}
	
	@org.junit.Test
	public void testTime(){

		
			String Event_SOD = "74986.92";
			String Event_TJD = "17524";
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
//			calendar.add(Calendar.SECOND, Integer.parseInt(Event_SOD));
			
			double jd = Double.parseDouble(Event_TJD)+ 2440000.5;
			AstroDate timeJD=new AstroDate(jd); //TJD = JD - 2440000.5;         % Truncated JD, Introduced by NASA in 1979
			int sod_MILLISECOND=(int) (Double.parseDouble(Event_SOD)*1000);
			//好像GregorianCalendar 对时区的处理有问题或者我的使用方式有问题，这里用两个变量表示GMT+0 和GMT+8,无法设置toString的时区
			//而是使用了默认时区
			// may be i use it in a wrong way , GMT+0 and GMT+8 
			//Bug toString() looks like always use default TimeZone
			GregorianCalendar gcaGMT0=timeJD.toGCalendar();
			GregorianCalendar gcaGMT8=timeJD.toGCalendar();
			gcaGMT0.add(GregorianCalendar.MILLISECOND,sod_MILLISECOND );
			gcaGMT8.add(GregorianCalendar.MILLISECOND,sod_MILLISECOND );
			gcaGMT8.add(GregorianCalendar.HOUR,8 ); 
			SimpleDateFormat longDateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			SimpleDateFormat longDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat longDateFormatTime = new SimpleDateFormat("HH:mm:ss:SSS");
			
			String EVENT_TIME ="GRB_DATE     :"+ Event_TJD+" (TJD)\t"+jd + "(JD)\t" +gcaGMT0.get(GregorianCalendar.DAY_OF_YEAR)+" DOY   " 
					+longDateFormatDate.format(gcaGMT0.getTime())+"(UTC+0)  \t"+"\r\n"
					+"GRB_TIME     : "+Event_SOD+"(SOD)\t"+ longDateFormatTime.format(gcaGMT0.getTime())+"(UTC+0)"+"\r\n"
					+"北京时间      : "+longDateFormatDateTime.format(gcaGMT8.getTime())+"(UTC+8)" ;
			System.out.println(EVENT_TIME);
		
	}
}
