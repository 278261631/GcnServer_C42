package gcn;

import org.apache.commons.configuration2.XMLConfiguration;

public class GcnMailNoticeConverter {

	public static String convertXmlToGcnNotice(XMLConfiguration config,String gcnTitle){
		StringBuilder gcnNoticeStringBuilder=new StringBuilder();
		gcnNoticeStringBuilder.append("TITLE:").append("\t\t\t").append("GCN/").append(gcnTitle).append("\tNotice").append("\r\n");
		String stringRa = config.getString("WhereWhen.ObsDataLocation.ObservationLocation.AstroCoords.Position2D.Value2.C1");
		return gcnNoticeStringBuilder.toString();
	}
}
