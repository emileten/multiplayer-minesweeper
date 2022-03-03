package main.domain.Players;

/** Represents the player of a MineSweeper game */
public interface Player {
	
	/** @return the ID of this player */ 
	public Object getID();
	
	/**
	 * @param other an object of the same type
	 * @return True if this is equal to other*/
	public boolean equals(Player other);
	
}
