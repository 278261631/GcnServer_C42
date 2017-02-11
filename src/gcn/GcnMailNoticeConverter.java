package gcn;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.configuration2.XMLConfiguration;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.GJChronology;

import com.mhuss.AstroLib.AstroDate;

import jsky.coords.WorldCoords;
import util.XML2File;

public class GcnMailNoticeConverter {

	public static String convertXmlToGcnNotice(XMLConfiguration config,String gcnTitle){
		String Tab3append="                ";
		String Tab1append="  ";
		String NewLineAppend= "<br/>";
		String preHead= "<pre>";
		String preEnd= "</pre>";
		String slashSep= "///////////////////////////////////////////////////////////";
		
		
		StringBuilder gcnNoticeStringBuilder=new StringBuilder();
		
		List<Object> whatNameList = config.getList("What.Param[@name]");
		List<Object> whatValueList=config.getList("What.Param[@value]");	
		List<Object> Trigger_ID_groupValue=config.getList("What.Group(0).Param[@value]");	
		List<Object> Obs_groupValue=config.getList("What.Group(2).Param[@value]");	 //Obs_Support_Info_groupValue

//		List<Object> whatGroup=config.get("What.Group[@name]");	
//		String TrigID = config.getString("What.Param.Pkt_Ser_Num.value");
		
		String TrigID="无TrigID ";
		if (whatNameList.indexOf("TrigID")>=0) {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));	
		}else {		}
		
		
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		String RADec_valid= Trigger_ID_groupValue.get(2).toString();
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String isTestNews=  Trigger_ID_groupValue.get(1).toString();
//		boolean isTestNews=  config.getBoolean("What.Group(0).Param[@value](1)");
		String isTestNewsStr= isTestNews.toUpperCase().equals("TRUE")?"TEST":"REAL";
		boolean isRADec_valid=RADec_valid.toUpperCase().equals("TRUE")?true:false;

		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		String Event_Ra_J2000;
		String Event_Ra_Current;
		String Event_Ra_J1950;
		String Event_Dec_J2000;
		String Event_Dec_Current;
		String Event_Dec_J1950;
		String Error2Radius = "--" ;
		Error2Radius = config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Error2Radius");
		if (isRADec_valid) {
			String EVENT_RA = config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Value2.C1");
			String EVENT_DEC = config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Value2.C2");
			Calendar caNow=Calendar.getInstance();
			WorldCoords wcRaDec=new WorldCoords(Double.parseDouble(EVENT_RA), Double.parseDouble(EVENT_DEC),2000.);
			double[] arCurrent= wcRaDec.getRaDec(caNow.get(Calendar.YEAR));
			double[] ar1950= wcRaDec.getRaDec(1950);
			WorldCoords wcRaDecCurrent = new WorldCoords(arCurrent[0],arCurrent[1]);
			WorldCoords wcRaDec1950 = new WorldCoords(ar1950[0], ar1950[1]);
			
			
			 Event_Ra_J2000=wcRaDec.getRaDeg()+"d\t{"+wcRaDec.getRA().toString()+"}\t(J2000)";
			 Event_Ra_Current=wcRaDecCurrent.getRaDeg()+"d\t{"+wcRaDecCurrent.getRA().toString()+"}\t(current)";
			 Event_Ra_J1950=wcRaDec1950.getRaDeg()+"d\t{"+wcRaDec1950.getRA().toString()+"}\t(1950)";
			
			 Event_Dec_J2000=wcRaDec.getDecDeg()+"d\t{"+wcRaDec.getDec().toString()+"}\t(J2000)";
			 Event_Dec_Current=wcRaDecCurrent.getDecDeg()+"d\t{"+wcRaDecCurrent.getDec().toString()+"}\t(current)";
			 Event_Dec_J1950=wcRaDec1950.getDecDeg()+"d\t{"+wcRaDec1950.getDec().toString()+"}\t(1950)";
			
			
			
		}else {
			
			 Event_Ra_J2000="Undefined"+"\t(J2000)";
			 Event_Ra_Current="Undefined"+"\t(current)";
			 Event_Ra_J1950="Undefined"+"\t(1950)";
			
			 Event_Dec_J2000="Undefined"+"\t(J2000)";
			 Event_Dec_Current="Undefined"+"\t(current)";
			 Event_Dec_J1950="Undefined"+"\t(1950)";
			
		}
		
		String Event_SOD = (String) whatValueList.get(whatNameList.indexOf("Event_SOD"));
		String Event_TJD = (String) whatValueList.get(whatNameList.indexOf("Event_TJD"));
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
//		calendar.add(Calendar.SECOND, Integer.parseInt(Event_SOD));
		
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
		SimpleDateFormat longDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		
		String EVENT_TIME = Event_TJD+" (TJD)\t"+jd + "(JD)\t" +Event_SOD+"(SOD)\t"
				+longDateFormat.format(gcaGMT0.getTime())+"(UTC+0)  \t"
				+ longDateFormat.format(gcaGMT8.getTime())+"(UTC+8)";
		String Sun_RA = Obs_groupValue.get(0).toString();
		String Sun_DEC = Obs_groupValue.get(1).toString();
		String Moon_RA = Obs_groupValue.get(4).toString();
		String Moon_DEC = Obs_groupValue.get(5).toString();
		WorldCoords sun_wcRaDec=new WorldCoords(Double.parseDouble(Sun_RA), Double.parseDouble(Sun_DEC),2000.);
		WorldCoords moon_wcRaDec=new WorldCoords(Double.parseDouble(Moon_RA), Double.parseDouble(Moon_DEC),2000.);
		String MOON_ILLUM = Obs_groupValue.get(7).toString();
		String Galactic_Long = Obs_groupValue.get(8).toString();
		String Galactic_Lat = Obs_groupValue.get(9).toString();
		String Ecliptic_Long = Obs_groupValue.get(10).toString();
		String Ecliptic_Lat = Obs_groupValue.get(11).toString();
		
		String Sun_Distance = "???计算方法";
		String Moon_Distance = "???计算方法";
		
		String EVENT_DATE = "???格式不明 并且与上边重复";
		String EVENT_DUR = "???说明中未找到";
		String EXPT = "部分信息可能不是从XML中获取到的";
		
		String SUN_POSTN = sun_wcRaDec.getRaDeg()+"d {"+sun_wcRaDec.getRA()+"}    "+sun_wcRaDec.getDecDeg() +"d {"+sun_wcRaDec.getDec()+"}";
		String SUN_DIST = Sun_Distance;
		String MOON_POSTN = moon_wcRaDec.getRaDeg()+"d {"+moon_wcRaDec.getRA()+"}    "+moon_wcRaDec.getDecDeg() +"d {"+moon_wcRaDec.getDec()+"}";
		String MOON_DIST = Moon_Distance;
		String MOON_ILLUM_str=MOON_ILLUM+ "[%]";
		String GAL_COORDS =Galactic_Long+","+Galactic_Lat+ " [deg] galactic lon,lat of the event 有数据但是样例中显示 Undefined";
		String ECL_COORDS =Ecliptic_Long+","+Ecliptic_Lat+ " [deg] ecliptic lon,lat of the event有数据但是样例中显示 Undefined";
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		String COMMENTS_2 = "部分信息可能不是从XML中获取到的";
		String COMMENTS_3 = "部分信息可能不是从XML中获取到的";
		String COMMENTS_4 = "部分信息可能不是从XML中获取到的";
		String COMMENTS_5 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
