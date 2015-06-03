package eus.ehu.dsiweb.entity;


public class DBRestaurant {
	
	private Integer id;
	private String name;
	private String description;
	private Integer tableCount;
	private String longitude;
	private String latitude;
	private byte[] logoImage;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTableCount() {
		return tableCount;
	}
	public void setTableCount(Integer tableCount) {
		this.tableCount = tableCount;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public byte[] getLogoImage() {
		return logoImage;
	}
	public void setLogoImage(byte[] logoImage) {
		this.logoImage = logoImage;
	}
	
	
}
