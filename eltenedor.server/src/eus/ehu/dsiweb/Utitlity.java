package eus.ehu.dsiweb;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.DBUser;

public class Utitlity {
	
	public static boolean isNotNull(String txt) {
		return txt != null && txt.trim().length() >= 0 ? true : false;
	}

	public static String constructJSON(String tag, boolean status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("tag", tag);
			obj.put("status", new Boolean(status));
		} catch (JSONException e) {
			 
		}
		return obj.toString();
	}

	public static String constructJSON(String tag, boolean status, String err_msg) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("tag", tag);
			obj.put("status", new Boolean(status));
			obj.put("error_msg", err_msg);
		} catch (JSONException e) {
			
		}
		return obj.toString();
	}
	
	public static String constructJSON(DBUser user) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("id",user.getId());
			obj.put("name", user.getName());
			obj.put("login", user.getLogin());
			obj.put("email", user.getEmail());
		} catch (JSONException e) {
			
		}
		return obj.toString();
	}
	
	private static JSONObject obtainJSON(DBRestaurant restaurant) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("id",restaurant.getId());
			obj.put("name", restaurant.getName());
			obj.put("description", restaurant.getDescription());
			obj.put("tableCount", restaurant.getTableCount());
			obj.put("longitude", restaurant.getLongitude());
			obj.put("latitude", restaurant.getLatitude());
			obj.put("logo", restaurant.getLogoImage());
		} catch (JSONException e) {
			
		}
		return obj;
	}
	
	public static String constructJSON(DBRestaurant res) {
		JSONObject obj = obtainJSON(res);
		return obj.toString();
	}
	
	public static String getRestaurantListJSON(List<DBRestaurant> list) {
		List<JSONObject> obj = new LinkedList<JSONObject>(); 
		for(DBRestaurant rest: list){
			obj.add(obtainJSON(rest));
		}
		return obj.toString();
	}
	

	private static JSONObject obtainJSON(DBReservation reservation) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("id",reservation.getId());
			obj.put("restaurant",reservation.getRestaurant());
			obj.put("user",reservation.getUser());
			obj.put("date",reservation.getDate());
			obj.put("status",reservation.getStatus());
			obj.put("creationDate",reservation.getCreationDate());
			obj.put("personCount",reservation.getPersonCount());
		} catch (JSONException e) {
			
		}
		return obj;
	}

	public static String constructJSON(DBReservation res) {
		JSONObject obj = obtainJSON(res);
		return obj.toString();
	}
	
	public static String getReservationListJSON(List<DBReservation> list) {
		List<JSONObject> obj = new LinkedList<JSONObject>(); 
		for(DBReservation reservation: list){
			obj.add(obtainJSON(reservation));
		}
		return obj.toString();
	}
	
}
