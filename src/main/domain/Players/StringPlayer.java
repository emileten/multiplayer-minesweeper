package main.domain.Players;

public class StringPlayer implements Player {

	private final String player_id;
	
	public StringPlayer(String id) {
		this.player_id = id;
	}
	
	public String getID() {
		String id = new String(this.player_id);
		return id;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.hashCode() == o.hashCode();
	}
	
	@Override
	public int hashCode() {
		return this.getID().hashCode();
	}

}
