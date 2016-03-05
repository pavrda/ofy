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
	Long gameCredit;

	/**
	 * Pokud jsem prihlasen, muj aktualni kredit
	 */
	Long credit;

	/**
	 * Pokud jsem prihlasen, moje username
	 */
	String username;


	public Long getCredit() {
		return credit;
	}
	public void setCredit(Long credit) {
		this.credit = credit;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Long getGameCredit() {
		return gameCredit;
	}
	public void setGameCredit(Long gameCredit) {
		this.gameCredit = gameCredit;
	}

}
