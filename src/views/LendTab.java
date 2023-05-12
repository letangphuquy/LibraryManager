package views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.DataTable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Timestamp;

import java.util.concurrent.TimeUnit;

public class LendTab {

	public JSplitPane pane;

	private JTable table;
	private DefaultTableModel model;
	private static final String COLUMN_NAMES[] = { "Student ID", "Book ID", "Lent Time" };
	private DataTable lendManager = new DataTable("Lend");
	private Date currrenDate;

	private JSplitPane splitPane;

	public LendTab() {
		initialize();
	}

	private void resetTable() {
		table.setModel(model);
		table.setForeground(null);
		table.setBackground(Color.WHITE);
	}

	private String student, book;

	private boolean getIDS() {
		student = studentTextfield.getText();
		book = bookTextfield.getText();
		if (student.isEmpty() || book.isEmpty()) {
			JOptionPane.showMessageDialog(pane, "Student and book ID must not be blank!", "Invalid input", 0);
			return false;
		}
		return true;
	}

	void lendBook() {
		resetTable();
		if (!getIDS())
			return;
		ArrayList<String> row = new ArrayList<String>();
		row.add(student);
		row.add(book);
		row.add(timeTextfield.getText());
		if (!lendManager.add(row))
			JOptionPane.showMessageDialog(pane, "Lending failed!", "Fail", 0);
		else
			JOptionPane.showMessageDialog(pane, "Finished!", "Nice", JOptionPane.INFORMATION_MESSAGE);
		studentTextfield.setText(null);
		bookTextfield.setText(null);
	}

