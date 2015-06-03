package eus.ehu.dsiweb.entity;

import java.util.Date;


public class DBReservation {
	
	private Integer id;
	private Integer restaurant;
	private Integer user;
	private Date date;
	private Integer status;
	private Date creationDate;
	private Integer personCount;
	private DBRestaurant DBRestaurant;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(Integer restaurant) {
		this.restaurant = restaurant;
	}
	public Integer getUser() {
		return user;
	}
	public void setUser(Integer user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Integer getPersonCount() {
		return personCount;
	}
	public void setPersonCount(Integer personCount) {
		this.personCount = personCount;
	}
	public DBRestaurant getDBRestaurant() {
		return DBRestaurant;
	}
	public void setDBRestaurant(DBRestaurant dBRestaurant) {
		DBRestaurant = dBRestaurant;
	}
	
	
}
