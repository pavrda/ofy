package cz.inited.ofy.models;

public class APIGetUserInfoResponse extends APIGetInfoResponse {

	
	/**
	 * Ticket pro pristi prihlaseni
	 */
	String ticket;

	/**
	 * Detail o uzivateli
	 */
	User user;
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