	void returnBook() {
		resetTable();
		if (!getIDS())
			return;
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		keys.add("studentID");
		keys.add("bookID");
		values.add(student);
		values.add(book);
		int option = JOptionPane.showConfirmDialog(pane, "Confirm student '" + student + "' returns book '" + book + "'?",
				null, JOptionPane.YES_NO_OPTION);
		if (option != JOptionPane.YES_OPTION)
			return;
		if (lendManager.remove(keys, values))
			JOptionPane.showMessageDialog(pane, "Finished!", "Nice", JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(pane, "Returning failed! Check for wrong information", "Fail", 0);
		studentTextfield.setText(null);
		bookTextfield.setText(null);
	}

	private void getInitDimension(Component C, int width[], int height[]) {
		width[0] = C.getWidth();
		height[0] = C.getHeight();
	}

	int getSize(int initSize, int initRange, int newRange) {
		return Math.round((float) initSize * newRange / initRange);
	}

	private JPanel lendPanel;
	private JLabel studentLabel, bookLabel, timeLabel;
	private JTextField studentTextfield;
	private JTextField bookTextfield;
	private JTextField timeTextfield;
	private JButton lendButton, returnButton;

	private int lendPanelWidth = 0, lendPanelHeight = 0;

	void setLayoutLendPanel() {
		if (lendPanelHeight == 0) {
			int refWidth[] = new int[1], refHeight[] = new int[1];
			getInitDimension(lendPanel, refWidth, refHeight);
			lendPanelWidth = refWidth[0];
			lendPanelHeight = refHeight[0];
		}
		int curWidth = lendPanel.getWidth();
		int curHeight = lendPanel.getHeight();

		// note: before writing the function
		int x = (int) (curWidth * 36.0 / lendPanelWidth);
		int width = (int) (curWidth * 84.0 / lendPanelWidth);
		int y = (int) (curHeight * 64.0 / lendPanelHeight);
		int height = (int) (curHeight * 40.0 / lendPanelHeight);

		studentLabel.setBounds(x, y, width, height);
		bookLabel.setBounds(x, 2 * y, width, height);

		x = (int) (curWidth * 132.0 / lendPanelWidth);
		width = (int) (curWidth * 180.0 / lendPanelWidth);
		studentTextfield.setBounds(x, y, width, height);
		bookTextfield.setBounds(x, 2 * y, width, height);

		int fontSize = Math.round(14 * curHeight / lendPanelHeight);
		Font font = new Font("Tahoma", Font.PLAIN, fontSize);
		studentLabel.setFont(font);
		bookLabel.setFont(font);
		studentTextfield.setFont(font);
		bookTextfield.setFont(font);

		lendButton.setFont(font);
		returnButton.setFont(font);
		timeLabel.setFont(font);
		timeTextfield.setFont(font);

		x = (int) (curWidth * 80.0 / lendPanelWidth);
		y = (int) (curHeight * 195.0 / lendPanelHeight);
		width = (int) (curWidth * 87.0 / lendPanelWidth);
		height = (int) (curHeight * 33.0 / lendPanelHeight);

		lendButton.setBounds(x, y, width, height);
		x = (int) (curWidth * 200.0 / lendPanelWidth);
		returnButton.setBounds(x, y, width, height);

		x = (int) (curWidth * 80.0 / lendPanelWidth);
		y = (int) (curHeight * 265.0 / lendPanelHeight);
		width = (int) (curWidth * 275.0 / lendPanelWidth);
		height = (int) (curHeight * 25.0 / lendPanelHeight);

		timeTextfield.setBounds(x, y, width, height);
		x = (int) (curWidth * 25.0 / lendPanelWidth);
		width = (int) (curWidth * 55.0 / lendPanelWidth);
		timeLabel.setBounds(x, y, width, height);
	}

	private JPanel listPanel;
	private JComboBox<String> comboBox;
	private JCheckBox isDescendingCheckBox;
	private JLabel sortbyLabel;
	private JButton sortButton;
	private JButton listButton;

	private int listPanelWidth = 0, listPanelHeight = 0;

	void setLayoutListPanel() {
		if (listPanelHeight == 0) {
			int refWidth[] = new int[1], refHeight[] = new int[1];
			getInitDimension(listPanel, refWidth, refHeight);
			listPanelWidth = refWidth[0];
			listPanelHeight = refHeight[0];
		}
		int fontSize = Math.round(14 * listPanel.getHeight() / listPanelHeight);
		Font font = new Font("Tahoma", Font.PLAIN, fontSize);
		sortbyLabel.setFont(font);
		listButton.setFont(font);
		sortButton.setFont(font);
		fontSize = Math.round(13 * listPanel.getHeight() / listPanelHeight);
		isDescendingCheckBox.setFont(new Font("Tahoma", Font.PLAIN, fontSize));

		int curWidth = listPanel.getWidth();
		int curHeight = listPanel.getHeight();

		listButton.setBounds(getSize(81, listPanelWidth, curWidth), getSize(218, lendPanelHeight, curHeight),
				getSize(214, lendPanelWidth, curWidth), getSize(47, lendPanelHeight, curHeight));
		sortButton.setBounds(getSize(273, listPanelWidth, curWidth), getSize(53, lendPanelHeight, curHeight),
				getSize(70, lendPanelWidth, curWidth), getSize(65, lendPanelHeight, curHeight));
		comboBox.setBounds(getSize(147, listPanelWidth, curWidth), getSize(58, lendPanelHeight, curHeight),
				getSize(107, lendPanelWidth, curWidth), getSize(32, lendPanelHeight, curHeight));
		isDescendingCheckBox.setBounds(getSize(36, listPanelWidth, curWidth), getSize(86, lendPanelHeight, curHeight),
				getSize(110, lendPanelWidth, curWidth), getSize(32, lendPanelHeight, curHeight));
		sortbyLabel.setBounds(getSize(36, listPanelWidth, curWidth), getSize(53, lendPanelHeight, curHeight),
				getSize(87, lendPanelWidth, curWidth), getSize(39, lendPanelHeight, curHeight));
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {
		pane = new JSplitPane();
		pane.setBounds(0, 0, MainView.WINDOW_WIDTH, MainView.WINDOW_HEIGHT);
		pane.setResizeWeight(0.75);

		model = new DefaultTableModel();
		lendManager.setModel(model);
		lendManager.setOrder("time DESC");
		model.setColumnIdentifiers(COLUMN_NAMES);

		JPanel rightPart = new JPanel();
		rightPart.setLayout(new GridLayout());
		pane.setRightComponent(rightPart);

		table = new JTable();
		table.setModel(model);
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFont(new Font("Consolas", Font.PLAIN, 13));
		lendManager.loadDatabase(null);
		rightPart.add(table);

		JScrollPane scroller = new JScrollPane(table,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		rightPart.add(scroller);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pane.setLeftComponent(splitPane);
		// Lend Panel
		lendPanel = new JPanel();
		lendPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setLayoutLendPanel();
			}
		});
		splitPane.setLeftComponent(lendPanel);
		lendPanel.setLayout(null);

		studentLabel = new JLabel("Student ID");
		lendPanel.add(studentLabel);

		studentTextfield = new JTextField();
		lendPanel.add(studentTextfield);
		studentTextfield.setColumns(10);

		bookLabel = new JLabel("Book ID");
		lendPanel.add(bookLabel);

		bookTextfield = new JTextField();
		bookTextfield.setColumns(10);
		lendPanel.add(bookTextfield);

		lendButton = new JButton("Lend");
		lendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lendBook();
			}
		});
		lendPanel.add(lendButton);

		returnButton = new JButton("Return");
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				returnBook();
			}
		});
		lendPanel.add(returnButton);

		timeTextfield = new JTextField();
		timeTextfield.setEditable(false);
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currrenDate = new Date();
				timeTextfield.setText(formatter.format(currrenDate));
			}
		}).start();
		timeTextfield.setColumns(10);
		lendPanel.add(timeTextfield);

		timeLabel = new JLabel("Time");
		lendPanel.add(timeLabel);

		JButton resetButton = new JButton("-");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pane.setDividerLocation(0.4);
				splitPane.setDividerLocation(0.5);
			}
		});
		resetButton.setBounds(0, 0, 27, 28);
		lendPanel.add(resetButton);
		// List Panel
		listPanel = new JPanel();
		listPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setLayoutListPanel();
			}
		});
		splitPane.setRightComponent(listPanel);
		listPanel.setLayout(null);

		listButton = new JButton("List overdue lend\r\ns");
		listButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				model.getRowCount();
				table.setBackground(Color.YELLOW);
				table.setForeground(Color.RED);
				DefaultTableModel tempModel = new DefaultTableModel();
				lendManager.setOrder("time");
				lendManager.loadDatabase(null);
				tempModel.setColumnIdentifiers(COLUMN_NAMES);
				int numColumn = table.getColumnCount();
				for (int i = 0; i < table.getRowCount(); i++) {
					Object[] row = new Object[numColumn];
					for (int j = 0; j < numColumn; j++)
						row[j] = table.getValueAt(i, j);
					System.out.println(row[numColumn - 1]);
					Timestamp lent = Timestamp.valueOf((String) row[numColumn - 1]);
					long diffInMillies = currrenDate.getTime() - lent.getTime();
					long diff = (TimeUnit.valueOf("DAYS")).convert(diffInMillies, TimeUnit.MILLISECONDS);
					// diff = (TimeUnit.valueOf("MINUTES")).convert(diffInMillies,
					// TimeUnit.MILLISECONDS);
					System.out.println("diff = " + diff);
					if (diff <= 7)
						break;
					tempModel.addRow(row);
				}
				table.setModel(tempModel);
			}
		});
		listPanel.add(listButton);

		sortButton = new JButton("Sort");
		sortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetTable();
				String order = (String) comboBox.getSelectedItem();
				if (isDescendingCheckBox.isSelected())
					order += " DESC";
				lendManager.setOrder(order);
				lendManager.loadDatabase(null);
			}
		});
		listPanel.add(sortButton);

		comboBox = new JComboBox<String>();
		comboBox.addItem("studentID");
		comboBox.addItem("bookID");
		comboBox.addItem("time");
		listPanel.add(comboBox);

		isDescendingCheckBox = new JCheckBox("Descending?\r\n");
		listPanel.add(isDescendingCheckBox);

		sortbyLabel = new JLabel("Sort by:");
		listPanel.add(sortbyLabel);
	}
}
