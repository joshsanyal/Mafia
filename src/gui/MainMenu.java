package gui;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.swing.*;

import networking.frontend.NetworkManagementPanel;
/**
 * Class representing the main menu of the game
 * @author azhou849
 * @version 1.0
 *
 */
public class MainMenu extends JPanel implements MouseListener {
	
	private enum State {
		MENU, INSTRUCTIONS, ROLES;
	}
	private State state;
	private Font titleFont, infoFont, smallFont;
	private RectButton start, instructions, back1, roles, back2;
	private JFrame window;
	
	/**
	 * Constructs a main menu
	 */
	public MainMenu() {
		state = State.MENU;
		setBackground(Color.WHITE);
		
		window = new JFrame("Mafia");
		window.setBounds(300, 300, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(this);
		window.addMouseListener(this);
		window.setVisible(true);
		
		titleFont = new Font("Times New Roman", Font.BOLD, 100);
		infoFont = new Font("Times New Roman", Font.PLAIN, 20);
		smallFont = new Font("Times New Roman", Font.PLAIN, 14);
	}
	
	/**
	 * Displays the main menu using the specified Graphics component
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
		if(state.equals(State.MENU)) {
			start = new RectButton(g,300,300,150,60, "START");
			start.draw();
			instructions = new RectButton(g,300,380,150,60, "HELP");
			instructions.draw();
			g.setFont(titleFont);
			g.drawString("Mafia", getWidth()/2-140, 200);
		}
		else if(state.equals(State.INSTRUCTIONS)) {
			back1 = new RectButton(g,20,20,135,60,"BACK");
			back1.draw();
			roles = new RectButton(g,600,20,150,60,"ROLES");
			roles.draw();
			g.setFont(titleFont);
			g.drawString("Instructions", 130, 175);
			g.setFont(infoFont);
			g.drawString("In this game, a group of players can create", 210, 280);
			g.drawString("custom games of Mafia which cycle in three", 210, 280+23);
			g.drawString("phases (discussion, voting, night). During", 210, 280+23*2);
			g.drawString("these phases, players can chat using the", 210, 280+23*3);
			g.drawString("game chat, perform different actions based", 210, 280+23*4);
			g.drawString("on their role, and vote to lynch certain", 210, 280+23*5);
			g.drawString("players. Once all townspeople or mafia die,", 210, 280+23*6);
			g.drawString("the game ends.", 210, 280+23*7);
		}
		else if(state.equals(State.ROLES)) {
			back2 = new RectButton(g,20,20,135,60,"BACK");
			back2.draw();
			g.setFont(titleFont);
			g.drawString("Roles", 270, 175);
			
			g.setFont(infoFont);
			g.drawString("Townsperson: aligned with town. No special attributes.",80,210);
			g.drawString("Detective: aligned with town. Can investigate one person per night to learn their role.", 80, 210+23);
			g.drawString("Doctor: aligned with town. Can heal one person per night to save them from dying.", 80, 210+2*23);
			g.drawString("Mafioso: aligned with mafia. Can kill one person per night.", 80, 210+3*23);
			
			g.setFont(smallFont);
			g.drawString("*For role assignment, the host will only be prompted for the number of detectives, doctors, and mafiosos.", 80, 520);
			g.drawString("Any unassigned players will become townspeople by default.", 80, 520+12);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mx = e.getX()-8; //offset for JFrame
		int my = e.getY()-30; 
		
		if(state.equals(State.MENU)) {
			if(start.isOnButton(mx,my)) {
				MafiaClient panel = new MafiaClient();
				NetworkManagementPanel nmp = new NetworkManagementPanel("Mafia servers", 20, panel);
				window.dispose();
			}
			else if(instructions.isOnButton(mx, my)) {
				state = State.INSTRUCTIONS;
				repaint();
			}
		}
		else if(state.equals(State.INSTRUCTIONS)) {
			if(back1.isOnButton(mx, my)) {
				state = State.MENU;
				repaint();
			}
			else if(roles.isOnButton(mx, my)) {
				state = State.ROLES;
				repaint();
			}
		}
		else if(state.equals(State.ROLES)) {
			if(back2.isOnButton(mx, my)) {
				state = State.INSTRUCTIONS;
				repaint();
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
