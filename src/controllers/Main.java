package controllers;

import java.awt.EventQueue;

import javax.swing.JFrame;

import views.LoginView;
import views.MainView;

//Purpose: Switch between different views

public class Main {
	public enum Window { LOGIN, MAIN };

	public static JFrame frame = null; //the currently visible
	
	static void closeView() {
		frame.setVisible(false);
		frame.dispose();
		frame = null;
	}
	
	public static void openView(Window view) {
		if (frame != null) closeView();
		EventQueue.invokeLater(new Runnable() {
			//From auto-generated code
			//https://stackoverflow.com/questions/22534356/java-awt-eventqueue-invokelater-explained
			public void run() {
				try {
					switch (view) {
					case LOGIN:
						frame = (new LoginView()).frame;
						break;
					case MAIN:
						frame = (new MainView()).frame;
						break;
					default:
						System.out.println("ERROR: View name not defined!");
					}
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String... args) {
		openView(Window.LOGIN);
	}
}