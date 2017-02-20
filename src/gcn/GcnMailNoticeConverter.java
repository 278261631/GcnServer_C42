package gcn;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.XMLConfiguration;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.chrono.GJChronology;

import com.mhuss.AstroLib.AstroDate;
import com.sun.mail.handlers.image_gif;

import jsky.coords.WorldCoords;
import util.XML2File;

public class GcnMailNoticeConverter {
	static String Tab3append="         ";
	static String Tab1append="  ";
	static String NewLineAppend= "<br/>";
	static String preHead= "<pre>";
	static String preEnd= "</pre>";
	static String slashSep= "///////////////////////////////////////////////////////////";

	//SNEWS 
	static String convertXmlToGcnNotice(XMLConfiguration config,String gcnTitle){
		
		
		StringBuilder gcnNoticeStringBuilder=new StringBuilder();
		
		List<Object> whatNameList = config.getList("What.Param[@name]");
		List<Object> whatValueList=config.getList("What.Param[@value]");	
		List<Object> Trigger_ID_groupValue=config.getList("What.Group(0).Param[@value]");	
		List<Object> Obs_groupValue=config.getList("What.Group(2).Param[@value]");	 //Obs_Support_Info_groupValue
		List<Object> Obs_groupName=config.getList("What.Group(2).Param[@name]");	 //Obs_Support_Info_groupName

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
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	//All types 
	static String convertXmlToGcnNotice_All(XMLConfiguration config,String gcnTitle){
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
		
		
		
		String NOTICE_DATE = config.getString("Who.Date","---");
		String NOTICE_TYPE="---";
		try {
			NOTICE_TYPE = Trigger_ID_groupValue.get(0).toString();
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}
		String RADec_valid="---";
		try {
			RADec_valid = Trigger_ID_groupValue.get(2).toString();
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String isTestNews="---";
		try {
			isTestNews = Trigger_ID_groupValue.get(1).toString();
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}
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
		
		
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	
	
	
	
	public static String convertXmlToGcnNotice_All(XMLConfiguration config,String gcnTitle_1 ,int typeNumber,String filePath){
		TYPE_MACRO_NAME type=TYPE_MACRO_NAME.valueOf(typeNumber);
		String messageContent="";
		String messageXmlContent=XML2File.readLogToString(filePath);
		messageXmlContent="<pre>"+messageXmlContent.replace(">", "&gt;").replace("<", "&lt;")+"</pre>";
		switch (type) {
		case SNEWS:
			messageContent = convertXmlToGcnNotice( config,gcnTitle_1);
			break;
		case SWIFT_BAT_GRB_ALERT:
			messageContent = convertXmlToGcnNotice_Swift_Alert( config, gcnTitle_1 , typeNumber);
			break;
		case SWIFT_BAT_GRB_POS_ACK:
			messageContent = convertXmlToGcnNotice_Swift_Pos_ACK( config, gcnTitle_1 , typeNumber);
			break;
		case SWIFT_BAT_GRB_LC:
			messageContent = convertXmlToGcnNotice_SWIFT_BAT_GRB_LC( config, gcnTitle_1 , typeNumber);
			break;
		case SWIFT_BAT_TRANS:
			messageContent = convertXmlToGcnNotice_SWIFT_BAT_TRANS( config, gcnTitle_1 , typeNumber);
			break;
		case SWIFT_UVOT_DBURST:
			messageContent = convertXmlToGcnNotice_SWIFT_UVOT_DBURST( config, gcnTitle_1 , typeNumber);
			break;
		case SWIFT_XRT_POSITION:
			messageContent = convertXmlToGcnNotice_SWIFT_XRT_POSITION( config, gcnTitle_1 , typeNumber);
			break;
		case SWIFT_XRT_IMAGE_PROC:
			messageContent = convertXmlToGcnNotice_SWIFT_XRT_IMAGE_PROC( config, gcnTitle_1 , typeNumber);
			break;
		case AMON_ICECUBE_COINC:
//			messageContent = convertXmlToGcnNotice_AMON_ICECUBE_COINC( config, gcnTitle_1 , typeNumber);
			break;
		case AMON_ICECUBE_HESE:
			messageContent = convertXmlToGcnNotice_AMON_ICECUBE_HESE( config, gcnTitle_1 , typeNumber);
			break;
		case AMON_ICECUBE_EHE:
//			messageContent = convertXmlToGcnNotice_AMON_ICECUBE_EHE( config, gcnTitle_1 , typeNumber); --未有测试数据
			break;
		default:
			XML2File.writeToLog("类型未格式化转换 ："+type);
			break;
		}
		messageContent=messageContent+"\r\n\r\n\r\n"+messageXmlContent;
		return messageContent;
	
	}


	private static String convertXmlToGcnNotice_AMON_ICECUBE_HESE(XMLConfiguration config, String gcnTitle_1,
			int typeNumber) {
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
		//AMON 没有这一段
//		List<Object> Obs_groupValue=config.getList("What.Group(3).Param[@value]");	 //Obs_Support_Info_groupValue
//		List<Object> Obs_groupName=config.getList("What.Group(3).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		String [] Event_DateTime=getTJD_SOD_Event(whatNameList,whatValueList);
		
		String SRC_ERROR      	="--";
		String SRC_ERROR50    	="--";
		String REVISION  		="--";
		String N_EVENTS  		="--";
		String STREAM    		="--";
		String DELTA_T   		="--";
		String SIGMA_T   		="--";
		String FALSE_POS 		="--";
		String PVALUE    		="--";
		String CHARGE    		="--";
		String SIGNAL_TRACKNESS ="--";
		try {
			
			SRC_ERROR      	=(String) whatValueList.get(whatNameList.indexOf("src_error_90"));
			SRC_ERROR50    	=(String) whatValueList.get(whatNameList.indexOf("src_error_50"));
			REVISION  		=(String) whatValueList.get(whatNameList.indexOf("Rev"));
			N_EVENTS  		=(String) whatValueList.get(whatNameList.indexOf("Nevents"));
			STREAM    		=(String) whatValueList.get(whatNameList.indexOf("Stream"));
			DELTA_T   		=(String) whatValueList.get(whatNameList.indexOf("deltaT"));
			SIGMA_T   		=(String) whatValueList.get(whatNameList.indexOf("sigmaT"));
			FALSE_POS 		=(String) whatValueList.get(whatNameList.indexOf("False_pos"));
			PVALUE    		=(String) whatValueList.get(whatNameList.indexOf("pvalue"));
			CHARGE    		=(String) whatValueList.get(whatNameList.indexOf("charge"));
			SIGNAL_TRACKNESS=(String) whatValueList.get(whatNameList.indexOf("signal_trackness"));
			

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		Map<String,String> raDec=getRaDec(config);

		
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE         :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE   :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE   :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM   :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_RA        :").append(Tab3append).append(raDec.get("Event_Ra_J2000")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_Current") ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_J1950")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DEC       :").append(Tab3append).append(raDec.get("Event_Dec_J2000")  ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_Current")).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_J1950")  ).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("SRC_ERROR      :").append(Tab3append).append(SRC_ERROR).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SRC_ERROR50    :").append(Tab3append).append(SRC_ERROR50).append(NewLineAppend);
		gcnNoticeStringBuilder.append("DISCOVERY_DATE :").append(Tab3append).append(Event_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("DISCOVERY_TIME :").append(Tab3append).append(Event_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(Event_DateTime[2]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("REVISION       :").append(Tab3append).append(REVISION).append(NewLineAppend);
		gcnNoticeStringBuilder.append("N_EVENTS       :").append(Tab3append).append(N_EVENTS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("STREAM         :").append(Tab3append).append(STREAM).append(NewLineAppend);
		gcnNoticeStringBuilder.append("DELTA_T        :").append(Tab3append).append(DELTA_T).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SIGMA_T        :").append(Tab3append).append(SIGMA_T).append(NewLineAppend);
		gcnNoticeStringBuilder.append("FALSE_POS      :").append(Tab3append).append(FALSE_POS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("PVALUE         :").append(Tab3append).append(PVALUE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("CHARGE         :").append(Tab3append).append(CHARGE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SIGNAL_TRACKNESS  :").append(Tab3append).append(SIGNAL_TRACKNESS).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append("没有Obs_Support_Info数据").append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append("没有Obs_Support_Info数据").append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append("没有Obs_Support_Info数据").append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append("没有Obs_Support_Info数据").append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append("没有Obs_Support_Info数据").append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append("没有Obs_Support_Info数据").append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		
		
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	static String convertXmlToGcnNotice_Swift_Alert(XMLConfiguration config,String gcnTitle_1 ,int typeNumber){
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
//		List<Object> Obs_groupValue=config.getList("What.Group(2).Param[@value]");	 //Obs_Support_Info_groupValue
		
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		
		String Trig_Index="无Trig_Index ";
		try {
			Trig_Index=(String) whatValueList.get(whatNameList.indexOf("Trig_Index"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String [] GRB_DateTime=getTJD_SOD(whatNameList,whatValueList);
		
		
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();

		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
				
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		

		String Rate_Signif="---";
		try {
			Rate_Signif = (String) whatValueList.get(whatNameList.indexOf("Rate_Signif"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
		}

		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE        :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE  :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE  :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM  :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_DATE     :").append(Tab3append).append(GRB_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_TIME     :").append(Tab3append).append(GRB_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间      :").append(Tab3append).append(GRB_DateTime[2]).append(NewLineAppend);
		
//		gcnNoticeStringBuilder.append(getTJD_SOD(whatNameList,whatValueList));

		gcnNoticeStringBuilder.append("TRIGGER_INDEX:").append(Tab3append).append(Trig_Index).append(NewLineAppend);
		gcnNoticeStringBuilder.append("Rate_Signif  :").append(Tab3append).append(Rate_Signif).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS     :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("北京时间:").append(Tab3append).append("__").append(NewLineAppend);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
//		gcnNoticeStringBuilder.append("</table >");
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	
	
	static String convertXmlToGcnNotice_Swift_Pos_ACK(XMLConfiguration config,String gcnTitle_1 ,int typeNumber){
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
		List<Object> Obs_groupValue=config.getList("What.Group(3).Param[@value]");	 //Obs_Support_Info_groupValue
		List<Object> Obs_groupName=config.getList("What.Group(3).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		
		String Trig_Index="无Trig_Index ";
		try {
			Trig_Index=(String) whatValueList.get(whatNameList.indexOf("Trig_Index"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String [] GRB_DateTime=getTJD_SOD(whatNameList,whatValueList);
		
		String Burst_Inten="--";
		String Bkg_Inten="--";
		String Bkg_Time="--";
		String Bkg_Dur="--";
		String Phi="--";
		String Theta="--";
		String Soln_Status="--";
		String Image_Signif="--";
		String MERIT_PARAMS="--";
		try {
			Burst_Inten = (String) whatValueList.get(whatNameList.indexOf("Burst_Inten"));
			Bkg_Inten = (String) whatValueList.get(whatNameList.indexOf("Bkg_Inten"));
			Bkg_Time = (String) whatValueList.get(whatNameList.indexOf("Bkg_Time"));
			Bkg_Dur = (String) whatValueList.get(whatNameList.indexOf("Bkg_Dur"));
			Phi = (String) whatValueList.get(whatNameList.indexOf("Phi"));
			Theta = (String) whatValueList.get(whatNameList.indexOf("Theta"));
			Soln_Status = (String) whatValueList.get(whatNameList.indexOf("Soln_Status"))+" 需要转换";
			Image_Signif = (String) whatValueList.get(whatNameList.indexOf("Image_Signif"));
			List<Object> Merit_groupValue=config.getList("What.Group(0).Param[@value]");
			MERIT_PARAMS=getMERIT_PARAMS(Merit_groupValue);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		Map<String,String> raDec=getRaDec(config);
		String Error2Radius= config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Error2Radius","--");
		String Rate_Signif="---";
		try {
			Rate_Signif = (String) whatValueList.get(whatNameList.indexOf("Rate_Signif"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
		}
		
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE         :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE   :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE   :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM   :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_RA        :").append(Tab3append).append(raDec.get("Event_Ra_J2000")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_Current") ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_J1950")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DEC       :").append(Tab3append).append(raDec.get("Event_Dec_J2000")  ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_Current")).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_J1950")  ).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_ERROR      :").append(Tab3append).append(Error2Radius).append("???").append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_INTEN      :").append(Tab3append).append(Burst_Inten).append(NewLineAppend);
		gcnNoticeStringBuilder.append("BKG_INTEN      :").append(Tab3append).append(Bkg_Inten).append(NewLineAppend);
		gcnNoticeStringBuilder.append("BKG_TIME       :").append(Tab3append).append(Bkg_Time).append(NewLineAppend);
		gcnNoticeStringBuilder.append("BKG_DUR        :").append(Tab3append).append(Bkg_Dur).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DATE       :").append(Tab3append).append(GRB_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_TIME       :").append(Tab3append).append(GRB_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(GRB_DateTime[2]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_PHI        :").append(Tab3append).append(Phi).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_THETA      :").append(Tab3append).append(Theta).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_INDEX  :").append(Tab3append).append(Trig_Index).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SOLN_STATUS    :").append(Tab3append).append(Soln_Status).append(NewLineAppend);
		gcnNoticeStringBuilder.append("RATE_SIGNIF    :").append(Tab3append).append(Rate_Signif).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMAGE_SIGNIF   :").append(Tab3append).append(Image_Signif).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MERIT_PARAMS   :").append(Tab3append).append(MERIT_PARAMS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append(getMOON_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append(getMOON_DIST(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append(getGAL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append(getECL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		
		
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	
	
	private static String convertXmlToGcnNotice_SWIFT_XRT_IMAGE_PROC(XMLConfiguration config, String gcnTitle_1,
			int typeNumber) {
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
		List<Object> Obs_groupValue=config.getList("What.Group(3).Param[@value]");	 //Obs_Support_Info_groupValue
		List<Object> Obs_groupName=config.getList("What.Group(3).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		
		String [] GRB_DateTime=getTJD_SOD(whatNameList,whatValueList);
		
		String Burst_Inten="--";
		
		String CENTRIOD_X="--";
		String CENTRIOD_Y="--";
		String ROLL="--";
		String GAIN="--";
		String MODE="--";
		String WAVEFORM="--";
		String EXPO_TIME="--";
		String GRB_POS_XRT_Y="--";
		String GRB_POS_XRT_Z="--";
		String IMAGE_URL="--";
		try {
			Burst_Inten = (String) whatValueList.get(whatNameList.indexOf("Burst_Inten"));
			
			CENTRIOD_X = (String) whatValueList.get(whatNameList.indexOf("Cent_X"));
			CENTRIOD_Y=(String) whatValueList.get(whatNameList.indexOf("Cent_Y"));
			ROLL=(String) whatValueList.get(whatNameList.indexOf("Image_roll"));
			GAIN=(String) whatValueList.get(whatNameList.indexOf("Gain"));
			MODE=(String) whatValueList.get(whatNameList.indexOf("Mode"));
			WAVEFORM=(String) whatValueList.get(whatNameList.indexOf("Waveform"));
			EXPO_TIME=(String) whatValueList.get(whatNameList.indexOf("Expo_Time"));
			GRB_POS_XRT_Y=(String) whatValueList.get(whatNameList.indexOf("GRB_in_XRT_X")) +" 应该是posX?";
			GRB_POS_XRT_Z=(String) whatValueList.get(whatNameList.indexOf("GRB_in_XRT_Y")) +" 应该是PosY？";
			IMAGE_URL=(String) whatValueList.get(whatNameList.indexOf("Image"));	
			IMAGE_URL = "<a href='"+IMAGE_URL+"'>"+IMAGE_URL +"</a>";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		Map<String,String> raDec=getRaDec(config);
		String Error2Radius= config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Error2Radius","--");

		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE         :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE   :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE   :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM   :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_RA        :").append(Tab3append).append(raDec.get("Event_Ra_J2000")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_Current") ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_J1950")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DEC       :").append(Tab3append).append(raDec.get("Event_Dec_J2000")  ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_Current")).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_J1950")  ).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_ERROR      :").append(Tab3append).append(Error2Radius).append("???").append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_INTEN      :").append(Tab3append).append(Burst_Inten).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMG_START_DATE :").append(Tab3append).append(GRB_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMG_START_TIME :").append(Tab3append).append(GRB_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(GRB_DateTime[2]).append(NewLineAppend);


		gcnNoticeStringBuilder.append("CENTRIOD_X     :").append(Tab3append).append(CENTRIOD_X).append(NewLineAppend);
		gcnNoticeStringBuilder.append("CENTRIOD_Y     :").append(Tab3append).append(CENTRIOD_Y).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ROLL           :").append(Tab3append).append(ROLL).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAIN           :").append(Tab3append).append(GAIN).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MODE           :").append(Tab3append).append(MODE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("WAVEFORM       :").append(Tab3append).append(WAVEFORM).append(NewLineAppend);
		gcnNoticeStringBuilder.append("EXPO_TIME      :").append(Tab3append).append(EXPO_TIME).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_POS_XRT_Y  :").append(Tab3append).append(GRB_POS_XRT_Y).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_POS_XRT_Z  :").append(Tab3append).append(GRB_POS_XRT_Z).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMAGE_URL      :").append(Tab3append).append(IMAGE_URL).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("BKG_INTEN      :").append(Tab3append).append(Bkg_Inten).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("BKG_TIME       :").append(Tab3append).append(Bkg_Time).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("BKG_DUR        :").append(Tab3append).append(Bkg_Dur).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("GRB_PHI        :").append(Tab3append).append(Phi).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("GRB_THETA      :").append(Tab3append).append(Theta).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("TRIGGER_INDEX  :").append(Tab3append).append(Trig_Index).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("SOLN_STATUS    :").append(Tab3append).append(Soln_Status).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("RATE_SIGNIF    :").append(Tab3append).append(Rate_Signif).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("IMAGE_SIGNIF   :").append(Tab3append).append(Image_Signif).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("MERIT_PARAMS   :").append(Tab3append).append(MERIT_PARAMS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append(getMOON_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append(getMOON_DIST(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append(getGAL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append(getECL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		
		
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	private static String convertXmlToGcnNotice_SWIFT_XRT_POSITION(XMLConfiguration config, String gcnTitle_1,
			int typeNumber) {
		String Tab3append="                ";
		String Tab1append="  ";
		String NewLineAppend= "<br/>";
		String preHead= "<pre>";
		String preEnd= "</pre>";
		String slashSep= "///////////////////////////////////////////////////////////";
		StringBuilder gcnNoticeStringBuilder=new StringBuilder();
		
		List<Object> whatNameList = config.getList("What.Param[@name]");
		List<Object> whatValueList=config.getList("What.Param[@value]");	
//		List<Object> Trigger_ID_groupValue=config.getList("What.Group(0).Param[@value]");	
		List<Object> Obs_groupValue=config.getList("What.Group(3).Param[@value]");	 //Obs_Support_Info_groupValue
		List<Object> Obs_groupName=config.getList("What.Group(3).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
			
		String [] GRB_DateTime=getTJD_SOD(whatNameList,whatValueList);
		
		String X_TAM_i1="--";
		String Y_TAM_i1="--";
		String X_TAM_i2="--";
		String Y_TAM_i2="--";
		String Amplifier="--";
		String Waveform="--";
		String GRB_SIGNIF="--";
		String Burst_Inten="--";
		
		try {
			X_TAM_i1 = (String) whatValueList.get(whatNameList.indexOf("X_TAM_i1"));
			Y_TAM_i1 = (String) whatValueList.get(whatNameList.indexOf("Y_TAM_i1"));
			X_TAM_i2 = (String) whatValueList.get(whatNameList.indexOf("X_TAM_i2"));
			Y_TAM_i2 = (String) whatValueList.get(whatNameList.indexOf("Y_TAM_i2"));
			Amplifier = (String) whatValueList.get(whatNameList.indexOf("Amplifier"));
			Waveform = (String) whatValueList.get(whatNameList.indexOf("Waveform"));
			GRB_SIGNIF = (String) whatValueList.get(whatNameList.indexOf("GRB_SIGNIF"));
			Burst_Inten = (String) whatValueList.get(whatNameList.indexOf("Burst_Inten"));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
//		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
//		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
//		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		Map<String,String> raDec=getRaDec(config);
		String Error2Radius= config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Error2Radius","--");
		
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE         :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE   :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE   :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM   :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_RA        :").append(Tab3append).append(raDec.get("Event_Ra_J2000")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_Current") ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_J1950")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DEC       :").append(Tab3append).append(raDec.get("Event_Dec_J2000")  ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_Current")).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_J1950")  ).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_ERROR      :").append(Tab3append).append(Error2Radius).append("???").append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_INTEN      :").append(Tab3append).append(Burst_Inten).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_SIGNIF     :").append(Tab3append).append(GRB_SIGNIF).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("IMG_START_DATE :").append(Tab3append).append(GRB_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMG_START_TIME :").append(Tab3append).append(GRB_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(GRB_DateTime[2]).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("TAM[0-3]       :").append(Tab3append).append(X_TAM_i1).append(Tab1append).append(Y_TAM_i1).append(Tab1append).
				append(X_TAM_i2).append(Tab1append).append(Y_TAM_i2).append(Tab1append).append(NewLineAppend);
		gcnNoticeStringBuilder.append("AMPLIFIER      :").append(Tab3append).append(Amplifier).append(NewLineAppend);
		gcnNoticeStringBuilder.append("WAVEFORM       :").append(Tab3append).append(Waveform).append(NewLineAppend);
		
//		gcnNoticeStringBuilder.append("GRB_PHI        :").append(Tab3append).append(Phi).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("GRB_THETA      :").append(Tab3append).append(Theta).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("TRIGGER_INDEX  :").append(Tab3append).append(Trig_Index).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("SOLN_STATUS    :").append(Tab3append).append(Soln_Status).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("RATE_SIGNIF    :").append(Tab3append).append(Rate_Signif).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("IMAGE_SIGNIF   :").append(Tab3append).append(Image_Signif).append(NewLineAppend);
//		gcnNoticeStringBuilder.append("MERIT_PARAMS   :").append(Tab3append).append(MERIT_PARAMS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append(getMOON_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append(getMOON_DIST(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append(getGAL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append(getECL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		
		
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	
	private static String convertXmlToGcnNotice_SWIFT_UVOT_DBURST(XMLConfiguration config, String gcnTitle_1,
			int typeNumber) {
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
		List<Object> Obs_groupValue=config.getList("What.Group(3).Param[@value]");	 //Obs_Support_Info_groupValue
		List<Object> Obs_groupName=config.getList("What.Group(3).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		
		String [] ExpoStart_DateTime=getExpoStartTJD_SOD(whatNameList,whatValueList);
		
		String Point_RA="--";
		String Point_Dec="--";
		String Point_Roll="--";
		String Filter="--";
		String X_OFFSET_X0="--";
		String Y_OFFSET_Y0="--";
		String X_MAX_Width="--";
		String Y_MAX_Height="--";
		String Image_URL="--";

		try {
			Point_RA = (String) whatValueList.get(whatNameList.indexOf("Point_RA"));
			Point_Dec = (String) whatValueList.get(whatNameList.indexOf("Point_Dec"));
			Point_Roll = (String) whatValueList.get(whatNameList.indexOf("Point_Roll"));
			Filter = (String) whatValueList.get(whatNameList.indexOf("Filter"));
			X_OFFSET_X0 = (String) whatValueList.get(whatNameList.indexOf("X0"));
			Y_OFFSET_Y0 = (String) whatValueList.get(whatNameList.indexOf("Y0"));
			X_MAX_Width = (String) whatValueList.get(whatNameList.indexOf("Width"));
			Y_MAX_Height = (String) whatValueList.get(whatNameList.indexOf("Height"));
			Image_URL = (String) whatValueList.get(whatNameList.indexOf("Image_URL"));
			Image_URL = "<a href='"+Image_URL+"'>"+Image_URL +"</a>";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE         :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE   :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE   :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM   :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		gcnNoticeStringBuilder.append("Point_RA   :").append(Tab3append).append(Point_RA).append(NewLineAppend);
		gcnNoticeStringBuilder.append("Point_Dec   :").append(Tab3append).append(Point_Dec).append(NewLineAppend);
		gcnNoticeStringBuilder.append("Point_Roll   :").append(Tab3append).append(Point_Roll).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("IMG_START_DATE :").append(Tab3append).append(ExpoStart_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMG_START_TIME :").append(Tab3append).append(ExpoStart_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(ExpoStart_DateTime[2]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("FILTER         :").append(Tab3append).append(Filter).append(" ??滤镜名字??").append(NewLineAppend);
		gcnNoticeStringBuilder.append("X_OFFSET       :").append(Tab3append).append(X_OFFSET_X0).append(" ?X0").append(NewLineAppend);
		gcnNoticeStringBuilder.append("Y_OFFSET       :").append(Tab3append).append(Y_OFFSET_Y0).append(" ?Y0").append(NewLineAppend);
		gcnNoticeStringBuilder.append("X_MAX          :").append(Tab3append).append(X_MAX_Width).append(" ?Width").append(NewLineAppend);
		gcnNoticeStringBuilder.append("Y_MAX          :").append(Tab3append).append(Y_MAX_Height).append(" ?Height").append(NewLineAppend);
		gcnNoticeStringBuilder.append("IM_URL         :").append(Tab3append).append(Image_URL).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append(getMOON_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append(getMOON_DIST(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append(getGAL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append(getECL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);

		
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	
	private static String convertXmlToGcnNotice_SWIFT_BAT_TRANS(XMLConfiguration config, String gcnTitle_1,
			int typeNumber) {
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
		List<Object> Obs_groupValue=config.getList("What.Group(3).Param[@value]");	 //Obs_Support_Info_groupValue
		List<Object> Obs_groupName=config.getList("What.Group(3).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		
		String Trig_Index="无Trig_Index ";
		try {
			Trig_Index=(String) whatValueList.get(whatNameList.indexOf("Trig_Index"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String [] GRB_DateTime=getTJD_SOD_Trans(whatNameList,whatValueList);
		
		String Burst_Inten="--";
		String Bkg_Inten="--";
		String Bkg_Time="--";
		String Bkg_Dur="--";
		String Phi="--";
		String Theta="--";
		String Soln_Status="--";
		String Image_Signif="--";
		String MERIT_PARAMS="--";
		try {
			Burst_Inten = (String) whatValueList.get(whatNameList.indexOf("Trans_Inten"));
			Bkg_Inten = (String) whatValueList.get(whatNameList.indexOf("Bkg_Inten"));
			Bkg_Time = (String) whatValueList.get(whatNameList.indexOf("Bkg_Time"));
			Bkg_Dur = (String) whatValueList.get(whatNameList.indexOf("Bkg_Dur"));
			Phi = (String) whatValueList.get(whatNameList.indexOf("Phi"));
			Theta = (String) whatValueList.get(whatNameList.indexOf("Theta"));
			Soln_Status = (String) whatValueList.get(whatNameList.indexOf("Soln_Status"))+" 需要转换";
			Image_Signif = (String) whatValueList.get(whatNameList.indexOf("Image_Signif"));
			List<Object> Merit_groupValue=config.getList("What.Group(0).Param[@value]");
			MERIT_PARAMS=getMERIT_PARAMS(Merit_groupValue);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		Map<String,String> raDec=getRaDec(config);
		String Error2Radius= config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Error2Radius","--");
		String Rate_Signif="---";
		try {
			Rate_Signif = (String) whatValueList.get(whatNameList.indexOf("Rate_Signif"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
		}
		
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE         :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE   :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE   :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM   :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_RA        :").append(Tab3append).append(raDec.get("Event_Ra_J2000")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_Current") ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Ra_J1950")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DEC       :").append(Tab3append).append(raDec.get("Event_Dec_J2000")  ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_Current")).append(NewLineAppend);
		gcnNoticeStringBuilder.append("              :").append(Tab3append).append(raDec.get("Event_Dec_J1950")  ).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_ERROR      :").append(Tab3append).append(Error2Radius).append("???").append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_INTEN      :").append(Tab3append).append(Burst_Inten).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_DUR    :").append(Tab3append).append("???xml中未找到??").append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_INDEX  :").append(Tab3append).append(Trig_Index).append(NewLineAppend);
		gcnNoticeStringBuilder.append("BKG_INTEN      :").append(Tab3append).append(Bkg_Inten).append(NewLineAppend);
		gcnNoticeStringBuilder.append("BKG_TIME       :").append(Tab3append).append(Bkg_Time).append(NewLineAppend);
		gcnNoticeStringBuilder.append("BKG_DUR        :").append(Tab3append).append(Bkg_Dur).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DATE       :").append(Tab3append).append(GRB_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_TIME       :").append(Tab3append).append(GRB_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(GRB_DateTime[2]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_PHI        :").append(Tab3append).append(Phi).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_THETA      :").append(Tab3append).append(Theta).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SOLN_STATUS    :").append(Tab3append).append(Soln_Status).append(NewLineAppend);
		gcnNoticeStringBuilder.append("RATE_SIGNIF    :").append(Tab3append).append(Rate_Signif).append(NewLineAppend);
		gcnNoticeStringBuilder.append("IMAGE_SIGNIF   :").append(Tab3append).append(Image_Signif).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MERIT_PARAMS   :").append(Tab3append).append(MERIT_PARAMS).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append(getMOON_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append(getMOON_DIST(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_ILLUM      :").append(Tab3append).append(getMoon_Illum(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append(getGAL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append(getECL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		
		
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
//		System.out.println("///////////////////////////////////////////////////////////");
		System.out.println(gcnNoticeStringBuilder.toString());
//		System.out.println("///////////////////////////////////////////////////////////");
		
		return gcnNoticeStringBuilder.toString();
	}
	
	
	static String convertXmlToGcnNotice_SWIFT_BAT_GRB_LC(XMLConfiguration config,String gcnTitle_1 ,int typeNumber){
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
		List<Object> Obs_groupName=config.getList("What.Group(2).Param[@name]");	 //Obs_Support_Info_groupName
		
		String TrigID="无TrigID ";		
		try {
			TrigID=(String) whatValueList.get(whatNameList.indexOf("TrigID"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String Segment_Num="无Segment_Num ";		
		try {
			Segment_Num=(String) whatValueList.get(whatNameList.indexOf("Segment_Num"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		
		
		String Trig_Index="无Trig_Index ";
		try {
			Trig_Index=(String) whatValueList.get(whatNameList.indexOf("Trig_Index"));
		} catch (Exception e) {
			XML2File.writeToLog(e.getMessage());
		}	
		String [] GRB_DateTime=getTJD_SOD(whatNameList,whatValueList);
		String Phi="--";
		String Theta="--";
		String Delta_Time="--";
		String Lightcurve_Link="--";
		String Lightcurve_Link_Jpeg="--";
		try {
			Phi = (String) whatValueList.get(whatNameList.indexOf("Phi"));
			Theta = (String) whatValueList.get(whatNameList.indexOf("Theta"));
//			List<Object> Merit_groupValue=config.getList("What.Group(0).Param[@value]");
			Delta_Time = (String) whatValueList.get(whatNameList.indexOf("Delta_Time"));
			Lightcurve_Link = (String) whatValueList.get(whatNameList.indexOf("Lightcurve_Link"));
			Lightcurve_Link_Jpeg = (String) whatValueList.get(whatNameList.indexOf("Lightcurve_Link_Jpeg"));
			Lightcurve_Link = "<a href='"+Lightcurve_Link+"'>"+Lightcurve_Link +"</a> 样例是 LC_URL";
			Lightcurve_Link_Jpeg = "<a href='"+Lightcurve_Link_Jpeg+"'>"+Lightcurve_Link_Jpeg +"</a> 样例是 LC_URL";
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String NOTICE_DATE = config.getString("Who.Date");
		String NOTICE_TYPE= Trigger_ID_groupValue.get(0).toString();
		
		NOTICE_TYPE=NOTICE_TYPE.equals("coinc")?"COINCIDENCE ???":NOTICE_TYPE;
		NOTICE_TYPE=NOTICE_TYPE.equals("indiv")?"INDIVIDUAL ???样例中无INDIVIDUAL缩写":NOTICE_TYPE; //???? not sure about indiv 
		
		String EVENT_FLUENCE="无EVENT_FLUENCE "; //EVENT_FLUENCE	 is	Event_Signal
		if (whatNameList.indexOf("Event_Signal")>=0) {
			EVENT_FLUENCE=(String) whatValueList.get(whatNameList.indexOf("Event_Signal"));	
		}else {		}
		EVENT_FLUENCE= EVENT_FLUENCE+ "[neutrinos]"+"???";
		
		Map<String,String> raDec=getRaDec(config);

		
		String COMMENTS_1 = "部分信息可能不是从XML中获取到的";
		
		gcnNoticeStringBuilder.append(preHead);
		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TITLE          :").append(Tab3append).append("GCN/").append(TYPE_MACRO_NAME.valueOf(typeNumber)).append("\tNotice ").append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_DATE    :").append(Tab3append).append(NOTICE_DATE).append(NewLineAppend);
		gcnNoticeStringBuilder.append("NOTICE_TYPE    :").append(Tab3append).append(TYPE_MACRO_NAME.valueOf(typeNumber)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("TRIGGER_NUM    :").append(Tab3append).append(TrigID).append(Tab1append).append("Seg_Num : ").append(Segment_Num).append(NewLineAppend);
		
		gcnNoticeStringBuilder.append("GRB_RA         :").append(Tab3append).append(raDec.get("Event_Ra_J2000")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("               :").append(Tab3append).append(raDec.get("Event_Ra_Current") ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("               :").append(Tab3append).append(raDec.get("Event_Ra_J1950")   ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DEC        :").append(Tab3append).append(raDec.get("Event_Dec_J2000")  ).append(NewLineAppend);
		gcnNoticeStringBuilder.append("               :").append(Tab3append).append(raDec.get("Event_Dec_Current")).append(NewLineAppend);
		gcnNoticeStringBuilder.append("               :").append(Tab3append).append(raDec.get("Event_Dec_J1950")  ).append(NewLineAppend);
		

		gcnNoticeStringBuilder.append("TRIGGER_INDEX  :").append(Tab3append).append(Trig_Index).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_DATE       :").append(Tab3append).append(GRB_DateTime[0]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_TIME       :").append(Tab3append).append(GRB_DateTime[1]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("北京时间        :").append(Tab3append).append(GRB_DateTime[2]).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_PHI        :").append(Tab3append).append(Phi).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GRB_THETA      :").append(Tab3append).append(Theta).append(NewLineAppend);
		gcnNoticeStringBuilder.append("DELTA_TIME     :").append(Tab3append).append(Delta_Time).append(NewLineAppend);
		gcnNoticeStringBuilder.append("LC_URL_LC_Link     :").append(Tab3append).append(Lightcurve_Link).append(NewLineAppend);
		gcnNoticeStringBuilder.append("LC_URL_LC_Link_Jpeg:").append(Tab3append).append(Lightcurve_Link_Jpeg).append(NewLineAppend);

		gcnNoticeStringBuilder.append("SUN_POSTN      :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("SUN_DIST       :").append(Tab3append).append(getSUN_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_POSTN     :").append(Tab3append).append(getMOON_POSTN(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_DIST      :").append(Tab3append).append(getMOON_DIST(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("MOON_ILLUM      :").append(Tab3append).append(getMoon_Illum(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("GAL_COORDS     :").append(Tab3append).append(getGAL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("ECL_COORDS     :").append(Tab3append).append(getECL_COORDS(Obs_groupValue, Obs_groupName)).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);
		gcnNoticeStringBuilder.append("COMMENTS       :").append(Tab3append).append(COMMENTS_1).append(NewLineAppend);

		gcnNoticeStringBuilder.append(slashSep).append(NewLineAppend);
		gcnNoticeStringBuilder.append(preEnd);
		
		System.out.println(gcnNoticeStringBuilder.toString());
		return gcnNoticeStringBuilder.toString();
	}
	
	
	
	private static String getMERIT_PARAMS(List<Object> merit_groupValue) {
		String result="";
		for (Object object : merit_groupValue) {
			result=result+object.toString();
		}
		return result;
	}
	public static String[] getTJD_SOD(List<Object> whatNameList,List<Object> whatValueList){
		String []EVENT_TIME=new String[3];
		try {
			String Event_SOD = (String) whatValueList.get(whatNameList.indexOf("Burst_SOD"));
			String Event_TJD = (String) whatValueList.get(whatNameList.indexOf("Burst_TJD"));
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
			SimpleDateFormat longDateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			SimpleDateFormat longDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat longDateFormatTime = new SimpleDateFormat("HH:mm:ss:SSS");
			
			EVENT_TIME[0] =Event_TJD+" (TJD)\t"+jd + "(JD)\t" +gcaGMT0.get(GregorianCalendar.DAY_OF_YEAR)+" DOY  " 
					+longDateFormatDate.format(gcaGMT0.getTime())+"(UTC+0)  ";
			EVENT_TIME[1] =Event_SOD+"(SOD)\t"+ longDateFormatTime.format(gcaGMT0.getTime())+"(UTC+0)";
			EVENT_TIME[2] =longDateFormatDateTime.format(gcaGMT8.getTime())+"(UTC+8)";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EVENT_TIME[0] ="--Error--";
			EVENT_TIME[1] ="--Error--";
			EVENT_TIME[2] ="--Error--";
			XML2File.writeToLog(e.getMessage());
		}
		return EVENT_TIME;
	}
	public static String[] getTJD_SOD_Event(List<Object> whatNameList,List<Object> whatValueList){
		String []EVENT_TIME=new String[3];
		try {
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
			SimpleDateFormat longDateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			SimpleDateFormat longDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat longDateFormatTime = new SimpleDateFormat("HH:mm:ss:SSS");
			
			EVENT_TIME[0] =Event_TJD+" (TJD)\t"+jd + "(JD)\t" +gcaGMT0.get(GregorianCalendar.DAY_OF_YEAR)+" DOY  " 
					+longDateFormatDate.format(gcaGMT0.getTime())+"(UTC+0)  ";
			EVENT_TIME[1] =Event_SOD+"(SOD)\t"+ longDateFormatTime.format(gcaGMT0.getTime())+"(UTC+0)";
			EVENT_TIME[2] =longDateFormatDateTime.format(gcaGMT8.getTime())+"(UTC+8)";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EVENT_TIME[0] ="--Error--";
			EVENT_TIME[1] ="--Error--";
			EVENT_TIME[2] ="--Error--";
			XML2File.writeToLog(e.getMessage());
		}
		return EVENT_TIME;
	}
	public static String[] getTJD_SOD_Trans(List<Object> whatNameList,List<Object> whatValueList){
		String []EVENT_TIME=new String[3];
		try {
			String Event_SOD = (String) whatValueList.get(whatNameList.indexOf("Trans_SOD"));
			String Event_TJD = (String) whatValueList.get(whatNameList.indexOf("Trans_TJD"));
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
			SimpleDateFormat longDateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			SimpleDateFormat longDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat longDateFormatTime = new SimpleDateFormat("HH:mm:ss:SSS");
			
			EVENT_TIME[0] =Event_TJD+" (TJD)\t"+jd + "(JD)\t" +gcaGMT0.get(GregorianCalendar.DAY_OF_YEAR)+" DOY  " 
					+longDateFormatDate.format(gcaGMT0.getTime())+"(UTC+0)  ";
			EVENT_TIME[1] =Event_SOD+"(SOD)\t"+ longDateFormatTime.format(gcaGMT0.getTime())+"(UTC+0)";
			EVENT_TIME[2] =longDateFormatDateTime.format(gcaGMT8.getTime())+"(UTC+8)";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EVENT_TIME[0] ="--Error--";
			EVENT_TIME[1] ="--Error--";
			EVENT_TIME[2] ="--Error--";
			XML2File.writeToLog(e.getMessage());
		}
		return EVENT_TIME;
	}
	
	public static String[] getExpoStartTJD_SOD(List<Object> whatNameList,List<Object> whatValueList){
		String []EVENT_TIME=new String[3];
		try {
			String Event_SOD = (String) whatValueList.get(whatNameList.indexOf("ExpoStart_TJD"));
			String Event_TJD = (String) whatValueList.get(whatNameList.indexOf("ExpoStart_SOD"));
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
			SimpleDateFormat longDateFormatDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
			SimpleDateFormat longDateFormatDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat longDateFormatTime = new SimpleDateFormat("HH:mm:ss:SSS");
			
			EVENT_TIME[0] =Event_TJD+" (TJD)\t"+jd + "(JD)\t" +gcaGMT0.get(GregorianCalendar.DAY_OF_YEAR)+" DOY  " 
					+longDateFormatDate.format(gcaGMT0.getTime())+"(UTC+0)  ";
			EVENT_TIME[1] =Event_SOD+"(SOD)\t"+ longDateFormatTime.format(gcaGMT0.getTime())+"(UTC+0)";
			EVENT_TIME[2] =longDateFormatDateTime.format(gcaGMT8.getTime())+"(UTC+8)";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EVENT_TIME[0] ="--Error--";
			EVENT_TIME[1] ="--Error--";
			EVENT_TIME[2] ="--Error--";
			XML2File.writeToLog(e.getMessage());
		}
		return EVENT_TIME;
	}
	
	
	static Map<String, String> getRaDec(XMLConfiguration config){
		Map <String,String> resultMap=new HashMap<String, String>();
		String Event_Ra_J2000="Undefined"+"\t(J2000)";
		String Event_Ra_Current="Undefined"+"\t(current)";
		String Event_Ra_J1950="Undefined"+"\t(1950)";
		String Event_Dec_J2000="Undefined"+"\t(J2000)";
		String Event_Dec_Current="Undefined"+"\t(current)";
		String Event_Dec_J1950="Undefined"+"\t(1950)";
		 
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
			
		 resultMap.put("Event_Ra_J2000", Event_Ra_J2000);
		 resultMap.put("Event_Ra_Current", Event_Ra_Current);
		 resultMap.put("Event_Ra_J1950", Event_Ra_J1950);
		 resultMap.put("Event_Dec_J2000", Event_Dec_J2000);
		 resultMap.put("Event_Dec_Current", Event_Dec_Current);
		 resultMap.put("Event_Dec_J1950", Event_Dec_J1950);
		return resultMap;
		 
	}

	static String getSUN_POSTN(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			String Sun_RA = Obs_groupValue.get(Obs_groupName.indexOf("Sun_RA")).toString();
			String Sun_DEC = Obs_groupValue.get(Obs_groupName.indexOf("Sun_Dec")).toString();
			WorldCoords sun_wcRaDec=new WorldCoords(Double.parseDouble(Sun_RA), Double.parseDouble(Sun_DEC),2000.);
			return  sun_wcRaDec.getRaDeg()+"d {"+sun_wcRaDec.getRA()+"}    "+sun_wcRaDec.getDecDeg() +"d {"+sun_wcRaDec.getDec()+"}";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}
	}
	static String getSUN_DIST(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			return Obs_groupValue.get(Obs_groupName.indexOf("Sun_Distance")).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}

	}
	static String getMOON_POSTN(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			String Moon_RA = Obs_groupValue.get(4).toString();
			String Moon_DEC = Obs_groupValue.get(5).toString();
			WorldCoords moon_wcRaDec=new WorldCoords(Double.parseDouble(Moon_RA), Double.parseDouble(Moon_DEC),2000.);
			return moon_wcRaDec.getRaDeg()+"d {"+moon_wcRaDec.getRA()+"}    "+moon_wcRaDec.getDecDeg() +"d {"+moon_wcRaDec.getDec()+"}";
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}
		
	}
	static String getMOON_DIST(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			return Obs_groupValue.get(Obs_groupName.indexOf("MOON_Distance")).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}
	}

	static String getMoon_Illum(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			return Obs_groupValue.get(Obs_groupName.indexOf("Moon_Illum")).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}
	}
	static String getGAL_COORDS(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			String Galactic_Long = Obs_groupValue.get(Obs_groupName.indexOf("Galactic_Long")).toString();
			String Galactic_Lat = Obs_groupValue.get(Obs_groupName.indexOf("Galactic_Lat")).toString();
			return Galactic_Long+","+Galactic_Lat+ " [deg] galactic lon,lat of the event";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}
	}
	static String getECL_COORDS(List<Object> Obs_groupValue,List<Object> Obs_groupName){
		try {
			String Ecliptic_Long = Obs_groupValue.get(Obs_groupName.indexOf("Ecliptic_Long")).toString();
			String Ecliptic_Lat = Obs_groupValue.get(Obs_groupName.indexOf("Ecliptic_Lat")).toString();
			return Ecliptic_Long+","+Ecliptic_Lat+ " [deg] ecliptic lon,lat of the event";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			XML2File.writeToLog(e.getMessage());
			return "--";
		}
		
	}
	
	

}
