package ca.uwaterloo.y367zhang.a04;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class NewAccountActivity extends Activity {

	private static final int NETWORK_ERROR = 0;
	private static final int CREATE_SUCCESSFULLY = 1;
	private SkedulrModel model = SkedulrModel.getInstance();
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newaccount);

    }
    
    public void createAccount(View button){  
    	
    	final EditText usernameField = (EditText) findViewById(R.id.editTextUsername);  
    	String username = usernameField.getText().toString();  
    	  
    	final EditText passwordField = (EditText) findViewById(R.id.editTextPassword);  
    	String password = passwordField.getText().toString();  
    	
    	final EditText repeatPasswordField = (EditText) findViewById(R.id.editTextRepeatPassword);  
    	String repeatPassword = repeatPasswordField.getText().toString();  
    	
    	final EditText givenNameField = (EditText) findViewById(R.id.editTextGivenNames);  
    	String givenNames = givenNameField.getText().toString();
    	
    	final EditText surnameField = (EditText) findViewById(R.id.editTextSurname);  
    	String surname = surnameField.getText().toString();
    	
    	TextView createAccountError = (TextView) findViewById(R.id.createAccountError);
    	
    	
    	if (username.length()==0
    			&&
    		password.length()==0
    			&&
    		givenNames.length()==0
    			&&
    		surname.length()==0){
    		createAccountError.setText(R.string.emptyRegisterInfoError);
    	}
    	else if(username.length()<4){
    		createAccountError.setText(R.string.shortUsernameError);
    	}
    	else if(password.length()<4){
    		createAccountError.setText(R.string.shortPasswordError);
    	}
    	else if(givenNames.length()==0){
    		createAccountError.setText(R.string.emptyGivennamesError);
    	}
    	else if(surname.length()==0){
    		createAccountError.setText(R.string.emptySurnameError);
    	}
    	else if (!password.equals(repeatPassword)){
    		createAccountError.setText(R.string.confirmPasswordCreateAccountError);
    	}
    	
    	else{
	    	try {
	    		model.createAccount(username, password, surname, givenNames);
	    		createAccountError.setText("");
	    		showDialog(CREATE_SUCCESSFULLY);
	    		//Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
	            //startActivity(i);
	    	} catch (DuplicateAccountErrorModel e) {
	    		createAccountError.setText(R.string.duplicatedAccountError);
			} catch (HttpPostException e) {
				showDialog(NETWORK_ERROR);
			} catch (AccountCreationErrorModel e) {
				createAccountError.setText(R.string.invalidPersonalInfoError);
			}
    	}
    }
    
	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case NETWORK_ERROR:
			return new AlertDialog.Builder(NewAccountActivity.this)
			.setTitle(R.string.networkError)
			.setPositiveButton(R.string.turnOnWifi, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
					dialog.cancel();
				}
			})
			.setNegativeButton(R.string.Exit, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.create();
		case CREATE_SUCCESSFULLY:
			return new AlertDialog.Builder(NewAccountActivity.this)
			.setTitle(R.string.accountCreated)
			.setMessage(R.string.loginChoice)
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					finish();
				}
			})
			.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					final EditText usernameField = (EditText) findViewById(R.id.editTextUsername);  
			    	usernameField.setText("");
			    	  
			    	final EditText passwordField = (EditText) findViewById(R.id.editTextPassword);  
			    	passwordField.setText("");
			    	
			    	final EditText repeatPasswordField = (EditText) findViewById(R.id.editTextRepeatPassword);  
			    	repeatPasswordField.setText("");
			    	
			    	final EditText givenNameField = (EditText) findViewById(R.id.editTextGivenNames);  
			    	givenNameField.setText("");
			    	
			    	final EditText surnameField = (EditText) findViewById(R.id.editTextSurname);  
			    	surnameField.setText("");
			    	
			    	dialog.cancel();
				}
			})
			.create();
		default:
			return null;
		}
	}
}