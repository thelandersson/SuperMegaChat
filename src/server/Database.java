package server;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * Database is an interface to the college application database, it uses JDBC to
 * connect to a SQLite3 file.
 * time DATETIME DEFAULT CURRENT_TIMESTAMP,
 */
public class Database {

	/**
	 * The database connection.
	 */
	private Connection conn;

	/**
	 * Creates the database interface object. Connection to the database is
	 * performed later.
	 */
	public Database() {
		conn = null;
	}

	/**
	 * Opens a connection to the database, using the specified filename (if we'd
	 * used a traditional DBMS, such as PostgreSQL or MariaDB, we would have
	 * specified username and password instead).
	 */
	public boolean openConnection(String filename) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + filename);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Closes the connection to the database.
	 */
	public void closeConnection() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks if the connection to the database has been established
	 * 
	 * @return true if the connection has been established
	 */
	public boolean isConnected() {
		return conn != null;
	}

	public boolean login(String username, String password) {
		String query = "SELECT  username, password " + "FROM    users " + "WHERE username = ? AND password = ?";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	public boolean addUser(String username, String password) {
		String query = "INSERT INTO users(username, password) VALUES (?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ps.setString(2, password);
			ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// INSERT INTO messages(username, message, time)
	// VALUES("anton", "hejsan", "2017-02-22");
	public void addMessage(String username, String message, String time) {
		String query = "INSERT INTO messages (username, message, time, timeLong) VALUES (?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ps.setString(2, message);
			ps.setString(3, time);
			ps.setLong(4, System.currentTimeMillis());
			ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public List<Message> getAllMessages() {
		List<Message> found = new LinkedList<Message>();
		String query = "SELECT  username, message, time " + "FROM    messages ";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				String usr = rs.getString("username");
				String msg = rs.getString("message");
				String time = rs.getString("time");
			
				found.add(new Message(1, usr, msg, time));
			}

		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return found;
	}
	
	public long getOldestTime() {
		long oldestTime = 0;
		String query = "SELECT  timeLong " + "FROM    messages ";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				oldestTime = rs.getLong("timeLong");
				
				
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return oldestTime;
		
	}
	public static void main(String[] args) {
		Database db = new Database();
		db.openConnection("project.db");
		db.getOldestTime();
	}

	public void removeOldest() {
		String query = "DELETE FROM messages WHERE id = (SELECT MIN(id) FROM messages)";
		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}

	
}
