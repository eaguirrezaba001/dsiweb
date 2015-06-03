package eus.ehu.dsiweb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
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
	
	public static Integer getCredit(String user) throws Exception {
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM balance ";
					query += " WHERE user = " + user;
			ResultSet rs = stmt.executeQuery(query);
			if (rs.next()) {
				Integer amount = rs.getInt("amount");
				return amount;
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
		return null;
	}
	
	public static DBUser getUserInfo(String username, String password) throws Exception {
		List<DBUser> list = null;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT u.id, u.name, u.login, u.email, b.amount FROM user as u left join balance as b on u.id = b.user";
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
				user.setCredit(rs.getInt("amount"));
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
					query += " ORDER BY date desc ";
			ResultSet rs = stmt.executeQuery(query);
			
			list = new LinkedList<DBReservation>();
			while(rs.next()){
				DBReservation res = new DBReservation();
				res.setId(rs.getInt("id"));
				res.setRestaurant(rs.getInt("restaurant"));
				res.setUser(rs.getInt("user"));
				res.setDate(rs.getTimestamp("date"));
				res.setStatus(rs.getInt("status"));
				res.setCreationDate(rs.getTimestamp("creation_date"));
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
	
	public static boolean insertReserva(int restaurant, int user, Date date, int personCount) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "INSERT into reservation(restaurant, user, date, status, creation_date, person_count) "
					+ " values(" + restaurant + "," + user + ",'" + new java.sql.Timestamp(date.getTime()) + "',0,'"+new java.sql.Timestamp((new Date()).getTime())+"', "+personCount+")";
			
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

	public static boolean removeReserva(int reservation) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "DELETE from reservation "
					+ " WHERE id = " + reservation;
			
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
	

	public static boolean cancelReserva(int reservation) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "UPDATE reservation "
					+ " SET status = 1"
					+ " WHERE id = " + reservation;
			
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

	public static boolean insertUserCredit(String login, Integer amount) throws SQLException, Exception {
		boolean insertStatus = false;
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO balance (user, amount)"
					+ " VALUES ((select id from user where login = '"+login+"'), "+amount+");";
			
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

	public static Integer obtainAvailableUserBalance(Integer user) throws SQLException, Exception {
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM balance";
					query += " WHERE user = "+user;
			ResultSet rs = stmt.executeQuery(query);
			
			Integer amount = 0;
			if(rs.next()){
				amount = rs.getInt("amount");
			}
			return amount;
			
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
	}

	public static Integer obtainAvailableTables(Integer restaurant, Date date, Integer count) throws SQLException, Exception {
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Calendar start = Calendar.getInstance();
			start.setTime(date);
			start.set(Calendar.SECOND, 0);
			Calendar end = Calendar.getInstance();
			end.setTime(date);
			end.set(Calendar.SECOND, 59);
			
			Statement stmt = conn.createStatement();
			String query = "SELECT sum(resv.person_count), (rest.table_count)*4 ";
				query += " FROM restaurant as rest left join reservation as resv ";
				query += " ON rest.id = resv.restaurant";
				query += " WHERE restaurant = "+restaurant;
				query += " AND date between '"+new java.sql.Timestamp(start.getTime().getTime())+"' AND '"+new java.sql.Timestamp(end.getTime().getTime())+"'";
				
			ResultSet rs = stmt.executeQuery(query);
			
			int reservedPerson = 0;
			int availablePerson = 0;
			if(rs.next()){
				reservedPerson = rs.getInt(1);
				availablePerson = rs.getInt(2);
			}
			return availablePerson-reservedPerson;
			
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
	}
	
	public static boolean decreaseUserCredit(Integer user, int amount) throws SQLException, Exception {
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "UPDATE balance "
					+ " SET amount = amount - " + amount
					+ " WHERE user = " + user;
			
			return stmt.executeUpdate(query)==1;
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
	}

	public static boolean increaseUserCredit(Integer user, int amount) throws SQLException, Exception {
		Connection conn = null;
		try {
			conn = DBConnection.createConnection();
			
			Statement stmt = conn.createStatement();
			String query = "UPDATE balance "
					+ " SET amount = amount + " + amount
					+ " WHERE user = " + user;
			
			return stmt.executeUpdate(query)==1;
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
	}


}
