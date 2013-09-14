package skedulr;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.*;




public class LoginView extends JPanel implements IView{
   private SkedulrModel model;
   private JTextField userIDField = new JTextField(10);
   private JPasswordField passwordField = new JPasswordField(10);
   private JButton register = new JButton("Register");
   private JButton login = new JButton("Login");
   private JButton cancel = new JButton("Cancel");
   private JLabel errorMessageJLabel = new JLabel("Please enter your userID and password.");
    
	public LoginView(SkedulrModel aModel) {
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
    	if(this.model.loginError == 1){
    		errorMessageJLabel.setText("Invalid userID or password, please register.");
    	}
    	else if(this.model.loginError == 2){
    		errorMessageJLabel.setText("Please enter your userID.");
    	}
    	else if(this.model.loginError == 3){
    		errorMessageJLabel.setText("Please enter your password.");
    	}
    	else if(this.model.loginError == 4){
    		errorMessageJLabel.setText("Network Error.");
    	}
    }

   public void layoutView() {
	  //this.setLayout(new FormLayout());
      this.setLayout(new GridLayout(4, 1)); 
      this.add(errorMessageJLabel);
      this.add(this.groupComponents2(new JLabel("UserID:"), this.userIDField));
      this.add(this.groupComponents2(new JLabel("Password:"), this.passwordField));
      this.add(this.groupComponents(this.register, this.login, this.cancel));
   }
   
   @SuppressWarnings("unused")
   private Box groupComponents(final JButton register, final JButton login, final JButton cancel ) {
		Box group = Box.createHorizontalBox();
		group.add(register);
		group.add(login);
		group.add(cancel);

		Dimension d = register.getPreferredSize();
		d.width = Math.max(login.getPreferredSize().width,
				cancel.getPreferredSize().width);
		login.setPreferredSize(d);
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
		this.login.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				String userID = userIDField.getText();
				String password = new String(passwordField.getPassword());
				model.login(userID, password);
				
				if (model.loginError == 0){
					// Error Handle should be added later
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
					mainMenuFrame.setSize(530, 600);
					mainMenuFrame.setVisible(true);
					  
				}
			}
		}); 
		
		this.register.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				
				java.awt.Window win[] = java.awt.Window.getWindows(); 
				for(int i=0;i<win.length;i++){ 
				win[i].dispose(); 
				} 
				
				model.views.clear();	
				CreateAccountView createAccountView = new CreateAccountView(model);
				
				JFrame frameRegister = new JFrame("Skedulr: Register");
				frameRegister.getContentPane().add(createAccountView);
				frameRegister.setLocationRelativeTo(null);
				frameRegister.pack();
				frameRegister.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frameRegister.setLocation(500,300);
				frameRegister.setVisible(true);
				frameRegister.setSize(400,200);
				
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
