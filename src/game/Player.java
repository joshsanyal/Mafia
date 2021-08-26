package game;

import java.util.HashMap;
/**
 * Class representing the player's name, role, actions
 * @author jsanyal173
 * @version 2.0
 */
public class Player {

	public static HashMap<String,Player> players = new HashMap<String,Player>();
	private boolean saved;
	private Role role;
	private String name;
	private boolean alive;
	private int votes;
	
	/**
	 * Creates a Player object with the specified name
	 * @param n the name of the player 
	 */
	public Player(String n) {
		saved = false;
		if (n.indexOf(';') != -1 || n.indexOf('>') != -1) {
			System.out.println("name shouldn't have a ';' or '>'");
		}
		votes = 0;
		name = n;
		alive = true;
		players.put(name, this);
	}
	
	
	/**
	 * Assigns a role to the player
	 * @param r the role being assigned
	 */
	public void assignRole(Role r) {
		role = r;
	}
	
	/**
	 * Kills the player
	 */
	public void kill() {
		alive = false;
	}
	
	/**
	 * Returns the name of the player
	 */
	public String toString() {
		return name;
	}
	
	/**
	 * Returns the player's role
	 */
	public Role getRole() {
		return role;
	}
	
	 /**
	  * Increases the number of votes for this player
	  */
	protected void incVotes() {
		votes++;
	}
	
	/**
	 * Saves the player from dying during the night (used by doctor)
	 */
	protected void save() {
		saved = true;
	}
	
	/**
	 * Returns if the player has been saved
	 */
	public boolean getSave() {
		return saved;
	}
	
	/**
	 * Sets saved to the value of b
	 */
	public void setSave(boolean b) {
		saved = b;
	}
	
	/**
	 * Reset number of votes to 0
	 */
	protected void resetVotes() {
		votes = 0;
	}
	
	
	/**
	 * Returns the player's living status
	 */
	public boolean getLivingStatus() {
		return alive;
	}
	
	/**
	 * Returns the number of votes for this player
	 */
	public int getVotes() {
		return votes;
	}
	
	
}
