package skedulr;

import java.util.LinkedList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MyCoursesView extends JPanel
                      implements ListSelectionListener, IView {
    private SkedulrModel model;
    private JList myCourseList;
    private DefaultListModel listModel;

    private JButton removeButton = new JButton("Remove");
    private JButton saveButton = new JButton("Save");
    private JButton logoutButton = new JButton("Log Out");
    //private List<RegisteredCourseModel> registeredCourses;

    public MyCoursesView(SkedulrModel aModel) {
        super(new BorderLayout());
        this.model = aModel;
        
        //this.registeredCourses = new LinkedList<RegisteredCourseModel>();
        
        listModel = new DefaultListModel();
        addMyCourseList(listModel);

        // Add a this view as a listener to the model
		this.model.addView(this);
		
        //Create the list and put it in a scroll pane.
        myCourseList = new JList(listModel);
        myCourseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        myCourseList.setSelectedIndex(0);
        myCourseList.addListSelectionListener(this);
        myCourseList.setVisibleRowCount(6);
        JScrollPane listScrollPane = new JScrollPane(myCourseList);

        removeButton.setActionCommand("Remove");
        removeButton.addActionListener(new RemoveListener());
        
        saveButton.setActionCommand("Save");
        saveButton.addActionListener(new SaveListener());

        logoutButton.setActionCommand("Log out");
        logoutButton.addActionListener(new LogoutListener());

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(logoutButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(removeButton);
        buttonPane.add(saveButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }
    
    /**
     * What to do when the model changes.
     */
    public void updateView() {
    	addMyCourseListFast(this.listModel);
    }

    class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            int size = listModel.getSize();

            if (size == 0) { //No courses are left, disable remove.
                removeButton.setEnabled(false);
            } else { //Select an index.
                int index = myCourseList.getSelectedIndex();
                listModel.remove(index);
                //registeredCourses.remove(index);
                model.registeredCourses.remove(index);
                
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
                
                model.moreThanFiveCoursesError = 0;
                
                myCourseList.setSelectedIndex(index);
                myCourseList.ensureIndexIsVisible(index);
            }
        }
    }
    
    class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	model.saveCourses();
        }
    }
    
    class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	
        	// Error Handle should be added later
			java.awt.Window win[] = java.awt.Window.getWindows(); 
			for(int i=0;i<win.length;i++){ 
			win[i].dispose(); 
			} 
        	
        	skedulr.SkedulrModel model = SkedulrModel.getInstance();
    		LoginView view = new LoginView(model);

    		JFrame loginFrame = new JFrame("Skedulr: Login");
    		loginFrame.getContentPane().add(view);
    		loginFrame.setLocationRelativeTo(null);
    		loginFrame.pack();
    		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		loginFrame.setLocation(450,300);
    		loginFrame.setVisible(true);
    		loginFrame.setSize(350,150);
        }
    }

    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
            if (myCourseList.getSelectedIndex() == -1) {
                removeButton.setEnabled(false);
            } else {
                removeButton.setEnabled(true);
            }
        }
    }

    public void addMyCourseList(DefaultListModel l){
	       //registeredCourses.clear();
	       l.clear();
	       
	       List<RegisteredCourseModel> courses = model.addInfo(model.getMyCourses());

	 	   for(RegisteredCourseModel c : courses) {
	 		   String courseInfo = c.toString();
	 		   
	 		   
	 		   //registeredCourses.add(new RegisteredCourseModel(subject, catalog, sec));
	 		   l.addElement(courseInfo);
	 	   }
	    }
    
    public void addMyCourseListFast(DefaultListModel l){
	       //registeredCourses.clear();
	       l.clear();
	       
	       List<RegisteredCourseModel> courses = model.addInfo(model.getMyCoursesFast());

	 	   for(RegisteredCourseModel c : courses) {
	 		   String courseInfo = c.toString();
	 		   
	 		   
	 		   //registeredCourses.add(new RegisteredCourseModel(subject, catalog, sec));
	 		   l.addElement(courseInfo);
	 	   }
	    }
}
