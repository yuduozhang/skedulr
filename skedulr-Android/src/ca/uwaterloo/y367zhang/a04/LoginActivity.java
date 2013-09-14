package ca.uwaterloo.y367zhang.a04;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	private static final int NETWORK_ERROR = 0;
	private static final int DIALOG_SAVE_COURSE = 1;
	private static final int SAVE_DONE = 2;
	private static final int LOGOUT = 3;
	private static final int SAVE_DONE_BACK = 4;
	private static final int DIALOG_SAVE_COURSE_BACK = 5;
	private static final int LOGOUT_BACK = 6;
	private SkedulrModel model = SkedulrModel.getInstance();
	private ProgressDialog pdia;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button registerScreen = (Button) findViewById(R.id.buttonNewAccount);

		// Listening to register new account link
		registerScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), NewAccountActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Button loginButton = (Button) findViewById(R.id.buttonLogin);
		Button seeScheduleButton = (Button) findViewById(R.id.buttonSeeSchedule);
		Button newAccountButton = (Button) findViewById(R.id.buttonNewAccount);
		if(model.checkLogin()){
			loginButton.setText("Logout");
			seeScheduleButton.setVisibility(View.VISIBLE);
			newAccountButton.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK 
				&&
				event.getRepeatCount() == 0
		) {
			
			if(model.checkLogin() && model.scheduleChanged){
				showDialog(DIALOG_SAVE_COURSE_BACK);
			}
			else{
				showDialog(LOGOUT_BACK);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/*private class ProgressTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute(){ 
			super.onPreExecute();
			pdia = new ProgressDialog(LoginActivity.this);
			pdia.setMessage("Loading...");
			pdia.show();    
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			pdia.dismiss();
		}
	}*/

	/*private class LoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute(){ 
			super.onPreExecute();
			pdia = new ProgressDialog(LoginActivity.this);
			pdia.setMessage("Loading...");
			pdia.show();    
		}

		@Override
		protected void onPostExecute(Void result){
			super.onPostExecute(result);
			pdia.dismiss();
			Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
			startActivity(i);
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			final EditText usernameField = (EditText) findViewById(R.id.editTextUsername);  
			final String username = usernameField.getText().toString();  

			final EditText passwordField = (EditText) findViewById(R.id.editTextPassword);  
			final String password = passwordField.getText().toString();  

			final TextView loginError = (TextView) findViewById(R.id.loginError);

			try {
				model.login(username, password);
				//pd.dismiss();
				loginError.setText("");
			}
			catch (AccountInvalidErrorModel e) {
				//pd.dismiss();
				loginError.setText(R.string.invalidLoginError);

			} catch (AccountMissingUserIDErrorModel e) {
				//pd.dismiss();
				loginError.setText(R.string.usernameMissingLoginError);

			} catch (AccountMissingPaawdErrorModel e) {
				//pd.dismiss();
				loginError.setText(R.string.passwordMissingLoginError);

			} catch (HttpPostException e) {
				//pd.dismiss();
				showDialog(NETWORK_ERROR);
			}
			return null;
		}
	}*/

	public void login(View button){
		if (!model.checkLogin()){
			//new LoginTask().execute(null, null , null);
			final EditText usernameField = (EditText) findViewById(R.id.editTextUsername);  
			final String username = usernameField.getText().toString();  

			final EditText passwordField = (EditText) findViewById(R.id.editTextPassword);  
			final String password = passwordField.getText().toString();  

			final TextView loginError = (TextView) findViewById(R.id.loginError);

			try {
				//ProgressTask progressTask = new ProgressTask();
				//progressTask.execute(null,null,null);
				model.login(username, password);
				//progressTask.cancel(true);
				loginError.setText("");
				Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
				startActivity(i);
			}
			catch (AccountInvalidErrorModel e) {
				loginError.setText(R.string.invalidLoginError);

			} catch (AccountMissingUserIDErrorModel e) {
				loginError.setText(R.string.usernameMissingLoginError);

			} catch (AccountMissingPaawdErrorModel e) {

				loginError.setText(R.string.passwordMissingLoginError);

			} catch (HttpPostException e) {

				showDialog(NETWORK_ERROR);
			}
		}

		else {
			if (model.scheduleChanged) {
				showDialog(DIALOG_SAVE_COURSE);
			}
			else{
				showDialog(LOGOUT);
			}
		}
	}

	public void logoutBehavior(){
		model.logout();
		Button loginButton = (Button) findViewById(R.id.buttonLogin);
		Button seeScheduButton = (Button) findViewById(R.id.buttonSeeSchedule);
		Button newAccountButton = (Button) findViewById(R.id.buttonNewAccount);
		final EditText passwordField = (EditText) findViewById(R.id.editTextPassword);  

		seeScheduButton.setVisibility(View.GONE);
		newAccountButton.setVisibility(View.VISIBLE);

		passwordField.setText("");
		loginButton.setText("Login");
	}

	public void seeSchedule(View button){
		Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
		startActivity(i);
	}

	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case NETWORK_ERROR:
			return new AlertDialog.Builder(LoginActivity.this)
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
		case SAVE_DONE:
			return new AlertDialog.Builder(LoginActivity.this)
			.setTitle(R.string.saveDone)
			.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					logoutBehavior();
				}
			})
			.create();
		case SAVE_DONE_BACK:
			return new AlertDialog.Builder(LoginActivity.this)
			.setTitle(R.string.saveDone)
			.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					logoutBehavior();
					finish();
				}
			})
			.create();
		case DIALOG_SAVE_COURSE:
			return new AlertDialog.Builder(LoginActivity.this)
			.setMessage(R.string.saveCourse)
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					try {
						model.saveCourses();
						model.logout();
						showDialog(SAVE_DONE);
					} catch (HttpPostException e) {
						// TODO Auto-generated catch block
						showDialog(NETWORK_ERROR);
					}
				}
			})
			.setNeutralButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					logoutBehavior();

				}
			})
			.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
				}
			})
			.create();
		case DIALOG_SAVE_COURSE_BACK:
			return new AlertDialog.Builder(LoginActivity.this)
			.setMessage(R.string.saveCourseBack)
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					try {
						model.saveCourses();
						showDialog(SAVE_DONE_BACK);
					} catch (HttpPostException e) {
						// TODO Auto-generated catch block
						showDialog(NETWORK_ERROR);
					}
				}
			})
			.setNeutralButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					/* User clicked OK so do some stuff */
					logoutBehavior();
					finish();
				}
			})
			.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
				}
			})

			.create();
		case LOGOUT:
			return new AlertDialog.Builder(LoginActivity.this)
			.setTitle(R.string.Logout)
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					logoutBehavior();
				}
			})
			.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.create();
		case LOGOUT_BACK:
			return new AlertDialog.Builder(LoginActivity.this)
			.setTitle(R.string.LogoutBack)
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					logoutBehavior();
					finish();
				}
			})
			.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.create();
		default:
			return null;
		}
	}
}