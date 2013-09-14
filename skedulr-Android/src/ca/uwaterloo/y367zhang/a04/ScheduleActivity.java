package ca.uwaterloo.y367zhang.a04;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class ScheduleActivity extends Activity {
	private static final int NETWORK_ERROR = 0;
	private static final int SAVE_DONE = 1;
	private static final int DIALOG_SAVE_COURSE = 2;
	private static final int SAVE_DONE_BACK = 3;
	private SkedulrModel model = SkedulrModel.getInstance();
	private List<RegisteredCourseModel> courses;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		updateCourseList();

		Button coursePickerScreen = (Button) findViewById(R.id.buttonAdd);

		// Listening to register new account link
		coursePickerScreen.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// Switching to Register screen
				Intent i = new Intent(getApplicationContext(), CoursePickerActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		updateCourseList();
	}
	
	
	public void updateCourseList(){

		courses = model.getMyCoursesFast();

		TableLayout scheduleTable =
			(TableLayout)findViewById(R.id.tableLayoutSchedule);

		scheduleTable.removeAllViews();

		int i = 0;
		for(RegisteredCourseModel c : courses) {
			// Add title row to schedule

			// Add title to title row
			TableRow titleRow = new TableRow(this);
			titleRow.setLayoutParams(new LayoutParams());

			TextView title = new TextView(this);

			title.setText(
					c.getSubject() 
					+ " " 
					+ c.getCatalog() 
					+ ": " 
					+ c.getTitle());

			TableRow.LayoutParams params = new TableRow.LayoutParams();
			params.span = 5;

			titleRow.addView(title,params);

			scheduleTable.addView(titleRow,
					new TableLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT, 
							LayoutParams.MATCH_PARENT));

			// Add column tags row to schedule
			TableRow columnTagRow = new TableRow(this);
			columnTagRow.setLayoutParams(new LayoutParams());

			TextView section = new TextView(this);
			TextView component = new TextView(this);
			TextView daysTimes = new TextView(this);
			TextView room = new TextView(this);

			section.setText(R.string.section);
			component.setText(R.string.component);
			daysTimes.setText(R.string.dayTime);
			room.setText(R.string.room);

			columnTagRow.addView(section);
			columnTagRow.addView(component);
			columnTagRow.addView(daysTimes);
			columnTagRow.addView(room);

			scheduleTable.addView(columnTagRow);

			// Add course information to schedule			
			TableRow columnInfoRow = new TableRow(this);
			columnInfoRow.setLayoutParams(new LayoutParams());

			TextView sectionInfo = new TextView(this);
			TextView componentInfo = new TextView(this);
			TextView daysTimesInfo = new TextView(this);
			TextView roomInfo = new TextView(this);

			sectionInfo.setText(c.getSec());
			componentInfo.setText(c.getType());
			daysTimesInfo.setText(c.getDayTime());
			roomInfo.setText(c.getRoom());

			columnInfoRow.addView(sectionInfo);
			columnInfoRow.addView(componentInfo);
			columnInfoRow.addView(daysTimesInfo);
			columnInfoRow.addView(roomInfo);

			// Add a checkbox to columnInfo row
			CheckBox courseCheckBox = new CheckBox(this);
			courseCheckBox.setId(i);
			courseCheckBox.setBackgroundResource(R.drawable.checkbox_background);
			courseCheckBox.setButtonDrawable(R.drawable.checkbox);

			final Rect r = new Rect();
			courseCheckBox.getHitRect(r);
			r.top -= 20;
			r.bottom += 20;
			courseCheckBox.setTouchDelegate( new TouchDelegate( r , courseCheckBox));

			i++;

			columnInfoRow.addView(courseCheckBox);

			scheduleTable.addView(columnInfoRow);			

			TableRow lineRow = new TableRow(this);
			TableRow lineRow2 = new TableRow(this);
			
			//lineRow.addView(ruler,new ViewGroup.LayoutParams( 
			//		ViewGroup.LayoutParams.FILL_PARENT, 2));
			
			TableLayout.LayoutParams tableRowParams=
				  new TableLayout.LayoutParams
				  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.FILL_PARENT);
			
			tableRowParams.setMargins(0, 8, 0, 8);
			
			scheduleTable.addView(
					lineRow,
					tableRowParams
			);
			
			View ruler = new View(this); 
			//ruler.setBackgroundResource(R.drawable.horizontal);
			ruler.setBackgroundColor(0xFF909090);
			
			scheduleTable.addView(
					ruler,
					new ViewGroup.LayoutParams( 
							ViewGroup.LayoutParams.FILL_PARENT, 2)
			);
			
			scheduleTable.addView(
					lineRow2,
					tableRowParams
			);

		} // for
	} // update course list

	public void removeCourse(View button){
		List<RegisteredCourseModel> deletedCourse = 
			new LinkedList<RegisteredCourseModel>();
		for (int i = 0; i < courses.size(); i++){
			CheckBox tempCheckBox = (CheckBox) findViewById(i);
			if (tempCheckBox.isChecked()){
				RegisteredCourseModel tempCourse = courses.get(i);
				deletedCourse.add(tempCourse);
			}
		}

		if (deletedCourse.size() != 0){
			for (RegisteredCourseModel c: deletedCourse){
				model.removeCourses(c);
			}
			updateCourseList();
		}
	}

	public void saveCourse(View button){
		try {
			model.saveCourses();
			showDialog(SAVE_DONE);
		} catch (HttpPostException e) {
			// TODO Auto-generated catch block
			showDialog(NETWORK_ERROR);
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch(id) {
		case NETWORK_ERROR:
			return new AlertDialog.Builder(ScheduleActivity.this)
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
			return new AlertDialog.Builder(ScheduleActivity.this)
			.setTitle(R.string.saveDone)
			.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					
				}
			})
			.create();
		case SAVE_DONE_BACK:
			return new AlertDialog.Builder(ScheduleActivity.this)
			.setTitle(R.string.saveDone)
			.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
					finish();
				}
			})
			.create();
		case DIALOG_SAVE_COURSE:
			return new AlertDialog.Builder(ScheduleActivity.this)
			.setMessage(R.string.saveCourse)
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
					dialog.cancel();
				}
			})
			.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
					finish();
				}
			})
			.create();
		default:
			return null;
		}
	}
}