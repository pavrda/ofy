package cz.inited.ofy.models;

import java.util.List;

public class APIBetListResponse extends APIResponseBase {
	List<APIBetListItem> bets;

	public List<APIBetListItem> getBets() {
		return bets;
	}

	public void setBets(List<APIBetListItem> bets) {
		this.bets = bets;
	}

	
}
