package eus.ehu.dsiweb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import eus.ehu.dsiweb.entity.DBReservation;
import eus.ehu.dsiweb.entity.DBRestaurant;
import eus.ehu.dsiweb.entity.DBUser;
import eus.ehu.dsiweb.entity.IEntityConstants;

public class Utility {
	
	public static boolean isNotNull(String txt) {
		return txt != null && txt.trim().length() >= 0 ? true : false;
	}

	public static String constructJSON(String tag, boolean status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("tag", tag);
			obj.put("status", new Boolean(status));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	public static String constructJSON(boolean status, String err_msg) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(IEntityConstants.STATUS, new Boolean(status));
			obj.put(IEntityConstants.ERROR_MSG, err_msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	public static String constructJSON(Integer amount) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(IEntityConstants.AMOUNT,amount);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	

	public static String constructJSON(DBUser user) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(IEntityConstants.ID,user.getId());
			obj.put(IEntityConstants.NAME, user.getName());
			obj.put(IEntityConstants.LOGIN, user.getLogin());
			obj.put(IEntityConstants.EMAIL, user.getEmail());
			obj.put(IEntityConstants.AMOUNT, user.getCredit());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	private static JSONObject obtainJSON(DBRestaurant restaurant) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(IEntityConstants.ID,restaurant.getId());
			obj.put(IEntityConstants.NAME, restaurant.getName());
			obj.put(IEntityConstants.DESCRIPTION, restaurant.getDescription());
			obj.put(IEntityConstants.TABLE_COUNT, restaurant.getTableCount());
			obj.put(IEntityConstants.LONGITUDE, restaurant.getLongitude());
			obj.put(IEntityConstants.LATITUDE, restaurant.getLatitude());
			String encodedString = org.apache.commons.codec.binary.Base64.encodeBase64String(restaurant.getLogoImage());
			obj.put(IEntityConstants.LOGO_IMAGE, encodedString!=null?encodedString:"");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static String constructJSON(DBRestaurant res) {
		JSONObject obj = obtainJSON(res);
		return obj.toString();
	}
	
	public static String getRestaurantListJSON(List<DBRestaurant> list) {
		JSONObject obj = new JSONObject();
		List<JSONObject> objList = new LinkedList<JSONObject>();
		try {
			for(DBRestaurant rest: list){
				objList.add(obtainJSON(rest));
			}
			obj.put(IEntityConstants.RESTAURANT_LIST, objList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	

	public static JSONObject obtainJSON(DBReservation reservation) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(IEntityConstants.ID,reservation.getId());
			obj.put(IEntityConstants.RESTAURANT,reservation.getRestaurant());
			obj.put(IEntityConstants.USER,reservation.getUser());
			obj.put(IEntityConstants.DATE,reservation.getDate());
			obj.put(IEntityConstants.STATUS,reservation.getStatus());
			obj.put(IEntityConstants.CREATION_DATE,reservation.getCreationDate());
			obj.put(IEntityConstants.PERSON_COUNT,reservation.getPersonCount());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static String constructJSON(DBReservation res) {
		JSONObject obj = obtainJSON(res);
		return obj.toString();
	}
	
	public static String getReservationListJSON(List<DBReservation> list) {
		JSONObject obj = new JSONObject();
		List<JSONObject> objList = new LinkedList<JSONObject>();
		try {
			for(DBReservation rest: list){
				objList.add(obtainJSON(rest));
			}
			obj.put(IEntityConstants.RESERVATION_LIST, objList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}

	public static DBReservation obtainReservation(JSONObject obj)
			throws JSONException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DBReservation reservation = new DBReservation();
		reservation.setId(obj.getInt(IEntityConstants.ID));
		try {
			reservation.setDate(sdf.parse(obj.getString(IEntityConstants.DATE)));
			reservation.setCreationDate(sdf.parse(obj.getString(IEntityConstants.CREATION_DATE)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		reservation.setUser(obj.getInt(IEntityConstants.USER));
		reservation.setRestaurant(obj.getInt(IEntityConstants.RESTAURANT));
		reservation.setPersonCount(obj.getInt(IEntityConstants.PERSON_COUNT));
		reservation.setStatus(obj.getInt(IEntityConstants.STATUS));
		return reservation;
	}

	public static String constructJSON(Boolean status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put(IEntityConstants.STATUS, status);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	
	

    public static DBUser obtainUser(JSONObject obj) throws JSONException {
    	DBUser user = new DBUser();
        user.setId(obj.getInt(IEntityConstants.ID));
        user.setName(obj.getString(IEntityConstants.NAME));
        user.setLogin(obj.getString(IEntityConstants.LOGIN));
        user.setEmail(obj.getString(IEntityConstants.EMAIL));
        user.setCredit(obj.getInt(IEntityConstants.AMOUNT));
        return user;
    }

	public static DBRestaurant obtainRestaurant(JSONObject obj) throws JSONException {
		DBRestaurant restaurant = new DBRestaurant();
		restaurant.setId(obj.getInt(IEntityConstants.ID));
		restaurant.setName(obj.getString(IEntityConstants.NAME));
		restaurant.setDescription(obj.getString(IEntityConstants.DESCRIPTION));
		restaurant.setTableCount(obj.getInt(IEntityConstants.TABLE_COUNT));
		restaurant.setLongitude(obj.getString(IEntityConstants.LONGITUDE));
		restaurant.setLatitude(obj.getString(IEntityConstants.LATITUDE));
		String logo = obj.getString(IEntityConstants.LOGO_IMAGE);
		restaurant.setLogoImage(logo != null ? Base64.decodeBase64(logo) : "".toString().getBytes());

		return restaurant;
	}

	public static boolean obtainStatus(JSONObject obj) throws JSONException {
        return obj.getBoolean(IEntityConstants.STATUS);
    }
	
	public static String obtainError(JSONObject obj) throws JSONException {
        return obj.getString(IEntityConstants.ERROR_MSG);
    }

    	
	
}
