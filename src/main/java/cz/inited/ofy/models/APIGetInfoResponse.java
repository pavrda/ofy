package cz.inited.ofy.models;

/**
 * Odpoved na getInfo
 * 
 * @author jara
 *
 */
public class APIGetInfoResponse extends APIResponseBase {

	/**
	 * Castka, o kterou se hraje v jackpotu
	 * Hodnota je EUR * 100
	 */
	Integer jackpot;

	/**
	 * Pokud jsem prihlasen, muj aktualni kredit
	 */
	Integer credit;

	/**
	 * Pokud jsem prihlasen, moje username
	 */
	String username;

	/**
	 * Ticket pro pristi prihlaseni
	 */
	String ticket;

	public Integer getCredit() {
		return credit;
	}
	public void setCredit(Integer credit) {
		this.credit = credit;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getJackpot() {
		return jackpot;
	}
	public void setJackpot(Integer jackpot) {
		this.jackpot = jackpot;
	}	
}
