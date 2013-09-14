package ca.uwaterloo.y367zhang.a04;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A user account for the skedulr web service.
 *
 */
public class AccountModel {

	public final String userId;
	public final String password;
	final String surname;
	final String givenNames;
	
	
	private Pattern accountInvalid = Pattern
	.compile("<\\?.*\\?><error>Invalid userid or password.</error>");
	private Pattern accountMissingUserID = Pattern
	.compile("<\\?.*\\?><error>Missing key: user_id</error>");
	private Pattern accounMissingPasswd = Pattern
	.compile("<\\?.*\\?><error>Missing key: passwd</error>");
	
	/**
	 * Construct an account based on data already in the database.
	 * 
	 * @param user
	 * @param passwd
	 * @throws HttpPostException 
	 * @throws AccountInvalidErrorModel 
	 * @throws AccountMissingUserIDErrorModel 
	 * @throws AccountMissingPaawdErrorModel 
	 */
	public AccountModel(String user, String passwd) throws 
				HttpPostException, 
				AccountInvalidErrorModel, 
				AccountMissingUserIDErrorModel, 
				AccountMissingPaawdErrorModel {
		this.userId = user;
		this.password = passwd;
		
		String[] accountInfo;
	    accountInfo = new String[3];
        //get a factory
        SAXParserFactory spf = SAXParserFactory.newInstance();
        try {

        	String result = HttpUtil.httpPost(
    				"http://anlujo.cs.uwaterloo.ca/cs349/getAccount.py",
    				new String[] { "user_id", "passwd"},
    				new String[] { user, passwd });
        	
        	
    		Matcher m = this.accountInvalid.matcher(result);
    		Matcher m2 = this.accountMissingUserID.matcher(result);
    		Matcher m3 = this.accounMissingPasswd.matcher(result);
    		
    		if (m.matches())
    			throw new AccountInvalidErrorModel(result);
    		else if (m2.matches())
    			throw new AccountMissingUserIDErrorModel(result);
    		else if (m3.matches())
    			throw new AccountMissingPaawdErrorModel(result);
    	        
            // get a new instance of parser
            SAXParser sp = spf.newSAXParser();
            String url = "http://anlujo.cs.uwaterloo.ca/cs349/getAccount.py?user_id=" + this.userId + "&passwd=" + this.password;
			//parse the input and also register a private class for call backs
			sp.parse(url, new AccountParserCallBacksModel( accountInfo ));
			
			//System.out.println(HttpUtil.httpPost(url, new String[] {}, new String[] {}));
        }catch(SAXException se) {
			se.printStackTrace();
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch (IOException ie) {
			ie.printStackTrace();
		}
		
		this.surname = accountInfo[1];
		this.givenNames = accountInfo[2];
	
		//System.out.println("  Surname debug = " + accountInfo[0]);
		//System.out.println("  Given debug  = " + accountInfo[1]);	
	}

	private Pattern accountExistsRE = Pattern
			.compile("<\\?.*\\?><error>Account for user_id ([a-zA-Z0-9]+) already exists.</error>");

	/**
	 * Construct an account that is not yet in the database.
	 * 
	 * @param userId
	 * @param passwd
	 * @param surname
	 * @param givenNames
	 * @throws HttpPostException 
	 */
	public AccountModel(String user, String passwd, String surname, String givenNames)
			throws DuplicateAccountErrorModel,
			AccountCreationErrorModel, HttpPostException {
		this.userId = user;
		this.password = passwd;
		this.surname = surname;
		this.givenNames = givenNames;
		
		String result = HttpUtil.httpPost(
				"http://anlujo.cs.uwaterloo.ca/cs349/createAccount.py",
				new String[] { "user_id", "passwd", "surname", "given_names" },
				new String[] { user, passwd, surname, givenNames });

		if (Pattern.matches("<\\?.*\\?><status>OK</status>", result))
			return;

		Matcher m = this.accountExistsRE.matcher(result);
		
		if (m.matches())
			throw new DuplicateAccountErrorModel(m.group(1));
		else
			throw new AccountCreationErrorModel(result);
	}
}



/**
 * An exception for signaling attempts to create a duplicate account.
 */
class DuplicateAccountErrorModel extends Exception {
	public DuplicateAccountErrorModel(String msg) {
		super(msg);
	}
}
/**
 * An exception for signaling a general problem with creating an account.
 */
class AccountCreationErrorModel extends Exception {
	public AccountCreationErrorModel(String msg) {
		super(msg);
	}
}
/**
 * An exception for signaling a general problem with creating an account.
 */
class AccountInvalidErrorModel extends Exception {
	public AccountInvalidErrorModel(String msg) {
		super(msg);
	}
}
/**
 * An exception for signaling a general problem with creating an account.
 */
class AccountMissingUserIDErrorModel extends Exception {
	public AccountMissingUserIDErrorModel(String msg) {
		super(msg);
	}
}
/**
 * An exception for signaling a general problem with creating an account.
 */
class AccountMissingPaawdErrorModel extends Exception {
	public AccountMissingPaawdErrorModel(String msg) {
		super(msg);
	}
}


/** 
 * A class providing the callbacks necessary to parse the list of
 * Account information.
 *
 */
class AccountParserCallBacksModel extends DefaultHandler {

    private String[] accountInfo;
	private String tmpVal;

	
	// values to add to the course currently being parsed.
	
	public AccountParserCallBacksModel(String[] accountInfo) {
        this.accountInfo = accountInfo; 
	}

    
	public void characters(char[] ch, int start, int length) throws SAXException {
		this.tmpVal = new String(ch, start, length);
	}
	
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("user_id")){
			this.accountInfo[0] = this.tmpVal;
		}
		else if (qName.equalsIgnoreCase("surname")) {
            this.accountInfo[1] = this.tmpVal;
		}
        else if (qName.equalsIgnoreCase("given_names")){
            this.accountInfo[2] = this.tmpVal;
        }
	}
}
