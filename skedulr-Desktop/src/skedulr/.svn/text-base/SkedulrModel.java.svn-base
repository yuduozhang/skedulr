package skedulr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class SkedulrModel extends Object {
	/* A list of the model's views. */
	public ArrayList<IView> views = new ArrayList<IView>();

	// There should be only one instance of the model.
	private static SkedulrModel instance = new SkedulrModel();
	private AccountModel account;
	// Override the default construtor, making it private.
	private SkedulrModel() {
	}
	
	public int loginError = 0;
	public int createAccountError = 0;
	public int moreThanFiveCoursesError = 0;
	public int timeConflictsError = 0;
	public List<CourseModel> courses = CourseModel.coursesFactory();
	public List<RegisteredCourseModel> registeredCourses;
	public String[] conflictCourses = new String[2];
	 
	

	/**
	 * There should be only one instance of the model. Use this method to get
	 * it.
	 */
	public static SkedulrModel getInstance() {
		return SkedulrModel.instance;
	}

	public void login(String userID, String password){
		
			try {
				account = new AccountModel(userID, password);
				loginError = 0;
			} catch (AccountInvalidErrorModel e) {
				loginError = 1;
				updateAllViews();
			} catch (AccountMissingUserIDErrorModel e) {
				loginError = 2;
				updateAllViews();
			} catch (AccountMissingPaawdErrorModel e) {
				loginError = 3;
				updateAllViews();
			} catch (HttpPostException e) {
				loginError = 4;
				updateAllViews();
			}
	}
	
	public void createAccount(
			String userID, 
			String password, 
			String surname,
			String givenNames
			){
		try {
			account = new AccountModel(userID, password, surname, givenNames);
			createAccountError = 0;
			System.out.println("  UserID  = " + account.userId);
			System.out.println("  Surname = " + account.surname);
			System.out.println("  Given   = " + account.givenNames);
		} catch (DuplicateAccountErrorModel e) {
			createAccountError = 1;
			updateAllViews();
		} catch (HttpPostException e) {
			createAccountError = 2;
			updateAllViews();
		} catch (AccountCreationErrorModel e) {
			createAccountError = 3;
			updateAllViews();
		}
	}
	
	public List<RegisteredCourseModel> addInfo (List<RegisteredCourseModel> lst){		
		 List<RegisteredCourseModel> newLst = new LinkedList<RegisteredCourseModel>();
		
		 for(RegisteredCourseModel course : lst) {
	 		   String subject = course.getSubject();
	 		   String catalog = course.getCatalog();
	 		   String sec 	  = course.getSec();
	 		   
	 		   boolean[] days = new boolean[7];
	 		   String startTime = null;
	 		   String endTime = null;
	 		   String room = null;
	 		   String type = null;
	 		   
	 		   for (CourseModel courseFull: courses){
	 			   if (subject.equals(courseFull.getSubject())
	 					   &&
	 				   catalog.equals(courseFull.getCatalog())){
	 				   
	 				   SectionModel[] sections = courseFull.getSections();
	 				   
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
	 		   course.endTime = endTime;
	 		   course.room = room;
	 		   course.type = type;
	 		   newLst.add(course);
		 }
		 
		return newLst;
	}
	
	 // Handle Time Conflicts here. 
	 //Note! lst hould be a list of courses of full information
	public boolean timeConflicts (List<RegisteredCourseModel> lst, String[] conflictCourses){
		
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
	}
	
	public void addCourses( RegisteredCourseModel c ){
		 //List<RegisteredCourseModel> lst;
		 //lst = RegisteredCourseModel.getCourses(account.userId, account.password);
		 //registeredCourses.add(c);
		 
		 if (registeredCourses.size() == 5){
				moreThanFiveCoursesError = 1;
				updateAllViews();
				return;
		 }
		 
		 if (!registeredCourses.contains(c)){
			 registeredCourses.add(c);
		 
		 
			 List<RegisteredCourseModel> fullLst = addInfo(registeredCourses);
			 if (timeConflicts(fullLst, conflictCourses)){
				timeConflictsError = 1;
				registeredCourses.remove(c);
				updateAllViews();
				return;
			 }
		}
		 
		updateAllViews();
		return;
	}	
	
	public void saveCourses(){
		 try {
			RegisteredCourseModel.replaceCourses(account.userId, account.password, registeredCourses);
			updateAllViews();
		} catch (HttpPostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}	
	
	public List<RegisteredCourseModel> getMyCourses(){
		 registeredCourses = 
			 RegisteredCourseModel.getCourses(account.userId, account.password);
		 return registeredCourses;
	}
	
	public List<RegisteredCourseModel> getMyCoursesFast() {
		return registeredCourses;
	}
	
	public List<CourseModel> returnCourseList(){
		return courses;
	}

	/** Add a new view of this triangle. */
	public void addView(IView view) {
		this.views.add(view);
		view.updateView();
	}

	/** Remove a view from this triangle. */
	public void removeView(IView view) {
		this.views.remove(view);
	}

	/** Update all the views that are viewing this triangle. */
	public void updateAllViews() {
		for (IView view : this.views) {
			view.updateView();
		}
	}

}
