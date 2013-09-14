package skedulr;

import java.util.LinkedList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class SelectCoursesView extends JPanel
                      implements ListSelectionListener ,IView{
    private SkedulrModel model;
    private JList courseList;
    private DefaultListModel listModel;

    private JButton addButton = new JButton("Add");
    //private JButton myCoursesButton = new JButton("My Courses");
    
    private JLabel errorMessageJLabel = 
    						new JLabel("");
    
    private List<RegisteredCourseModel> registeredCourses;

    public SelectCoursesView(SkedulrModel aModel) {
        super(new BorderLayout());
        this.model = aModel;
        
        // Add a this view as a listener to the model
		this.model.addView(this);
        
        this.registeredCourses = new LinkedList<RegisteredCourseModel>();
        
        listModel = new DefaultListModel();
        addCourseList(listModel);

        //Create the list and put it in a scroll pane.
        courseList = new JList(listModel);
        courseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseList.setSelectedIndex(0);
        courseList.addListSelectionListener(this);
        courseList.setVisibleRowCount(20);
        JScrollPane listScrollPane = new JScrollPane(courseList);

        addButton.setActionCommand("Add");
        addButton.addActionListener(new AddListener());
        
        //myCoursesButton.setActionCommand("My Courses");
        //myCoursesButton.addActionListener(new MyCoursesListener());

        String name = listModel.getElementAt(
                              courseList.getSelectedIndex()).toString();

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        //buttonPane.add(myCoursesButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(errorMessageJLabel);
        buttonPane.add(addButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    class AddListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
            int index = courseList.getSelectedIndex();
            RegisteredCourseModel course = registeredCourses.get(index); 
            model.addCourses(course);
        }
    }
    
    class MyCoursesListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	MyCoursesView myCourses = new MyCoursesView(model);
			
			JFrame mainMenuFrame = new JFrame("My Courses");
		
			mainMenuFrame.getContentPane().add(myCourses);
			mainMenuFrame.setLocationRelativeTo(null);
			mainMenuFrame.pack();
			mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mainMenuFrame.setVisible(true);
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        model.timeConflictsError = 0;
    	if (e.getValueIsAdjusting() == false) {

            if (courseList.getSelectedIndex() == -1) {
                addButton.setEnabled(false);

            } else {
                addButton.setEnabled(true);
            }
        }
    }

    public void addCourseList(DefaultListModel l){
       List<CourseModel> courses = model.returnCourseList();
 	   
 	   for(CourseModel c : courses) {
 		   String courseInfo = c.toString();
 		   String subject = c.getSubject();
 		   String catalog = c.getCatalog();
 		   String sec;
 		   
 		   // Get only lec information
 		   SectionModel[] sections = c.getSections();
 		   courseInfo = courseInfo + "  " + sections[0];
 		   
 		   // Get sec of lec
 		   sec = sections[0].getSec();
 		   
 		   registeredCourses.add(new RegisteredCourseModel(subject, catalog, sec));
 		   l.addElement(courseInfo);
 	   }
    }

	public void updateView() {
		if (model.moreThanFiveCoursesError == 1){
			errorMessageJLabel.setText("You can enroll in at most 5 courses.");
			addButton.setEnabled(false);
			return;
		}
		else if (model.moreThanFiveCoursesError == 0){
			errorMessageJLabel.setText("");
			addButton.setEnabled(true);
		}
		if (model.timeConflictsError == 1){
			String timeConflictsError = model.conflictCourses[0]
			                                                  + " and "
			                                                  + model.conflictCourses[1]
			                                                  + " has time conflicts.";
			errorMessageJLabel.setText(timeConflictsError);
			return;
		}
		else if(model.timeConflictsError == 0){
			errorMessageJLabel.setText("");

		}
	}
}

