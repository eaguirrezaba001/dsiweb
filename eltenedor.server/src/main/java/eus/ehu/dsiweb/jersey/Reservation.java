package eus.ehu.dsiweb.jersey;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;

import eus.ehu.dsiweb.DBConnection;
import eus.ehu.dsiweb.Utility;
import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;

@Path("/reservation")
public class Reservation {

	@GET 
	@Path("/allrestaurant")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getAllRestaurant(){
		try {
			List<DBRestaurant> list = DBConnection.getAllRestaurant();
			if(list!=null){
				return Utility.getRestaurantListJSON(list);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}
	
	@GET 
	@Path("/getrestaurant")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getRestaurant(@QueryParam("id") int id){
		try {
			DBRestaurant res = DBConnection.getRestaurant(id);
			if(res!=null){
				return Utility.constructJSON(res);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}

	@GET 
	@Path("/allreservation")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getAllReservation(@QueryParam("user") String user){
		try {
			List<DBReservation> list = DBConnection.getAllReservation(Integer.parseInt(user));
			if(list!=null){
				return Utility.getReservationListJSON(list);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}
	
	@GET 
	@Path("/getreservation")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getReservation(@QueryParam("id") int id){
		try {
			DBReservation res = DBConnection.getReservation(id);
			if(res!=null){
				return Utility.constructJSON(res);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}

	@PUT 
	@Path("/doreservation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	//public String insertReservation(@QueryParam("restaurant") int restaurant, @QueryParam("user") int user, @QueryParam("reservation_date") String date, @QueryParam("person_count") String personCount){
	public String insertReservation(final String input){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		SimpleDateFormat formatter = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
//		String dateInString = "Friday, Jun 7, 2013 12:10:56 PM";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		//SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
		String dateInString = "Wed May 27 15:07:06 CEST 2015";
		
		
		try {
//	        Boolean status = DBConnection.insertReserva(restaurant, user, sdf.parse(date), personCount);
			JSONObject obj = new JSONObject(input);
	        Boolean status = DBConnection.insertReserva(
	        		obj.getInt("restaurant"), 
	        		obj.getInt("user"), 
	        		sdf.parse(obj.getString("date")), 
	        		obj.getInt("personCount"));
			if(status!=null){
				return Utility.constructJSON(status);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}
	
	@DELETE
	@Path("/removereservation")
	@Produces(MediaType.APPLICATION_JSON)
	public String removeReservation(@QueryParam("id") int id){
		try {
			Boolean status = DBConnection.removeReserva(id);
			if(status!=null){
				return Utility.constructJSON(status);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}
	
	
}
