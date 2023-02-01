package controllers;

public class Login {
	public Login() {
		
	}
	public static boolean login(String username, String password) {
		return (username.equals("sa") && password.equals("1"));
	}
}
