package views;

import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import models.DataTable;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JToolBar;
import java.awt.Color;
import java.awt.Dimension;

public class BookTab {

	public JDesktopPane desk;
	private JTable table;
	private DefaultTableModel model = null;
	private JInternalFrame dataFrame, addSection, deleteSection, updateSection;
	private DataTable bookManager = new DataTable("Book");

	private JCheckBox invertRowColumnCheck;
	private JTextField orderBy;

	public BookTab() {
		initialize();
	}

	private static final String COLUMN_NAMES[] = { "Book ID", "Title", "Author", "Category", "Quantity" };
	private static final String FIELD_NAMES[] = { "id", "title", "author", "category", "quantity" };

	private JTextField idTextfield;
	private JTextField titleTextfield;
	private JTextField authorTextfield;
	private JTextField categoryTextfield;
	private JTextField quantityTextfield;
	private JButton deleteButton;
	private JLabel deleteInstruction;
	private JCheckBox showUpdateButtonCheckBox;
	private JTextField newIdTextfield;
	private JTextField searchTextfield;

	int getQuantity() {
		int quantity = -1;
		try {
			quantity = Integer.parseInt(quantityTextfield.getText());
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(addSection, "Expected integer", "Invalid entry", 0);
		}
		return quantity;
	}

	private void addBook() {
		String id, title, author, category;
		id = idTextfield.getText();
		title = titleTextfield.getText();
		author = authorTextfield.getText();
		category = categoryTextfield.getText();
		int quantity = getQuantity();
		ArrayList<String> fields = new ArrayList<String>();
		fields.add(id);
		fields.add(title);
		fields.add(author);
		fields.add(category);
		fields.add(Integer.toString(quantity));
		if (quantity >= 0) {
			if (!bookManager.add(fields))
				JOptionPane.showMessageDialog(desk, "Insertion failed!", "Fail", 0);
			else
				JOptionPane.showMessageDialog(desk, "Finished!", "Nice", JOptionPane.INFORMATION_MESSAGE);
			idTextfield.setText("");
			titleTextfield.setText("");
			authorTextfield.setText("");
			categoryTextfield.setText("");
		} else {
			JOptionPane.showMessageDialog(desk, "Please input non-negative quantity", "Warn",
					JOptionPane.INFORMATION_MESSAGE);
		}
		quantityTextfield.setText("");
	}

