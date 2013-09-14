package skedulr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A user account for the skedulr web service.
 *
 */
public class Account {

	final String userId;
	final String password;
	final String surname;
	final String givenNames;

	/**
	 * Construct an account based on data already in the database.
	 * 
	 * @param user
	 * @param passwd
	 */
	public Account(String user, String passwd) {
		this.userId = user;
		this.password = passwd;
		
		// Work to do here!
		this.surname = "";
		this.givenNames = "";

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
	 */
	public Account(String user, String passwd, String surname, String givenNames)
			throws DuplicateAccountError, HttpPostException,
			AccountCreationError {
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
			throw new DuplicateAccountError(m.group(1));
		else
			throw new AccountCreationError(result);
	}

}

/**
 * An exception for signaling attempts to create a duplicate account.
 */
class DuplicateAccountError extends Exception {
	public DuplicateAccountError(String msg) {
		super(msg);
	}
}
/**
 * An exception for signaling a general problem with creating an account.
 */
class AccountCreationError extends Exception {
	public AccountCreationError(String msg) {
		super(msg);
	}
}