//		gcnNoticeStringBuilder.append("<table border=\"0\">");
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE        :").append(Tab3append).append("GCN/").append(gcnTitle).append("\tNotice ???").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE  :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE  :").append(Tab3append).append(isTestNewsStr).append(Tab1append)
		.append(NOTICE_TYPE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM  :").append(Tab3append).append(TrigID).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_RA     :").append(Tab3append).append(Event_Ra_J2000).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              ").append(Tab3append).append(Event_Ra_Current).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              ").append(Tab3append).append(Event_Ra_J1950).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_DEC    :").append(Tab3append).append(Event_Dec_J2000).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              ").append(Tab3append).append(Event_Dec_Current).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              ").append(Tab3append).append(Event_Dec_J1950).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_ERROR  :").append(Tab3append).append(Error2Radius).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_FLUENCE:").append(Tab3append).append(EVENT_FLUENCE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_TIME   :").append(Tab3append).append(EVENT_TIME).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_DATE   :").append(Tab3append).append(EVENT_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EVENT_DUR    :").append(Tab3append).append(EVENT_DUR).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EXPT         :").append(Tab3append).append(Tab1append).append(EXPT).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_POSTN    :").append(Tab3append).append(SUN_POSTN).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST     :").append(Tab3append).append(SUN_DIST).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN   :").append(Tab3append).append(MOON_POSTN).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST    :").append(Tab3append).append(MOON_DIST).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS   :").append(Tab3append).append(GAL_COORDS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS   :").append(Tab3append).append(ECL_COORDS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS     :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS     :").append(Tab3append).append(COMMENTS_2).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS     :").append(Tab3append).append(COMMENTS_3).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS     :").append(Tab3append).append(COMMENTS_4).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS     :").append(Tab3append).append(COMMENTS_5).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("北京时间:").append(Tab3append).append("__").append(NewLineAppend);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
//		gcnNoticeStringBuilder.append("</table >");
		
		String stringRa = config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Value2.C1");

		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
}
