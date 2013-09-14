package ca.uwaterloo.y367zhang.a04;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class SkedulrModel extends Object {

	// There should be only one instance of the model.
	private static SkedulrModel instance = new SkedulrModel();
	public AccountModel account;
	// Override the default construtor, making it private.
	private SkedulrModel() {
	}

	public boolean login = false;
	public int createAccountError = 0;
	public int moreThanFiveCoursesError = 0;
	public int timeConflictsError = 0;
	public boolean scheduleChanged = false;
	

	// Should change static back later
	public static List<CourseModel> courses;
	public List<RegisteredCourseModel> registeredCourses;
	public String[] conflictCourses = new String[2];



	/**
	 * There should be only one instance of the model. Use this method to get
	 * it.
	 */
	public static SkedulrModel getInstance() {
		return SkedulrModel.instance;
	}

	public void login(String userID, String password) 
	throws HttpPostException, 
	AccountInvalidErrorModel,
	AccountMissingUserIDErrorModel, 
	AccountMissingPaawdErrorModel{
		account = new AccountModel(userID, password);
		courses = CourseModel.coursesFactory();
		getMyCourses();
		login = true;
		scheduleChanged = false;
	}

	public boolean checkLogin(){
		return login;
	}

	public void logout(){
		login = false;
	}

	public void createAccount(
			String userID, 
			String password, 
			String surname,
			String givenNames
	) 
	throws DuplicateAccountErrorModel, AccountCreationErrorModel, HttpPostException{

		account = new AccountModel(userID, password, surname, givenNames);
	}

	public List<RegisteredCourseModel> addInfo (List<RegisteredCourseModel> lst){		
		List<RegisteredCourseModel> newLst = new LinkedList<RegisteredCourseModel>();

		for(RegisteredCourseModel course : lst) {
			String subject = course.getSubject();
			String catalog = course.getCatalog();
			String sec 	  = course.getSec();

			boolean[] days = new boolean[7];
			String title = null;
			String startTime = null;
			String endTime = null;
			String room = null;
			String type = null;

			for (CourseModel courseFull: courses){
				if (subject.equals(courseFull.getSubject())
						&&
						catalog.equals(courseFull.getCatalog())){

					SectionModel[] sections = courseFull.getSections();
					title = courseFull.getTitle();

					for (SectionModel section: sections){
						if (sec.equals(section.getSec())){
							days = section.getDays();
							startTime = section.getStartTime();
							endTime = section.getEndTime();
							room = section.getRoom();
							type = section.getType();
							break;
						}
					}
					break;  
				}
			} 
			course.days = days;
			course.startTime = startTime;
			course.title = title;
			course.endTime = endTime;
			course.room = room;
			course.type = type;
			newLst.add(course);
		}

		return newLst;
	}

	public RegisteredCourseModel addInfoCourse (RegisteredCourseModel c){		
		if(c!=null){
			String subject = c.getSubject();
			String catalog = c.getCatalog();
			String sec 	  = c.getSec();

			RegisteredCourseModel course = new RegisteredCourseModel(subject,catalog,sec);

			boolean[] days = new boolean[7];
			String title = null;
			String startTime = null;
			String endTime = null;
			String room = null;
			String type = null;

			for (CourseModel courseFull: courses){
				if (subject.equals(courseFull.getSubject())
						&&
						catalog.equals(courseFull.getCatalog())){

					SectionModel[] sections = courseFull.getSections();
					title = courseFull.getTitle();

					for (SectionModel section: sections){
						if (sec.equals(section.getSec())){
							days = section.getDays();
							startTime = section.getStartTime();
							endTime = section.getEndTime();
							room = section.getRoom();
							type = section.getType();
							break;
						}
					}
					break;  
				}
			} 
			course.days = days;
			course.startTime = startTime;
			course.title = title;
			course.endTime = endTime;
			course.room = room;
			course.type = type;

			return course;
		}
		return null;
	}

	// Handle Time Conflicts here. 
	//Note! lst hould be a list of courses of full information
	/*public boolean timeConflicts (List<RegisteredCourseModel> lst, String[] conflictCourses){

		String course1;
		String course2;

		for (RegisteredCourseModel c: lst){
			for (RegisteredCourseModel c2: lst){
				if ( ! (
						c.getSubject().equals(c2.getSubject()) &&
						c.getCatalog().equals(c2.getCatalog()) &&
						c.getSec().equals(c2.getSec())
				)
				){
					boolean time = false;

					if (c.startTime.compareTo(c2.startTime) < 0){
						if (c.endTime.compareTo(c2.startTime) > 0){
							time = true;
						}
					}
					else{
						if (c2.endTime.compareTo(c.startTime) > 0){
							time = true;
						}
					}

					if (time == true){
						for(int i=0; i<c.days.length; i++) {
							if (c.days[i] && c2.days[i]) {
								course1 = c.getSubject() + c.getCatalog() + " sec " + c.getSec();
								course2 = c2.getSubject() + c2.getCatalog() + " sec " + c2.getSec();
								conflictCourses[0] = course1;
								conflictCourses[1] = course2;
								return true;
							}
						}
					}
				}
				else continue;
			}
		}

		return false;
	}*/

	public boolean timeConflicts (RegisteredCourseModel course , RegisteredCourseModel[] courseInSchedule){

		String course1;
		String course2;

		for (RegisteredCourseModel c: registeredCourses){
			if (!course.equals(c)){
				boolean time = false;

				if (c.startTime.compareTo(course.startTime) < 0){
					if (c.endTime.compareTo(course.startTime) > 0){
						time = true;
					}
				}
				else{
					if (course.endTime.compareTo(c.startTime) > 0){
						time = true;
					}
				}

				if (time == true){
					for(int i=0; i<c.days.length; i++) {
						if (c.days[i] && course.days[i]) {
							course1 = c.getSubject() + c.getCatalog() + " sec " + c.getSec();
							course2 = course.getSubject() + 
							course.getCatalog() + 
							" sec " + 
							course.getSec();
							conflictCourses[0] = course1;
							conflictCourses[1] = course2;
							if (courseInSchedule!=null){
								courseInSchedule[0] = c;
							}
							return true;
						}
					}
				}

				else continue;
			}
		}

		return false;
	}

	public void addCourses( RegisteredCourseModel c ){
		//List<RegisteredCourseModel> lst;
		//lst = RegisteredCourseModel.getCourses(account.userId, account.password);
		//registeredCourses.add(c);

		if (!registeredCourses.contains(c)){
			registeredCourses.add(c);
			scheduleChanged = true;
		}

		/*List<RegisteredCourseModel> fullLst = addInfo(registeredCourses);
			if (timeConflicts(fullLst, conflictCourses)){
				timeConflictsError = 1;
				registeredCourses.remove(c);
				updateAllViews();
				return;
			}
		}

		updateAllViews();*/
		return;
	}	

	public void removeCourses(RegisteredCourseModel c){
		registeredCourses.remove(c);
		scheduleChanged = true;
	}

	public void saveCourses() throws HttpPostException{
		RegisteredCourseModel.replaceCourses(account.userId, account.password, registeredCourses);
		scheduleChanged = false;
	}	

	public List<RegisteredCourseModel> getMyCourses() throws HttpPostException{
		List<RegisteredCourseModel> 
		courseLessInfo = new LinkedList<RegisteredCourseModel>();
		courseLessInfo = 
			RegisteredCourseModel.getCourses(account.userId, account.password);

		registeredCourses = addInfo(courseLessInfo);
		return registeredCourses;
	}

	public List<RegisteredCourseModel> getMyCoursesFast() {
		return registeredCourses;
	}

	public List<RegisteredCourseModel> returnCourseList(){
		List<RegisteredCourseModel> 
		courseToRegisteredCourse = new LinkedList<RegisteredCourseModel>();

		List<RegisteredCourseModel> 
		courseList = new LinkedList<RegisteredCourseModel>();

		for(CourseModel c : courses) {
			String subject = c.getSubject();
			String catalog = c.getCatalog();
			String sec;

			// Get only lec information
			SectionModel[] sections = c.getSections();

			// Get sec of lec
			sec = sections[0].getSec();

			courseToRegisteredCourse.add(new RegisteredCourseModel(subject, catalog, sec));
		}

		courseList = addInfo(courseToRegisteredCourse);
		return courseList;
	}

	/** Add a new view of this triangle. */
	/*public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}*/

	/** Remove a view from this triangle. */
	/*public void removeView(IView view) {
		this.views.remove(view);
	}*/

	/** Update all the views that are viewing this triangle. */
	public void updateAllViews() {
		/*for (IView view : this.views) {
			view.updateView();
		}*/
	}

}
