package eus.ehu.dsiweb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.DBUser;

public class DBConnection {
	
	/**
	 * Method to create DB Connection
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Connection createConnection() throws Exception {
		Connection con = null;
		try {
			Class.forName(Constants.DB_DRIVER_CLASS);
			con = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORDD);
		} catch (Exception e) {
			throw e;
		}
		return con;
	}

	//////////////////////////////
	// USUARIO
	//////////////////////////////
	
	public static boolean checkLogin(String username, String password) throws Exception {
		boolean isUserAvailable = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM user ";
					query += " WHERE (login = '" + username + "' OR email = '" + username +"')";
					query += " AND password=" + "'" + password + "'";
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				isUserAvailable = true;
			}
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return isUserAvailable;
	}
	
	public static DBUser getUserInfo(String username, String password) throws Exception {
		List<DBUser> list = null;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM user ";
				query += " WHERE (login = '" + username + "' OR email = '" + username +"')";
				query += " AND password=" + "'" + password + "'";
			ResultSet rs = stmt.executeQuery(query);
			
			list = new LinkedList<DBUser>();
			while(rs.next()){
				DBUser user = new DBUser();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setLogin(rs.getString("login"));
				user.setEmail(rs.getString("email"));
				list.add(user);
			}
			
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return list.get(0);
	}
	
	public static DBUser getUserInfo(Integer id) throws SQLException, Exception {
		List<DBUser> list = null;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM user ";
					query += " WHERE  id = " + id;
			ResultSet rs = stmt.executeQuery(query);
			
			list = new LinkedList<DBUser>();
			while(rs.next()){
				DBUser user = new DBUser();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setLogin(rs.getString("login"));
				user.setEmail(rs.getString("email"));
				list.add(user);
			}
			
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return list.get(0);
	}

	public static boolean insertUser(String name, String document, String login, String password, String email, String phone) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "INSERT into user (name, document, login, password, email, phone) "
					+ " VALUES ('" + name + "'," + "'" + document+ "'," + "'" + login+ "'," + "'" + password+ "'," + "'" + email+ "','" + phone+ "')";
			
			int records = stmt.executeUpdate(query);
			
			if (records > 0) {
				insertStatus = true;
			}
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return insertStatus;
	}
	
	public static boolean insertBalance(String user, Integer amount) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "INSERT into balance (user, amount) "
					+ " VALUES ('" + user + "'," + amount+ ")";
			
			int records = stmt.executeUpdate(query);
			
			if (records > 0) {
				insertStatus = true;
			}
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return insertStatus;
	}

	
	//////////////////////////////
	// RESTAURANTE
	//////////////////////////////
	
	public static List<DBRestaurant> getAllRestaurant() throws SQLException, Exception {
		return getAllRestaurant(null);
	}
	
	public static DBRestaurant getRestaurant(int id) throws SQLException, Exception {
		List<DBRestaurant> list = getAllRestaurant(id);
		return list!=null && !list.isEmpty()?list.get(0):null;
	}
	
	private static List<DBRestaurant> getAllRestaurant(Integer id) throws SQLException, Exception {
		List<DBRestaurant> list = null;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM restaurant ";
					query += " WHERE 1 = 1";
					query += id!=null ? " AND id = "+id : "" ;
			ResultSet rs = stmt.executeQuery(query);
			
			list = new LinkedList<DBRestaurant>();
			while(rs.next()){
				DBRestaurant rest = new DBRestaurant();
				rest.setId(rs.getInt("id"));
				rest.setName(rs.getString("name"));
				rest.setDescription(rs.getString("description"));
				rest.setTableCount(rs.getInt("table_count"));
				rest.setLongitude(rs.getString("longitude"));
				rest.setLatitude(rs.getString("latitude"));
				rest.setLogoImage((byte[]) rs.getBytes("logo_image"));
				list.add(rest);
			}
			
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return list;
	}
	

	////////////////////
	// RESERVAS
	////////////////////
	
	public static DBReservation getReservation(int id) throws SQLException, Exception {
		List<DBReservation> list = getAllReservation(id, null);
		return list!=null && !list.isEmpty()?list.get(0):null;
	}
	
	public static List<DBReservation> getAllReservation(int user) throws SQLException, Exception {
		return getAllReservation(null, user);
	}
	
	private static List<DBReservation> getAllReservation(Integer id, Integer user) throws SQLException, Exception {
		List<DBReservation> list = null;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM reservation ";
					query += " WHERE 1=1";
					query += id!=null ? " AND id = "+id : "";
					query += user!=null ? " AND user = "+user : "";
			ResultSet rs = stmt.executeQuery(query);
			
			list = new LinkedList<DBReservation>();
			while(rs.next()){
				DBReservation res = new DBReservation();
				res.setId(rs.getInt("id"));
				res.setRestaurant(rs.getInt("restaurant"));
				res.setUser(rs.getInt("user"));
				res.setDate(rs.getDate("date"));
				res.setStatus(rs.getInt("status"));
				res.setCreationDate(rs.getDate("creation_date"));
				res.setPersonCount(rs.getInt("person_count"));
				list.add(res);
			}
			
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return list;
	}
	
	public static boolean insertReserva(int restaurant, int user, Date date) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "INSERT into reservation(restaurant, user, date, status, creation_date) "
					+ " values(" + restaurant + "," + user + "," + new java.sql.Date(date.getTime()) + ",0, "+new java.sql.Date((new Date()).getTime())+")";
			
			int records = stmt.executeUpdate(query);
			
			if (records > 0) {
				insertStatus = true;
			}
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			if (conn != null) {
				conn.close();
			}
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return insertStatus;
	}
	
}
