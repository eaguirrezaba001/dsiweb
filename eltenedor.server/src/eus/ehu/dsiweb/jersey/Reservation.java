package eus.ehu.dsiweb.jersey;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import eus.ehu.dsiweb.DBConnection;
import eus.ehu.dsiweb.Utitlity;
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
				return Utitlity.getRestaurantListJSON(list);
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
				return Utitlity.constructJSON(res);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}

	@GET 
	@Path("/allreservation")
	@Produces(MediaType.APPLICATION_JSON) 
	public String getAllReservation(@QueryParam("user") int user){
		try {
			List<DBReservation> list = DBConnection.getAllReservation(user);
			if(list!=null){
				return Utitlity.getReservationListJSON(list);
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
				return Utitlity.constructJSON(res);
			}
		} catch(SQLException sqle){
			
		} catch (Exception e) {
			
		}
		return "";
	}
	
	
}
