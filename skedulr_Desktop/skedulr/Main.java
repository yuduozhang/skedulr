package skedulr;

import javax.swing.JFrame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
