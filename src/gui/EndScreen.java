package gui;
import game.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Class representing the end screen of the game
 * @author azhou849
 * @version 1.0
 */
public class EndScreen extends JPanel {
	private ArrayList<Player> players;
	private String winString;
	private JFrame window;
	
	/**
	 * Constructs an end screen using the given parameters
	 * @param players The players in the game
	 * @param winStatus Value representing who won (1 = mafia won, 2 = town won)
	 */
	public EndScreen(ArrayList<Player> players, int winStatus) {
		this.players = players;
		if(winStatus == 1) {
			winString = "MAFIA WINS";
		}
		else if (winStatus == 2){
			winString = "TOWN WINS";
		}
		else {
			winString = "STALEMATE";
		}
		setBackground(Color.WHITE);
		window = new JFrame("Mafia");
		window.setBounds(300, 300, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(this);
		window.setVisible(true);
	}
	
	/**
	 * Displays the end screen using the specified Graphics component
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		String fileSep = File.separator;
		ImageIcon detective = new ImageIcon("lib" + fileSep + "detective.png");
		ImageIcon mafia = new ImageIcon("lib" + fileSep + "mafia.png");
		ImageIcon stars = new ImageIcon("lib" + fileSep + "sky.png");
		
		g.drawImage(stars.getImage(), 0, 0, getWidth(), getHeight(), this);
		g.drawImage(detective.getImage(), 50, 300, detective.getIconWidth()/4, detective.getIconHeight()/4, this);
		g.drawImage(mafia.getImage(), getWidth()-50-mafia.getIconWidth()/4, 300, detective.getIconWidth()/4, detective.getIconHeight()/4, this);
		
		
		
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Times New Roman", Font.BOLD, 60));
		g.drawString(winString, 220, 100);
		g.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		
		g.drawString("GAME SUMMARY", 300, 200);
		
		int y = 245;
		int sep = 25;
		for(Player p : players) {
			String alive = "";
			if(p.getLivingStatus()) alive = "Alive";
			else alive = "Dead";
			g.drawString(p.toString() + ": " + p.getRole() + " (" + alive + ")", 300, y);
			y += sep;
		}
	}
	
}
