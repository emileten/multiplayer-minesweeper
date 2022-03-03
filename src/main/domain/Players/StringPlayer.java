package main.domain.Players;

public class StringPlayer implements Player {

	private final String player_id;
	
	public StringPlayer(String id) {
		this.player_id = id;
	}
	
	@Override
	public String getID() {
		String id = new String(this.player_id);
		return id;
	}
	
	public boolean equals(Player other) {
		return this.player_id.equals(other.getID());
	}

}
