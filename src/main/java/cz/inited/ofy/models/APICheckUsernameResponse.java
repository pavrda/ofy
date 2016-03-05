package cz.inited.ofy.models;

public class APICheckUsernameResponse extends APIResponseBase {
	
	String username;
	String exists;

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getExists() {
		return exists;
	}
	public void setExists(String exists) {
		this.exists = exists;
	}
}
