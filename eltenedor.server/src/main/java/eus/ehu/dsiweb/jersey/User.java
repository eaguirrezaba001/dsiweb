package eus.ehu.dsiweb.jersey;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import eus.ehu.dsiweb.DBConnection;
import eus.ehu.dsiweb.Utility;
import eus.ehu.dsiweb.entity.DBUser;

//Path: http://localhost/<appln-folder-name>/user
@Path("/user")
public class User {

	// HTTP Get Method
	@GET 
	// Path: http://localhost/<appln-folder-name>/user/dologin
	@Path("/dologin")
	// Produces JSON as response
	@Produces(MediaType.APPLICATION_JSON) 
	// Query parameters are parameters: http://localhost/<appln-folder-name>/user/dologin?username=abc&password=xyz
	public String doLogin(@QueryParam("username") String username, @QueryParam("password") String password){
		String response = "";
		
		if(Utility.isNotNull(username) && Utility.isNotNull(password)){
			try {
				DBUser user = DBConnection.getUserInfo(username, password);
				response = Utility.constructJSON(user);
			} catch (Exception e) {
				response = "";
			}
		}
//		if(checkCredentials(username, password)){
//			response = Utitlity.constructJSON("login",true);
//			
//			
//		}else{
//			response = Utitlity.constructJSON("login", false, "Incorrect Email or Password");
//		}
		return response;
	}
	
	@GET 
	@Path("/doregister")  
	@Produces(MediaType.APPLICATION_JSON) 
	public String doRegistration(@QueryParam("name") String name,
			@QueryParam("document") String document,
			@QueryParam("login") String login,
			@QueryParam("password") String password,
			@QueryParam("email") String email, 
			@QueryParam("phone") String phone) {
		String response = "";
		RegistrationStatus status = registerUser(name, document, login, password, email, phone);
		if (status == RegistrationStatus.CREATED) {
			response = Utility.constructJSON("register", true);
		} else if (status == RegistrationStatus.REGISTERED) {
			response = Utility.constructJSON("register", false, "You are already registered");
		} else if (status == RegistrationStatus.WRONG_CHARACTER) {
			response = Utility.constructJSON("register", false, "Special Characters are not allowed in Username and Password");
		} else if (status == RegistrationStatus.ERROR) {
			response = Utility.constructJSON("register", false, "Error occured");
		}
		return response;
	}
	
	/**
	 * @param name
	 * @param document
	 * @param login
	 * @param password
	 * @param email
	 * @param phone
	 * @return
	 */
	private RegistrationStatus registerUser(String name, String document, String login, String password, String email, String phone) {
		RegistrationStatus result = null;
		if(Utility.isNotNull(login) && Utility.isNotNull(password)){
			try {
				if(DBConnection.insertUser(name, document, login, password, email, phone)){
					result = RegistrationStatus.CREATED;
				}
			} catch(SQLException sqle){
				//When Primary key violation occurs that means user is already registered
				if(sqle.getErrorCode() == 1062){
					result = RegistrationStatus.REGISTERED;
				} 
				//When special characters are used in name,username or password
				else if(sqle.getErrorCode() == 1064){
					System.out.println(sqle.getErrorCode());
					result = RegistrationStatus.WRONG_CHARACTER;
				}
			} catch (Exception e) {
				result = RegistrationStatus.ERROR;
			}
		} else {
			result = RegistrationStatus.ERROR;
		}
		return result;
	}
	
	/**
	 * @param uname
	 * @param pwd
	 * @return
	 */
	private boolean checkCredentials(String username, String password){
		boolean result = false;
		if(Utility.isNotNull(username) && Utility.isNotNull(password)){
			try {
				result = DBConnection.checkLogin(username, password);
			} catch (Exception e) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}
	
	
	public enum RegistrationStatus {
		CREATED,
		REGISTERED,
		WRONG_CHARACTER,
		ERROR;
	}
	
	public enum ReservationStatus {
		ACTIVE,
		CANCELED;
	}
	
}
