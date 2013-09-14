package skedulr;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



public class CreateAccountView extends JPanel implements IView{
	   private SkedulrModel model;
	   private JTextField userIDField = new JTextField(10);
	   private JTextField passwordField = new JPasswordField(10);
	   private JTextField surnameField = new JTextField(10);
	   private JTextField giveNamesField = new JTextField(10);
	   private JLabel errorMessageJLabel = new JLabel("Please enter your personal information.");
	   
	   private JButton submit = new JButton("Submit");
	   private JButton cancel = new JButton("Cancel");
	    
	   
	   public CreateAccountView(SkedulrModel aModel) {
			super();
			this.model = aModel;
			this.layoutView();
			this.registerControllers();

			// Add a this view as a listener to the model
			this.model.addView(this);
		}
		
	    /**
	     * What to do when the model changes.
	     */
	    public void updateView() {
	    	if(this.model.createAccountError == 1){
	    		errorMessageJLabel.setText("Sorry, this userID has been used.");
	    	}
	    	else if(this.model.createAccountError == 2){
	    		errorMessageJLabel.setText("Network Error.");
	    	}
	    	else if(this.model.createAccountError == 3){
	    		errorMessageJLabel.setText("Invalid Personal Information.");
	    	}
	    }

	   public void layoutView() {
		  //this.setLayout(new FormLayout());
	      this.setLayout(new GridLayout(6, 1)); 
	      this.add(errorMessageJLabel);
	      this.add(this.groupComponents2(new JLabel("UserID:"), this.userIDField));
	      this.add(this.groupComponents2(new JLabel("Password:"), this.passwordField));
	      this.add(this.groupComponents2(new JLabel("Given Names:"), this.giveNamesField));
	      this.add(this.groupComponents2(new JLabel("Surname:"), this.surnameField));
	      this.add(this.groupComponents(this.submit, this.cancel));
	   }
	   
	   @SuppressWarnings("unused")
	   private Box groupComponents(final JButton submit, final JButton cancel ) {
			Box group = Box.createHorizontalBox();
			group.add(submit);
			group.add(cancel);

			Dimension d = submit.getPreferredSize();
			d.width = Math.max(submit.getPreferredSize().width,
					cancel.getPreferredSize().width);
			submit.setPreferredSize(d);
			cancel.setPreferredSize(d);

			return group;
		}
	   
	   private Box groupComponents2(final JLabel label, final JTextField text) {
			Box group = Box.createHorizontalBox();
			group.add(label);
			group.add(text);

			Dimension d = label.getPreferredSize();
			d.width = Math.max(label.getPreferredSize().width,
					cancel.getPreferredSize().width);
			label.setPreferredSize(d);
			text.setPreferredSize(d);
			return group;
		}
	   
		private void registerControllers() {
			// Add a controller to interpret user actions in the base text field
			this.submit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt) {
			
						String userID = userIDField.getText();
						String password = new String(((JPasswordField) passwordField).getPassword());
						String surname = surnameField.getText();
						String givenNames = giveNamesField.getText();
						model.createAccount(userID, password, surname, givenNames);
						
						if (
							model.createAccountError == 0
							){	
							
							java.awt.Window win[] = java.awt.Window.getWindows(); 
							for(int i=0;i<win.length;i++){ 
							win[i].dispose(); 
							} 
							
							model.views.clear();
							
							MainMenuView mainMenuView = new MainMenuView(model);
							//MyCoursesView myCourses = new MyCoursesView(model);
							
							JFrame mainMenuFrame = new JFrame("Skedulr");
						
							mainMenuFrame.getContentPane().add(mainMenuView);
							mainMenuFrame.setLocationRelativeTo(null);
							mainMenuFrame.pack();
							mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							mainMenuFrame.setVisible(true);
							
							// make the frame half the height and width
							Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
							int height = screenSize.height;
							int width = screenSize.width;

							// here's the part where i center the jframe on screen
							mainMenuFrame.setLocation(300,50);
							  
							mainMenuFrame.setVisible(true);
							mainMenuFrame.setSize(530, 600);
						}
				}
			});
		

			// Add a controller to interpret user actions in the height text field
			this.cancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
	                System.exit(0);
				}
			});
		}
}
