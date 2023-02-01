package views;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainView {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 700;

	public static final Font defaultFont = new Font("Tahoma", Font.PLAIN, 14);

	private JFrame frame;
	private JTabbedPane tabs;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
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
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Library Manager");
		frame.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);

		tabs = new JTabbedPane();
		tabs.setBounds(0, 0, WINDOW_WIDTH - 15, WINDOW_HEIGHT - 40);
		BookTab bookTab = new BookTab();
		tabs.insertTab("Books management", null, bookTab.desk, "View, add, delete or update books in the library", 0);
		LendTab lendTab = new LendTab();
		tabs.insertTab("Lending list", null, lendTab.pane, "List of lent books & students", 1);

		frame.add(tabs);
	}

}
