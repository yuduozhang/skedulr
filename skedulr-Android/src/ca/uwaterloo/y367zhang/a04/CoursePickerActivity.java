package ca.uwaterloo.y367zhang.a04;

import java.util.LinkedList;
import java.util.List;

import ca.uwaterloo.y367zhang.a04.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

public class CoursePickerActivity extends Activity {

	private SkedulrModel model = SkedulrModel.getInstance();
	private ExpandableListAdapter mAdapter;
	private ExpandableListView courseView;
	private Bundle conflictCourseInfo = new Bundle();

	private RegisteredCourseModel courseWanted;
	private RegisteredCourseModel courseInSchedule;
	private int checkBoxID;

	static final int DIALOG_MAXIMUM_ID = 0;
	private static final int NETWORK_ERROR = 1;
	private static final int SWAP_COURSE = 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coursepicker);

		courseView =
			(ExpandableListView) findViewById(R.id.expandableListCourseInfo);
		mAdapter = new CoursePickerExpandableListAdapter();
		courseView.setAdapter(mAdapter);

		final EditText filterText = (EditText) findViewById(R.id.filterText);

		filterText.setSingleLine();  
		filterText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				// TODO Auto-generated method stub          

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				((Filterable) mAdapter).getFilter().filter(s);
			}

			@Override
			public void onTextChanged(CharSequence s, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				//((Filterable) mAdapter).getFilter().filter(s);
			}
		});
	}

	public class CoursePickerExpandableListAdapter extends BaseExpandableListAdapter implements Filterable {
		private SkedulrModel model = SkedulrModel.getInstance();
		public List<RegisteredCourseModel> courseList = model.returnCourseList();
		public List<RegisteredCourseModel> filteredCourseList = courseList;

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return filteredCourseList.get(groupPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// Every group only has one child
			return childPosition;
		}

		@Override
		public View getChildView(
				final int groupPosition, 
				int childPosition, 
				boolean isLastChild, 
				View convertView, 
				ViewGroup parent) {

			final RegisteredCourseModel courseTemp =
				(RegisteredCourseModel) getChild(groupPosition, childPosition);

			TableLayout courseTable =
				new TableLayout(
						CoursePickerActivity.this
				);

			//courseTable.setLayoutParams(new TableLayout.LayoutParams(
			//		LayoutParams.MATCH_PARENT,
			//		LayoutParams.MATCH_PARENT));

			courseTable.setStretchAllColumns(true);
			courseTable.setColumnShrinkable(2, true);

			//Add conflict information row to schedule

			// Add conflict information to this row
			RegisteredCourseModel conflictCourse[] = new RegisteredCourseModel[1];

			if (model.timeConflicts(courseTemp,conflictCourse)){
				TableRow conflictInfoRow = new TableRow(CoursePickerActivity.this);
				conflictInfoRow.setLayoutParams(new LayoutParams());

				TextView title = new TextView(CoursePickerActivity.this);

				title.setText(getString(R.string.timeConflicts) + " " + conflictCourse[0].getSubject() + " " + conflictCourse[0].getCatalog());
				title.setTextColor(Color.RED);


				TableRow.LayoutParams params = new TableRow.LayoutParams();
				params.span = 5;

				conflictInfoRow.addView(title,params);

				courseTable.addView(conflictInfoRow,
						new TableLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT, 
								LayoutParams.MATCH_PARENT));

				conflictCourseInfo.putString(
						Integer.toString(groupPosition) + "courseWantedSubject", courseTemp.getSubject());
				conflictCourseInfo.putString(
						Integer.toString(groupPosition) + "courseWantedCatalog", courseTemp.getCatalog());
				conflictCourseInfo.putString(
						Integer.toString(groupPosition) + "courseWantedSec", courseTemp.getSec());

				conflictCourseInfo.putString(
						Integer.toString(groupPosition) + "courseInScheduleSubject", conflictCourse[0].getSubject());
				conflictCourseInfo.putString(
						Integer.toString(groupPosition) + "courseInScheduleCatalog", conflictCourse[0].getCatalog());
				conflictCourseInfo.putString(
						Integer.toString(groupPosition) + "courseInScheduleSec", conflictCourse[0].getSec());

				System.out.println(conflictCourse[0].getCatalog());
				System.out.println(groupPosition);
				System.out.println(Integer.toString(groupPosition) + "courseInScheduleCatalog");
				System.out.println(conflictCourseInfo.getString(Integer.toString(groupPosition) + "courseInScheduleCatalog"));
			}

			// Add column tags row to schedule
			TableRow columnTagRow = new TableRow(CoursePickerActivity.this);
			columnTagRow.setLayoutParams(new LayoutParams());

			TextView section = new TextView(CoursePickerActivity.this);
			TextView component = new TextView(CoursePickerActivity.this);
			TextView daysTimes = new TextView(CoursePickerActivity.this);
			TextView room = new TextView(CoursePickerActivity.this);

			section.setText(R.string.section);
			component.setText(R.string.component);
			daysTimes.setText(R.string.dayTime);
			room.setText(R.string.room);

			columnTagRow.addView(section);
			columnTagRow.addView(component);
			columnTagRow.addView(daysTimes);
			columnTagRow.addView(room);

			courseTable.addView(columnTagRow);

			// Add course information to schedule			
			TableRow columnInfoRow = new TableRow(CoursePickerActivity.this);
			columnInfoRow.setLayoutParams(new LayoutParams());

			TextView sectionInfo = new TextView(CoursePickerActivity.this);
			TextView componentInfo = new TextView(CoursePickerActivity.this);
			TextView daysTimesInfo = new TextView(CoursePickerActivity.this);
			TextView roomInfo = new TextView(CoursePickerActivity.this);

			sectionInfo.setText(courseTemp.getSec());
			componentInfo.setText(courseTemp.getType());
			daysTimesInfo.setText(courseTemp.getDayTime());
			roomInfo.setText(courseTemp.getRoom());

			columnInfoRow.addView(sectionInfo);
			columnInfoRow.addView(componentInfo);
			columnInfoRow.addView(daysTimesInfo);
			columnInfoRow.addView(roomInfo);

			// Add a checkbox to columnInfo row
			final CheckBox addDeleteCheckBox = new CheckBox(CoursePickerActivity.this);
			addDeleteCheckBox.setId(groupPosition);
			addDeleteCheckBox.setBackgroundResource(R.drawable.checkbox_background);
			addDeleteCheckBox.setButtonDrawable(R.drawable.buttondrawable);

			if (model.registeredCourses.contains(courseTemp)){
				addDeleteCheckBox.setChecked(true);
			}

			/*else if (model.timeConflicts(courseTemp,null)){

				addDeleteCheckBox.setEnabled(false);
			}*/

			addDeleteCheckBox.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// Perform action on click
					if(!addDeleteCheckBox.isChecked()){
						model.removeCourses(courseTemp);
					}
					else if (model.timeConflicts(courseTemp, null)){
						conflictCourseInfo.putInt("courseWantedID", groupPosition);
						addDeleteCheckBox.setChecked(false);
						showDialog(SWAP_COURSE, conflictCourseInfo);
					}
					else if (model.registeredCourses.size() >= 5){
						addDeleteCheckBox.setChecked(false);
						showDialog(DIALOG_MAXIMUM_ID);
					}
					else if (addDeleteCheckBox.isChecked()){
						model.addCourses(courseTemp);
					}
				}
			});

			columnInfoRow.addView(addDeleteCheckBox);
			courseTable.addView(columnInfoRow);			

			/*View ruler = new View(CoursePickerActivity.this); 
			ruler.setBackgroundColor(0xFF909090);
			courseTable.addView(
					ruler,
					new ViewGroup.LayoutParams( 
							ViewGroup.LayoutParams.FILL_PARENT, 2)
			);*/

			return courseTable;
		}

		public TextView getGenericView() {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 64);

			TextView textView = new TextView(CoursePickerActivity.this);
			textView.setLayoutParams(lp);
			// Center the text vertically
			textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			textView.setMaxLines(3);

			// Set the text starting position
			textView.setPadding(53, 0, 0, 0);
			return textView;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return filteredCourseList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return filteredCourseList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, 
				boolean isExpanded, 
				View convertView, 
				ViewGroup parent) {
			// TODO Auto-generated method stub
			TextView textView = getGenericView();
			textView.setText(getGroup(groupPosition).toString());

			RegisteredCourseModel courseChild = 
				(RegisteredCourseModel) getChild(groupPosition, 0);

			if (model.timeConflicts(courseChild,null)){
				textView.setTextColor(Color.RED);
			}

			if (model.registeredCourses.contains(getChild(groupPosition, 0))){
				courseView.expandGroup(groupPosition);
			}

			return textView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			Filter filter = new MyFilter();
			return filter;
		}

		private class MyFilter extends Filter{

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				//final EditText filterText = (EditText) findViewById(R.id.filterText);
				//constraint = filterText.getText().toString().toLowerCase();
				FilterResults result = new FilterResults();
				List<RegisteredCourseModel> filteredCourseListTemp 
				= new LinkedList<RegisteredCourseModel>();

				if (constraint!= null && constraint.toString().length() > 0) {

					for (int index = 0; index < courseList.size(); index++) {
						RegisteredCourseModel courseTemp = courseList.get(index);
						if(courseTemp.toString().toLowerCase().contains(constraint)){
							filteredCourseListTemp.add(courseTemp);   
						}
					}
					result.values = filteredCourseListTemp;
					result.count = filteredCourseListTemp.size();                   
				}
				else{
					synchronized (courseList){
						result.values = courseList;
						result.count = courseList.size();
					}
				}

				return result;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults result) {
				// TODO Auto-generated method stub
				filteredCourseList =  (List<RegisteredCourseModel>) result.values;
				notifyDataSetChanged();
			}
		}
	}

	protected Dialog onCreateDialog(int id, final Bundle b) {
		switch(id) {
		case NETWORK_ERROR:
			return new AlertDialog.Builder(CoursePickerActivity.this)
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
		case DIALOG_MAXIMUM_ID:
			return new AlertDialog.Builder(CoursePickerActivity.this)
			.setMessage(R.string.maximumCoursesError)
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					finish();
					dialog.cancel();
				}
			})
			.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					/* User clicked OK so do some stuff */
					dialog.cancel();
				}
			})
			.create();
		case SWAP_COURSE:
			
			/*conflictCourseInfo.putString(
					Integer.toString(groupPosition) + "courseWantedSubject", courseTemp.getSubject());
			conflictCourseInfo.putString(
					Integer.toString(groupPosition) + "courseWantedCatalog", courseTemp.getCatalog());
			conflictCourseInfo.putString(
					Integer.toString(groupPosition) + "courseWantedSec", courseTemp.getSec());

			conflictCourseInfo.putString(
					Integer.toString(groupPosition) + "courseInScheduleSubject", conflictCourse[0].getSubject());
			conflictCourseInfo.putString(
					Integer.toString(groupPosition) + "courseInScheduleCatalog", conflictCourse[0].getCatalog());
			conflictCourseInfo.putString(
					Integer.toString(groupPosition) + "courseInScheduleSec", conflictCourse[0].getSec());
			conflictCourseInfo.putInt("courseWantedID", groupPosition);*/

			checkBoxID = b.getInt("courseWantedID");

			String courseWantedSubject = b.getString(Integer.toString(checkBoxID)+"courseWantedSubject");
			String courseWantedCatalog = b.getString(Integer.toString(checkBoxID)+"courseWantedCatalog");
			String courseWantedSec = b.getString(Integer.toString(checkBoxID)+"courseWantedSec");

			String courseInScheduleSubject = b.getString(Integer.toString(checkBoxID)+"courseInScheduleSubject");
			String courseInScheduleCatalog = b.getString(Integer.toString(checkBoxID)+"courseInScheduleCatalog");
			String courseInScheduleSec = b.getString(Integer.toString(checkBoxID)+"courseInScheduleSec");

			courseWanted = model.addInfoCourse(			
					new RegisteredCourseModel(courseWantedSubject, courseWantedCatalog, courseWantedSec));
			courseInSchedule = model.addInfoCourse(				
					new RegisteredCourseModel(courseInScheduleSubject, courseInScheduleCatalog, courseInScheduleSec));

			return new AlertDialog.Builder(CoursePickerActivity.this)
			.setMessage(getString(R.string.timeConflicts) + " " + courseInSchedule.toString() + ". " + getString(R.string.swap))
			.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton){
					/* User clicked OK so do some stuff */				
					model.removeCourses(courseInSchedule);				
					model.addCourses(courseWanted);				
					CheckBox checkBoxChanged = (CheckBox) findViewById(checkBoxID);			
					checkBoxChanged.setChecked(true);				
					dialog.cancel()	;
				}
			})
			.setNegativeButton(R.string.No, new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){				
					dialog.cancel()	;
				}
			})
			.create();
		default:
			return null;
		}
	}

	protected void onPrepareDialog (int id, Dialog dialog, Bundle b){
		switch(id) {
		case SWAP_COURSE:
			
			System.out.println("prepare");

			checkBoxID = b.getInt("courseWantedID");

			String courseWantedSubject = b.getString(Integer.toString(checkBoxID)+"courseWantedSubject");
			String courseWantedCatalog = b.getString(Integer.toString(checkBoxID)+"courseWantedCatalog");
			String courseWantedSec = b.getString(Integer.toString(checkBoxID)+"courseWantedSec");

			String courseInScheduleSubject = b.getString(Integer.toString(checkBoxID)+"courseInScheduleSubject");
			String courseInScheduleCatalog = b.getString(Integer.toString(checkBoxID)+"courseInScheduleCatalog");
			String courseInScheduleSec = b.getString(Integer.toString(checkBoxID)+"courseInScheduleSec");

			courseWanted = model.addInfoCourse(			
					new RegisteredCourseModel(courseWantedSubject, courseWantedCatalog, courseWantedSec));
			courseInSchedule = model.addInfoCourse(				
					new RegisteredCourseModel(courseInScheduleSubject, courseInScheduleCatalog, courseInScheduleSec));
			
			((AlertDialog) dialog).setMessage(getString(R.string.timeConflicts) + " " + courseInSchedule.toString() + ". " + getString(R.string.swap));
			
			break;
		}
	}
}