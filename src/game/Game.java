package game;

import java.util.ArrayList;
/**
 * Class representing the Game
 * @author jsanyal173
 * @version 3.0
 */
public class Game {
	
	private ArrayList<Player> player;
	
	private String gamePhase;
	private int currentTime;
	private ArrayList<Integer> roles;
	private int numVotes;

	public static final int PHASE_TIME = 30;
	
	private int phaseIndex;
	private static final String[] GAME_PHASES = new String[] {"Discuss", "Vote", "Night"};
	
	/**
	 * Initializes a game with a set of players
	 * @param p set of players in the game
	 */
	public Game(ArrayList<Player> p) {
		phaseIndex=0;
		currentTime = PHASE_TIME;
		gamePhase = GAME_PHASES[phaseIndex];
		player = p;
		roles = new ArrayList<Integer>();
		numVotes = 0;
	}
	
	/**
	 * Returns the number of doctors, detective, mafia
	 */
	public int[] getNumberRoles() {
		int doc = 0, det = 0, maf = 0;
		for (Player p : player) {
			if (p.getRole() instanceof Doctor) {
				doc++;
			}
			else if (p.getRole() instanceof Detective) {
				det++;
			}
			else if (p.getRole() instanceof Mafioso) {
				maf++;
			}
		}
		return new int[] {doc, det, maf};
	}
	
	/**
	 * Checks if the game is over
	 * @return 0 if no win, 1 if mafia wins, 2 if townsfolk win
	 */
	public int checkWin() {
		boolean mafiaAlive = false;
		boolean townAlive = false;
		for(Player p : player) {
			if(p.getLivingStatus()) {
				if(p.getRole() instanceof Mafioso) mafiaAlive = true;
				else townAlive = true;
			}	
		}
		if(mafiaAlive && !townAlive) return 1;
		else if(townAlive && !mafiaAlive) return 2;
		return 0;
	}
	
	/**
	 * Create a random list of roles for the players
	 */
	public void setRoles() {
		int numPlayersLeft = player.size();
		for (Player p : player) {
			int role = (int) (Math.random()*numPlayersLeft);
			int a = roles.remove(role);
			if (a == 0) {
				p.assignRole(new Townsperson());
			}
			else if (a == 3) {
				p.assignRole(new Mafioso());
			}
			else if (a == 2) {
				p.assignRole(new Doctor());
			}
			else {
				p.assignRole(new Detective());
			}
			numPlayersLeft--;
		}
	}
	
	/**
	 * Set the number of each role in the game
	 * @param the number of detectives (first), doctors (second), mafia (third)
	 */
	public void setNumberOfRole(int[] r) {
		int index = 0;
		for (; index < r[0]; index++) { // detective = 1;
			roles.add(1);
		}
		for (; index < r[0] + r[1]; index++) { // doctors = 2;
			roles.add(2);
		}
		for (; index < r[0] + r[1] + r[2]; index++) { // mafia = 3;
			roles.add(3);
		}
		for (; index < player.size(); index++) { // townsperson = 0;
			roles.add(0);
		}
		
	}
	
	/**
	 * Returns an ArrayList of the players currently in the game
	 */
	public ArrayList<Player> getPlayers(){
		return player;
	}
	
	/**
	 * Returns a String containing key information about the current players
	 * @return a String with the names of the players and their roles (name and role separated by '>', different players separated by ';')
	 */
	public String getPlayersInts() {
		String ret = "";
		for (Player p : player) {
			if (p.getRole() instanceof Doctor) 
				ret += p + ">0;";
			else if (p.getRole() instanceof Detective) 
				ret += p + ">1;";
			else if (p.getRole() instanceof Townsperson) 
				ret += p + ">2;";
			else
				ret += p + ">3;";
				
		}
		return ret;
	}
	
	/**
	 * Changes the current game phase to next game phase and resets current time
	 */
	public void changePhase() {
		if(phaseIndex==GAME_PHASES.length-1) phaseIndex=0;
		else phaseIndex++;
		gamePhase = GAME_PHASES[phaseIndex];
		currentTime = PHASE_TIME;
	}
	
	/**
	 * @return the gamePhase
	 */
	public String getGamePhase() {
		return gamePhase;
	}

	/**
	 * @return the currentTime
	 */
	public int getCurrentTime() {
		return currentTime;
	}

	/**
	 * Decreases remaining game phase time by one
	 */
	public void decrementTime() {
		currentTime--;
	}
	
	/**
	 * Assigns vote to the specified player
	 * @param name The username of the player being voted for
	 */
	public void assignVote(String name) {
		numVotes++;
		Player.players.get(name).incVotes();
	}
	
	/**
	 * Assigns a cancelled vote the game
	 */
	public void assignCancelledVote() {
		numVotes++;
	}
	
	/**
	 * Resets the vote after voting phase is over
	 * @return Player with the most votes
	 */
	public Player resetVotes() {
		int maxVotes = 0;
		Player ret = null;
		for (Player p : player) {
			if (p.getVotes() > maxVotes) {
				maxVotes = p.getVotes();
				ret = p;
			}
			else if (p.getVotes() == maxVotes) {
				ret = null;
			}
			p.resetVotes();
		}
		numVotes = 0;
		return ret;
	}
	
	/**
	 * checks if every player has voted
	 * @return true if all players have voted
	 */
	public boolean voteDone() {
		return numVotes == player.size();
	}

	/**
	 * Saves the specified player
	 * @param name The username of the player
	 */
	public void saveVote(String name) {
		Player.players.get(name).save();
	}
	
	/**
	 * Resets any saves made by the doctors
	 */
	public void resetSaves() {
		for(Player p : player) {
			p.setSave(false);
		}
	}
	
	/**
	 * Checks if the game has 1 doctor, 1 mafioso
	 */
	private boolean checkTie() {
		int numAlive = 0;
		boolean doctor = false, mafia = false;
		for (Player p : player) {
			if (p.getLivingStatus()) {
				numAlive++;
				if (p.getRole() instanceof Doctor) {
					doctor = true;
				}
				else if (p.getRole() instanceof Mafioso) {
					mafia = true;
				}
			}
		}
		return (numAlive == 2 && doctor && mafia);
	}
	
	
}
