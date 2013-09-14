package skedulr;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class RegisteredCourseModel {
	private String subject;
	private String catalog;
	private String sec; 
	
	// Public Variables used in time conflicts handling
	public boolean[] days = new boolean[7];
	public String startTime;
	public String endTime;
	
	public String room;
	public String type;
	
	/** Construct a course from provided data.
	 * 
	 * @param sub  Subject (e.g. "CS")
	 * @param cat  Catalog number (e.g. "349" or "129R")
	 * @param sec  Catalog number (e.g. "001" or "002")
	 */
	public RegisteredCourseModel(String sub, String cat, String sec) {
		this.subject = sub;
		this.catalog = cat;
		this.sec= sec;
	}

	/** Get this course's subject. */
	public String getSubject() {
		return subject;
	}

	/** Get this course's catalog number. */
	public String getCatalog() {
		return catalog;
	}

	/** Get this course's sec number. */
	public String getSec() {
		return sec;
	}
	
	// Meaningful abbreviations for the days of the week in toString.
	public static String[] dayOfWeek= {"M", "T", "W", "Th", "F", "Sa", "Su"};
	
	/** Return a string representing this course. */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(this.subject + " " + this.catalog + " Section: " + this.sec + " Location: " + this.room + " Time: ");
		for(int i=0; i<this.days.length; i++) {
			if (this.days[i]) {
				sb.append(RegisteredCourseModel.dayOfWeek[i]);
			}
		}
		sb.append("\t" + this.startTime + "-" + this.endTime);
		return sb.toString();
	}
	

	/**
	 * Read the registered courses from the web service and parse it 
	 * into a list of GetCourse objects.
	 * @return
	 */
	public static List<RegisteredCourseModel> getCourses(String userID, String password) {
		List<RegisteredCourseModel> lst = new LinkedList<RegisteredCourseModel>();
		
		//get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
		try {

			//get a new instance of parser
			SAXParser sp = spf.newSAXParser();

			//parse the input and also register a private class for call backs
            String url = "http://anlujo.cs.uwaterloo.ca/cs349/getCourses.py?user_id=" + userID + "&passwd=" + password;
			sp.parse(url, new GetCourseParserCallBacksModel( lst ));

		}catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		return lst;
	}
	
	
	
	public static void replaceCourses(String userID, 
									String password, 
									List<RegisteredCourseModel> lst) 
	throws HttpPostException{
			
			
		
			String url = "http://anlujo.cs.uwaterloo.ca/cs349/replaceCourses.py?user_id=" 
						+ userID+ "&passwd=" + password;
			
			for(RegisteredCourseModel c : lst) {
				 url = url + "&subject=" + c.subject + "&catalog=" + c.catalog + "&section=" + c.sec;
			}
			
			String result = HttpUtil.httpPost(
					url,
					new String[]{},
					new String[]{});

			if (Pattern.matches("<\\?.*\\?><status>OK</status>", result))
				return;
	}
}


class GetCourseParserCallBacksModel extends DefaultHandler {
	private List<RegisteredCourseModel> lst;
	
	private String tmpVal;
	
	// values to add to the course currently being parsed.
	private String subject;
	private String catalog;
	private String sec;
	
	
	public GetCourseParserCallBacksModel(List<RegisteredCourseModel> lst) {
		this.lst = lst;
	}
	
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		this.tmpVal = new String(ch, start, length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("course")) {
			this.lst.add(new RegisteredCourseModel(this.subject, this.catalog, this.sec)); 
		} else if (qName.equalsIgnoreCase("subject")) {
			this.subject = this.tmpVal;
		} else if (qName.equalsIgnoreCase("catalog")) {
			this.catalog = this.tmpVal;
		} else if (qName.equalsIgnoreCase("section")) {
			this.sec = this.tmpVal;
		}
	}
}

/**
 * An exception for signaling attempts to create a duplicate account.
 */
class MoreThanFiveCoursesErrorModel extends Exception {
	public MoreThanFiveCoursesErrorModel(String msg) {
		super(msg);
	}
}

