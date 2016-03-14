package cz.inited.ofy.models;

import java.util.List;

public class APIGameListResponse extends APIResponseBase {
	List<APIGameListItem> games;

	public List<APIGameListItem> getGames() {
		return games;
	}

	public void setGames(List<APIGameListItem> games) {
		this.games = games;
	}

}
