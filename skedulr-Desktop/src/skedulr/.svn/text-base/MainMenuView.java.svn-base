package skedulr;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainMenuView extends JPanel implements IView {
	   private SkedulrModel model;
	   
	   SelectCoursesView selectCourses;
	   MyCoursesView myCourses; 
       
	   public MainMenuView(SkedulrModel aModel) {
			super();
			this.model = aModel;
			selectCourses = new SelectCoursesView(model);
			myCourses = new MyCoursesView(model);
			this.layoutView();
			this.registerControllers();

			// Add a this view as a listener to the model
			this.model.addView(this);
		}
		
	    /**
	     * What to do when the model changes.
	     */
	    public void updateView() {
            // A lot to do later
	    }

	   public void layoutView() {
		   this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		   this.add(selectCourses);
		   this.add(myCourses);
	   }
	   
		private void registerControllers() {
			// Add a controller to interpret user actions in the base text field
		}
}
