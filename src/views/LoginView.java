package views;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import controllers.Login;
import controllers.Main;
import controllers.Main.Window;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginView {

	public JFrame frame;
	private JTextField usernameTextfield;
	private JTextField passwordTextfield;

	public LoginView() {
		initialize();
	}
	
	void checkLogin() {
		String username = usernameTextfield.getText();
		String password = passwordTextfield.getText();
		boolean feedback = Login.login(username, password);
		if (feedback) Main.openView(Window.MAIN);
		else JOptionPane.showMessageDialog(frame, "Wrong credentials!", "Failed", 0);
	};
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Book management app for school libraries - Login");
		frame.setBounds(100, 100, 720, 480);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel usernameLabel = new JLabel("Username");
		usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
		usernameLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		usernameLabel.setBounds(112, 197, 131, 44);
		frame.getContentPane().add(usernameLabel);
		
		usernameTextfield = new JTextField();
		usernameTextfield.setBounds(265, 197, 294, 44);
		frame.getContentPane().add(usernameTextfield);
		usernameTextfield.setColumns(10);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		passwordLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		passwordLabel.setBounds(112, 271, 131, 44);
		frame.getContentPane().add(passwordLabel);
		
		passwordTextfield = new JPasswordField();
		passwordTextfield.setColumns(10);
		passwordTextfield.setBounds(265, 271, 294, 44);
//		passwordTextfield.setEc
		frame.getContentPane().add(passwordTextfield);
		
		JLabel title = new JLabel("PQ Library manager");
		title.setFont(new Font("Tahoma", Font.PLAIN, 24));
		title.setBounds(229, 67, 240, 71);
		frame.getContentPane().add(title);
		
		//https://stackoverflow.com/questions/440209/how-do-i-assign-enter-as-the-trigger-key-of-all-jbuttons-in-my-java-application
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);
		JButton loginButton = new JButton("Login!");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkLogin();
			}
		});
		loginButton.setMnemonic('l');
		loginButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		loginButton.setBounds(289, 344, 131, 44);
		frame.getContentPane().add(loginButton);
	}

}