	private void removeBooks() {
		int rows[] = table.getSelectedRows();
		String idx = idTextfield.getText();
		if (rows.length == 0) {
			if (idx.isEmpty()) {
				JOptionPane.showMessageDialog(desk, "No row selected", "Fail", 0);
				return;
			}
		} else if (!idx.isEmpty()) {
			if (JOptionPane.showConfirmDialog(desk, "Notice: Only selected rows (not the id) will be deleted", "Warn",
					JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION)
				return;
		}
		if (rows.length > 1) {
			if (JOptionPane.showConfirmDialog(desk, "You are going to delete multiple rows! Proceed?", null,
					JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
				return;
		}
		String ids[] = new String[rows.length];
		for (int i = 0; i < rows.length; i++)
			ids[i] = table.getModel().getValueAt(rows[i], 0).toString();
		if (!idx.isEmpty() && ids.length == 0) {
			ids = new String[1];
			ids[0] = idx;
		}
		for (String id : ids) {
			int option = JOptionPane.showConfirmDialog(desk, "Do you really want to delete \"" + id + "\"?", null,
					JOptionPane.YES_NO_OPTION);
			if (option == 0) {
				if (!bookManager.remove(Collections.singletonList("id"), Collections.singletonList(id)))
					JOptionPane.showMessageDialog(desk, "Deletion of \"" + id + "\" failed!", "Fail", 0);
			}
		}
		JOptionPane.showMessageDialog(desk, "Finished!", "Nice", JOptionPane.INFORMATION_MESSAGE);
		if (!idx.isEmpty())
			idTextfield.setText("");
	}

	private void updateBook() {
		String id, title, author, category, quantity, newId;
		id = idTextfield.getText();
		title = titleTextfield.getText();
		author = authorTextfield.getText();
		category = categoryTextfield.getText();
		quantity = quantityTextfield.getText();
		newId = newIdTextfield.getText();
		List<String> fields = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		if (!id.isEmpty()) {
			fields.add("id");
			values.add(id);
		} else {
			JOptionPane.showMessageDialog(desk, "Please input book ID", "Fail", 0);
			return;
		}
		if (!title.isEmpty()) {
			fields.add("title");
			values.add(title);
		}
		if (!author.isEmpty()) {
			fields.add("author");
			values.add(author);
		}
		if (!category.isEmpty()) {
			fields.add("category");
			values.add(category);
		}
		if (!newId.isEmpty()) {
			fields.add("id");
			values.add(newId);
		}
		int qty = 0;
		if (!quantity.isEmpty()) {
			fields.add("quantity");
			values.add(quantity);
			qty = Integer.parseInt(quantity);
		}
		if (qty >= 0) {
			if (!bookManager.update(fields, values))
				JOptionPane.showMessageDialog(desk, "Update failed!", "Fail", 0);
			else
				JOptionPane.showMessageDialog(desk, "Finished!", "Nice", JOptionPane.INFORMATION_MESSAGE);
			titleTextfield.setText("");
			authorTextfield.setText("");
			categoryTextfield.setText("");
			newIdTextfield.setText("");
		} else {
			JOptionPane.showMessageDialog(desk, "Please input non-negative quantity", "Warn",
					JOptionPane.INFORMATION_MESSAGE);
		}
		quantityTextfield.setText("");
	}

	private void initialize() {
		desk = new JDesktopPane();
		desk.setBounds(100, 100, MainView.WINDOW_WIDTH, MainView.WINDOW_HEIGHT);

		initDataFrame();
		initAddSection();
		initUpdateSection();
		initDeleteSection();
	}

	private JToolBar toolBar;
	private JLabel orderByLabel;
	private JButton sortButton;
	private boolean isSorting;
	private String order;

	void showSortComponents() {
		boolean state = invertRowColumnCheck.isSelected();
		sortButton.setVisible(state);
		orderByLabel.setVisible(state);
		orderBy.setVisible(state);
		table.setRowSelectionAllowed(!state);
		table.setColumnSelectionAllowed(state);
	}

	void processSearchBar() {
		StringBuffer curKeyword = new StringBuffer(searchTextfield.getText());
		if (curKeyword.equals(lastKeyword))
			return;
		lastKeyword = curKeyword;
		if (curKeyword.indexOf("-") != -1 || curKeyword.indexOf("'") != -1) {
			//https://stackoverflow.com/questions/15206586/getting-attempt-to-mutate-notification-exception
			Runnable resetBar = new Runnable() {
		        @Override
		        public void run() {
		            // your highlight code
		        	JOptionPane.showConfirmDialog(desk, "You are not allowed to type in ', or -", "SQL Injection Alert!",
		        			JOptionPane.CLOSED_OPTION);
		        	searchTextfield.setText("");
		        }
		    };       
		    SwingUtilities.invokeLater(resetBar);
			return;
		}
		if (isSorting)
			bookManager.setOrder(order);
		bookManager.loadDatabase(curKeyword);
	}

	private StringBuffer lastKeyword;

	private void initToolbar() {
		toolBar = new JToolBar("Database 's toolbar");

		toolBar.setOrientation(JToolBar.HORIZONTAL);
		;
		toolBar.setFloatable(false);
		toolBar.setBounds(0, 0, 200, 40);
		toolBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		toolBar.setBackground(new Color(128, 0, 128));
		toolBar.setToolTipText("Common functions: sort & search");
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.LINE_AXIS));
		dataFrame.getContentPane().add(toolBar, BorderLayout.PAGE_START);

		orderBy = new JTextField("...");
		orderBy.setBackground(new Color(192, 192, 192));
		orderBy.setToolTipText("Criteria by which the rows are sorted");
		orderBy.setFont(new Font("Tahoma", Font.PLAIN, 11));
		orderBy.setPreferredSize(new Dimension(70, 30));
		orderBy.setMaximumSize(new Dimension(70, 30));
		orderBy.setEditable(false);

		orderByLabel = new JLabel("Order by:");
		orderByLabel.setForeground(new Color(128, 255, 255));

		invertRowColumnCheck = new JCheckBox("Select column?");
		invertRowColumnCheck.setToolTipText("Invert row/column selection.\r\nMUST select to use the sort function.");
		invertRowColumnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showSortComponents();
			}
		});

		JButton deselectButton = new JButton("X ");
		deselectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				table.getSelectionModel().clearSelection();
				table.removeColumnSelectionInterval(0, table.getColumnCount() - 1);
				orderBy.setText("...");
				isSorting = false;
			}
		});
		deselectButton.setToolTipText("Use this to deselect rows & columns");

		sortButton = new JButton("Sort");
		sortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int col = table.getSelectedColumn();
				if (col == -1) {
					JOptionPane.showMessageDialog(dataFrame, "Please select a column first", "Invalid",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				isSorting = true;
				order = FIELD_NAMES[col];
				bookManager.setOrder(order);
				bookManager.loadDatabase(null);
			}
		});

		searchTextfield = new JTextField();
		searchTextfield.setFocusable(true);
		searchTextfield.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				processSearchBar();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				processSearchBar();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub
			}
		});
		searchTextfield.setToolTipText("Search bar. Auto-refresh as you type");
		searchTextfield.setColumns(20);

		// Add components to toolBar
		toolBar.add(deselectButton);
		toolBar.add(Box.createRigidArea(new Dimension(5, 30)));
		toolBar.add(invertRowColumnCheck);
		toolBar.add(Box.createRigidArea(new Dimension(10, 30)));
		toolBar.add(orderByLabel);
		toolBar.add(Box.createRigidArea(new Dimension(10, 30)));
		toolBar.add(orderBy);
		toolBar.add(Box.createRigidArea(new Dimension(5, 30)));
		toolBar.add(sortButton);
		toolBar.add(Box.createRigidArea(new Dimension(708 / 3, 30)));
		toolBar.add(searchTextfield);

		// Make search bar not too wide
		dataFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				toolBar.remove(searchTextfield);
				toolBar.remove(toolBar.getComponentCount() - 1);
				toolBar.add(Box.createRigidArea(new Dimension((int) (toolBar.getWidth() / 3.0), 30)));
				toolBar.add(searchTextfield);
			}
		});

		// On start up
		toolBar.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				showSortComponents();
			}
		});

		// Select column
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int col = table.getSelectedColumn();
				if (col != -1)
					orderBy.setText(FIELD_NAMES[col]);
			}
		});
	}

	private void initDataFrame() {
		// TABLE
		dataFrame = new JInternalFrame("Book Database", true, false, true, true);
		dataFrame.getContentPane().setLayout(new BorderLayout());
		dataFrame.setBounds(21, 25, 708, 332);
		dataFrame.setVisible(true);

		model = new DefaultTableModel();
		model.setColumnIdentifiers(COLUMN_NAMES);
		bookManager.setModel(model);
		bookManager.setOrder("id");
		bookManager.setFieldNames(FIELD_NAMES);

		table = new JTable();
		table.setFillsViewportHeight(true);
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setFont(new Font("Consolas", Font.PLAIN, 13));
		bookManager.loadDatabase(null);
		dataFrame.getContentPane().add(table);
		//
		JScrollPane bookScroller = new JScrollPane(table);
		bookScroller.setBounds(0, 0, 735, 600);
		bookScroller.setHorizontalScrollBarPolicy(
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		bookScroller.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		dataFrame.getContentPane().add(bookScroller);

		initToolbar();

		desk.add(dataFrame);
	}

	// for interchanging between the two frames
	private JLabel newIdLabel, titleLabel;
	private JButton updateButton;

	private void initAddSection() {
		addSection = new JInternalFrame("Add new books", false, false, false, true);
		addSection.setBounds(21, 378, 708, 224);
		// frame.add(addSection);
		desk.add(addSection);
		addSection.getContentPane().setLayout(null);

		JLabel idLabel = new JLabel("Book ID");
		idLabel.setFont(MainView.defaultFont);
		idLabel.setBounds(10, 11, 107, 26);
		addSection.getContentPane().add(idLabel);

		idTextfield = new JTextField();
		idTextfield.setFont(MainView.defaultFont);
		idTextfield.setBounds(10, 36, 107, 33);
		addSection.getContentPane().add(idTextfield);
		idTextfield.setColumns(10);

		titleLabel = new JLabel("Title");
		titleLabel.setFont(MainView.defaultFont);
		titleLabel.setBounds(168, 11, 505, 26);
		addSection.getContentPane().add(titleLabel);

		titleTextfield = new JTextField();
		titleTextfield.setFont(MainView.defaultFont);
		titleTextfield.setBounds(168, 36, 505, 33);
		titleTextfield.setColumns(10);
		addSection.getContentPane().add(titleTextfield);

		JLabel authorLabel = new JLabel("Author");
		authorLabel.setFont(MainView.defaultFont);
		authorLabel.setBounds(10, 80, 280, 26);
		addSection.getContentPane().add(authorLabel);

		authorTextfield = new JTextField();
		authorTextfield.setFont(MainView.defaultFont);
		authorTextfield.setColumns(10);
		authorTextfield.setBounds(10, 105, 280, 33);
		addSection.getContentPane().add(authorTextfield);

		JLabel categoryLabel = new JLabel("Category");
		categoryLabel.setFont(MainView.defaultFont);
		categoryLabel.setBounds(341, 80, 176, 26);
		addSection.getContentPane().add(categoryLabel);

		categoryTextfield = new JTextField();
		categoryTextfield.setFont(MainView.defaultFont);
		categoryTextfield.setColumns(10);
		categoryTextfield.setBounds(341, 105, 176, 33);
		addSection.getContentPane().add(categoryTextfield);

		JLabel quantityLabel = new JLabel("Quantity");
		quantityLabel.setFont(MainView.defaultFont);
		quantityLabel.setBounds(566, 80, 107, 26);
		addSection.getContentPane().add(quantityLabel);

		quantityTextfield = new JTextField();
		quantityTextfield.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!showUpdateButtonCheckBox.isSelected())
					getQuantity();
			}
		});
		quantityTextfield.setFont(MainView.defaultFont);
		quantityTextfield.setColumns(10);
		quantityTextfield.setBounds(566, 105, 107, 33);
		addSection.getContentPane().add(quantityTextfield);

		JButton addButton = new JButton("Add!");
		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addBook();
			}
		});
		addButton.setFont(MainView.defaultFont);
		addButton.setBounds(217, 149, 95, 35);
		addSection.getContentPane().add(addButton);
		addSection.setVisible(true);

		updateButton = new JButton("Update\r\n");
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateBook();
			}
		});
		updateButton.setFont(MainView.defaultFont);
		updateButton.setBounds(350, 149, 93, 35);
		updateButton.setVisible(false);
		addSection.getContentPane().add(updateButton);

		newIdTextfield = new JTextField();
		newIdTextfield.setFont(MainView.defaultFont);
		newIdTextfield.setColumns(10);
		newIdTextfield.setVisible(false);
		addSection.getContentPane().add(newIdTextfield);

		newIdLabel = new JLabel("New ID");
		newIdLabel.setFont(MainView.defaultFont);
		newIdLabel.setVisible(false);
		addSection.getContentPane().add(newIdLabel);
	}

	private void initUpdateSection() {
		updateSection = new JInternalFrame("How to update?");
		updateSection.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				try {
					updateSection.requestFocus();
					updateSection.setSelected(true);
				} catch (PropertyVetoException e1) {
					e1.printStackTrace();
				}
			}
		});
		updateSection.setIconifiable(true);
		updateSection.setBounds(755, 237, 213, 365);
		updateSection.getContentPane().setLayout(null);
		desk.add(updateSection);

		showUpdateButtonCheckBox = new JCheckBox("<html>Toggle 'Update' button</html>\r\n\r\n");
		showUpdateButtonCheckBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean state = showUpdateButtonCheckBox.isSelected();
				if (state) {
					titleLabel.setBounds(270, 11, 505 - 102, 26);
					titleTextfield.setBounds(270, 36, 403, 33);
					newIdTextfield.setBounds(137, 36, 107, 33);
					newIdLabel.setBounds(137, 11, 107, 26);
				} else {
					titleLabel.setBounds(168, 11, 505, 26);
					titleTextfield.setBounds(168, 36, 505, 33);
				}
				updateButton.setVisible(state);
				newIdLabel.setVisible(state);
				newIdTextfield.setVisible(state);
			}
		});
		showUpdateButtonCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		showUpdateButtonCheckBox.setBounds(10, 295, 191, 34);
		updateSection.getContentPane().add(showUpdateButtonCheckBox);

		JTextArea updateHelptext = new JTextArea();
		updateHelptext.setFont(MainView.defaultFont);
		updateHelptext.setWrapStyleWord(true);
		updateHelptext.setLineWrap(true);
		updateHelptext.setText(
				"1. To update, you must:\r\n + Fill in the \"Book ID\" field \r\n + Fill in fields that you want to update with the desired values.\r\n\r\n2. Blank fields will not be affected.\r\n\r\n3. Invalid book ID will cause in an error.\r\n\r\n4. Tick the box below to see the \"Update\" button.\r\n");
		updateHelptext.setEditable(false);
		updateHelptext.setBounds(10, 11, 183, 277);
		updateSection.getContentPane().add(updateHelptext);
		updateSection.setVisible(true);
	}

	private void initDeleteSection() {
		deleteSection = new JInternalFrame("Delete book(s)\r\n");
		deleteSection.setBounds(755, 25, 213, 182);
		desk.add(deleteSection);
		deleteSection.getContentPane().setLayout(null);
		deleteSection.setIconifiable(true);
		try {
			deleteSection.setIcon(true);
		} catch (Exception e) {
			System.out.println("Ouch!");
		}

		deleteInstruction = new JLabel();
		deleteInstruction.setHorizontalAlignment(SwingConstants.CENTER);
		deleteInstruction.setBounds(10, 0, 183, 85);
		deleteSection.getContentPane().add(deleteInstruction);
		deleteInstruction.setText(
				"<html>Select a row (or rows) OR input the book ID, then press the button below to delete</html>");
		deleteInstruction.setFont(MainView.defaultFont);

		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeBooks();
			}
		});
		deleteButton.setBounds(56, 96, 89, 36);
		deleteButton.setFont(MainView.defaultFont);
		deleteSection.getContentPane().add(deleteButton);
		deleteSection.setVisible(true);
	}
}
