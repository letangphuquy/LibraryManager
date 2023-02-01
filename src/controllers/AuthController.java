package controllers;

public class AuthController {
	public AuthController() {
		
	}
	public static boolean login(String username, String password) {
		return (username.equals("sa") && password.equals("1"));
	}
}
