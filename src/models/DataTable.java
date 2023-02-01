package models;
//update: changed String to StringBuffer for efficiency
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class DataTable {
	public final static String DBNAME = "LibMgmt";
    public final static String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public final static String URL = "jdbc:sqlserver://localhost:1433;databaseName=" + DBNAME + ";encrypt=true;trustServerCertificate=true";
    public final static String USERNAME = "sa";
    public final static String PASSWORD = "1";

    private String tableName;
    private String[] fieldNames;
    private StringBuffer order;
    private DefaultTableModel model;
    
	public String[] getFieldNames() {
		return fieldNames;
	}

	public void setFieldNames(String[] fieldNames) {
		this.fieldNames = fieldNames;
	}

	public StringBuffer getOrder() {
		return order;
	}
	
	public void setOrder(String order) {
		this.order = new StringBuffer(order);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DefaultTableModel getModel() {
		return model;
	}

	public void setModel(DefaultTableModel model) {
		this.model = model;
	}

	public DataTable(String tableName) {
		setTableName(tableName);
		connection = getConnection();
		System.out.println("Connected! " + connection);
	}
	
	private Connection connection = null;
    
    private Connection getConnection() {
        try {
            Class.forName(DRIVER);
    		System.out.println("Registered successfully");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Could not connect");
            e.printStackTrace();
        }
        return null;
    }

    public void loadDatabase(StringBuffer filter) {
    	try {
    		model.setRowCount(0); //reset 
    		Statement statement = connection.createStatement();
    		StringBuffer command = new StringBuffer("SELECT * FROM " + tableName);
    		if (filter != null && !filter.isEmpty() && fieldNames.length > 0) {
    			command.append(" WHERE ");
    			for (String field : fieldNames) {
    				if (field != fieldNames[0])
    					command.append("OR ");
    				command.append(field + " like ");
    				command.append("N'%");
    				command.append(filter);
    				command.append("%' ");
    			}
    		}
			if (order != null && !order.isEmpty()) command.append(" ORDER BY " + order);
    		System.out.println(command);
    		ResultSet result = statement.executeQuery(command.toString());
    		ResultSetMetaData metadata = result.getMetaData();
    		int numColumns = metadata.getColumnCount();
    		String args[] = new String[numColumns];
    		while (result.next()) {
    			for (int i = 0; i < numColumns; i++)
    				args[i] = result.getString(i+1);
    			model.addRow(args);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

    public boolean add(List<String> args) {
    	try {
    		if (args.size() <= 1) return false;
    		StringBuffer command = new StringBuffer("INSERT INTO " + tableName + " VALUES(");
    		for (int i = 0; i < args.size(); i++) {
    			command.append("?");
    			if (i+1 < args.size()) command.append(", ");
    			else command.append(")");
    		}
    		PreparedStatement statement = connection.prepareStatement(command.toString());
    		for (int i = 0; i < args.size(); i++)
    			statement.setString(i+1, args.get(i));    		
    		if (statement.executeUpdate() > 0) {
//    			model.addRow(args.toArray());
    			loadDatabase(null);
    			return true;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
	}
    
    public boolean remove(List<String> keys, List<String> values) {
    	try {
    		if (keys.isEmpty()) return false;
    		int n = keys.size();
    		StringBuffer command = new StringBuffer("DELETE FROM " + tableName + " WHERE ");// id = ?";
    		for (int i = 0; i < n; i++) {
    			command.append(keys.get(i) + " = ?");
    			if (i+1 < n)
    				command.append(" AND ");
    		}
    		PreparedStatement statement = connection.prepareStatement(command.toString());
    		for (int i = 0; i < n; i++)
    			statement.setString(i+1, values.get(i));
    		if (statement.executeUpdate() > 0) {
    			loadDatabase(null);
    			return true;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public boolean update(List<String> fields, List<String> values) {
    	try {
    		if (fields.size() <= 1) return false;
    		StringBuffer command = new StringBuffer("UPDATE " + tableName + " SET ");
    		for (int i = 1; i < fields.size(); i++) {
    			if (i > 1) command.append(", ");
    			command.append(fields.get(i) + " = ?");    			
    		}
    		assert(fields.get(0).equals("id"));
    		command.append(" WHERE id = '" + values.get(0) + "'");
    		PreparedStatement statement = connection.prepareStatement(command.toString());
    		for (int i = 1; i < fields.size(); i++)
    			statement.setString(i, values.get(i));
    		if (statement.executeUpdate() > 0) {
    			loadDatabase(null);
    			return true;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return false;
    }
}
