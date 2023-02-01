package views;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import controllers.AuthController;

import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginView {

	private JFrame frame;
	private JTextField usernameTextfield;
	private JTextField passwordTextfield;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginView window = new LoginView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginView() {
		initialize();
	}
	
	public void close() {
		frame.setVisible(false); //you can't see me!
		frame.dispose(); //Destroy the JFrame object
	}

	void viewMain() {
//		MainView.main();
		MainView.main();
		close();
	}

	void checkLogin() {
		String username = usernameTextfield.getText();
		String password = passwordTextfield.getText();
		boolean feedback = AuthController.login(username, password);
		if (feedback) viewMain();
		else JOptionPane.showMessageDialog(frame, "Wrong credentials!", "title", 0);
	};
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Book management app for school libraries - Login");
		frame.setBounds(100, 100, 690, 472);
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
		
		JButton btnNewButton = new JButton("Login!");
		btnNewButton.setMnemonic('l');
		btnNewButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) checkLogin();
			}
		});
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkLogin();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton.setBounds(289, 344, 131, 44);
		frame.getContentPane().add(btnNewButton);
	}

}
