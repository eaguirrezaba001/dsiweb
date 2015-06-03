package eus.ehu.dsiweb.jersey;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
import eus.ehu.dsiweb.entity.IEntityConstants;

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
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@PUT 
	@Path("/doreservation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String insertReservation(final String input){
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//		String dateInString = "Wed May 27 15:07:06 CEST 2015";
		try {
			JSONObject obj = new JSONObject(input);
			
			Integer restaurant = obj.getInt(IEntityConstants.RESTAURANT); 
    		Integer user = obj.getInt(IEntityConstants.USER);
    		Date date = sdf.parse(obj.getString(IEntityConstants.DATE)); 
    		Integer count = obj.getInt(IEntityConstants.PERSON_COUNT);
    		if(!isReservationAvailable(restaurant, date, count)){
    			return Utility.constructJSON(false, "No hay mesas para "+count+" personas en la fecha y hora seleccionadas");
    		} else if(!userBalanceAvailable(user)){
    			return Utility.constructJSON(false, "No dispones de saldo para realizar el pago.");
			} else {
				Boolean successful = DBConnection.insertReserva(restaurant, user, date, count);
				if(successful!=null){
					if(successful){
						DBConnection.decreaseUserCredit(user, 10);
					}
					return Utility.constructJSON(successful);
				}
			}
		} catch(SQLException sqle){
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private boolean isReservationAvailable(Integer restaurant, Date date, Integer count) {
		try {
			Integer totalAvailable = DBConnection.obtainAvailableTables(restaurant, date, count);
			return totalAvailable>=count;
		} catch(SQLException sqle){
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean userBalanceAvailable(Integer user) {
		try {
			Integer amount = DBConnection.obtainAvailableUserBalance(user);
			return amount>=10;
		} catch(SQLException sqle){
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	@POST
	@Path("/cancelreservation")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String cancelReservation(final String input){
		try {
			JSONObject obj = new JSONObject(input);
	        Boolean successful = DBConnection.cancelReserva(obj.getInt(IEntityConstants.ID));
	        if(successful!=null){
	        	if(successful){
	        		DBConnection.increaseUserCredit(obj.getInt(IEntityConstants.USER), 10);
	        	}
				return Utility.constructJSON(successful);
			}
		} catch(SQLException sqle){
			sqle.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
}
