package gui;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Queue;

import javax.swing.*;

import networking.frontend.NetworkDataObject;
import networking.frontend.NetworkListener;
import networking.frontend.NetworkManagementPanel;
import networking.frontend.NetworkMessenger;

import game.*;

/**
 * A game client used by players to play Mafia.
 * @author azhou849
 * @version 4.0
 */
public class MafiaClient extends JPanel implements ActionListener, NetworkListener
{
	
	public boolean isHost = false;
	
	private String username;
	
	
	private JFrame window;
	private JEditorPane inText;
	private JTextField outText;
	private JButton sendButton, startButton, roleButton;
	private JPanel bottomPanel = new JPanel(), topPanel = new JPanel(), sidePanel = new JPanel();


	private boolean started = false, nightPhase = false;
	private JLabel gameTime = new JLabel();
	private JLabel playerList = new JLabel();

	private Game game;
	private Player player;

	private NetworkMessenger nm;

	/**
	 * Create a new instance of the client used to play Mafia
	 */
	public MafiaClient () {
		
		inText = new JEditorPane();
		inText.setContentType("text/html");
		inText.setEditable(false);
		
		outText = new JTextField();
		sendButton = new JButton("Send");
		startButton = new JButton("Start game");
		roleButton = new JButton("Show role");
		sendButton.addActionListener(this);
		startButton.addActionListener(this);
		roleButton.addActionListener(this);
		setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane(inText);
		add(scroll,BorderLayout.CENTER);

		bottomPanel.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		sidePanel.setLayout(new BorderLayout());

		topPanel.add(gameTime, BorderLayout.CENTER);
		sidePanel.add(playerList,BorderLayout.NORTH);
		bottomPanel.add(outText, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.EAST);
		bottomPanel.add(startButton,BorderLayout.WEST);


		add(bottomPanel,BorderLayout.SOUTH);
		add(topPanel,BorderLayout.NORTH);
		add(sidePanel,BorderLayout.EAST);
		outText.addActionListener(this);

		window = new JFrame("Mafia Game");
		window.setBounds(300, 300, 800, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.add(this);
		window.setVisible(true);
	}
	
	private void setNight() {
		inText.setBackground(new Color(0,0,200));
	}
	
	private void setVote() {
		inText.setBackground(new Color(150,0,0));
	}

	private void setDiscussion() {
		inText.setBackground(Color.WHITE);
	}

	private class VoteThread extends Thread {
		public boolean finishedVote = false;
		public void run() {
			while(game.checkWin()==0) {
				if(!finishedVote) {
					if (game.getGamePhase().equals("Vote")) { // DEAD PLAYER CHECK
						setVote();
						updatePlayerList();
						if(player.getLivingStatus()) {
							startVote();
							finishedVote = true;
						}
					}
					else if (game.getGamePhase().equals("Night")) {
						endVote();
						setNight();
						updatePlayerList();
						nightPhase = true;
						if(player.getLivingStatus() && game.checkWin()==0) {
							if (player.getRole() instanceof Mafioso) {
								mafiaVote();
								finishedVote = true;
							}
							else if (player.getRole() instanceof Doctor) {
								doctorVote();
								finishedVote = true;
							}
							else if (player.getRole() instanceof Detective) {
								detectiveVote();
								finishedVote = true;
							}
							finishedVote = true;
						}
						
					}
				}
			}
		}
	}

	private class GameThread extends Thread {
		private MafiaClient client;
		private VoteThread vt;
		public GameThread(MafiaClient c, VoteThread v) {
			client = c;
			vt = v;
		}

		public void run() {
			while(game.checkWin()==0) {
				int time = game.getCurrentTime();
				String phase = game.getGamePhase();

				gameTime.setText(phase + ": " + time);
				game.decrementTime();

				if(time==0) {
					game.changePhase();
					vt.finishedVote = false;
					if(game.getGamePhase().equals("Discuss")) {
						setDiscussion();
						endNight();
						nightPhase = false;
						updatePlayerList();
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(game.checkWin()==1) {
				JOptionPane.showMessageDialog(client, "Mafia wins");
			}
			else if (game.checkWin()==2) {
				JOptionPane.showMessageDialog(client, "Town wins");
			}
			else {
				JOptionPane.showMessageDialog(client, "Tie");
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			window.dispose();
			EndScreen end = new EndScreen(game.getPlayers(),game.checkWin());
		}

	}
	
	private String getCurChat() {
		String ret = inText.getText();
		ret = ret.substring(ret.indexOf("<body>")+11,ret.indexOf("</body>")-3);
		return ret;
	}

	private void endNight() {
		Player killed = game.resetVotes();
		if (killed == null || killed.getSave()) {
			inText.setText(getCurChat()+"<br><b>No one has died</b>");
		}
		else {
			inText.setText(getCurChat()+"<br><b>" + killed + " has died</b>");
			killed.kill();
		}
		game.resetSaves();
		updatePlayerList();
	}

	private void doctorVote() {
		String[] options = new String[game.getPlayers().size()];
		int counter = 0;
		for (Player p : game.getPlayers()) {
			if (!p.toString().equals(username) && p.getLivingStatus()) {
				options[counter] = p.toString(); 
				counter++;
			}
		}

		Object selected = JOptionPane.showInputDialog(null, "Who would you like to save?", "Selection", JOptionPane.DEFAULT_OPTION, null, options, "0");
		if (selected != null ){ //null if the user cancels. 
			game.saveVote(selected.toString());
			nm.sendMessage(NetworkDataObject.SAVE, selected.toString());
		}
	}

	private void detectiveVote() {
		String[] options = new String[game.getPlayers().size()-1];
		int counter = 0;
		for (Player p : game.getPlayers()) {
			if (!p.toString().equals(username) && p.getLivingStatus()) {
				options[counter] = p.toString();
				counter++;
			}
		}
		Object selected = JOptionPane.showInputDialog(null, "Who would you like to investigate?", "Selection", JOptionPane.DEFAULT_OPTION, null, options, "0");
		if (selected != null ){ //null if the user cancels. 
			Player sel = null;
			for (Player p : game.getPlayers()) {
				if (p.toString().equals(selected.toString())) {
					sel = p;
				}
			}
			inText.setText(getCurChat()+"<br><b>" + selected.toString() + "</b> is a <b>" + sel.getRole() + "</b>");
		} 
	}

	private void mafiaVote() {
		String[] options = new String[game.getPlayers().size()];
		int counter = 0;
		for (Player p : game.getPlayers()) {
			if(!(p.getRole() instanceof Mafioso) && p.getLivingStatus()) {
				options[counter] = p.toString();
				counter++;
			}
		}

		Object selected = JOptionPane.showInputDialog(null, "Who would you like to kill?", "Selection", JOptionPane.DEFAULT_OPTION, null, options, "0");
		if (selected != null ){ //null if the user cancels. 
			game.assignVote(selected.toString());
			nm.sendMessage(NetworkDataObject.KILL, selected.toString(), username);
			inText.setText(getCurChat()+"<br><b>You</b>: "+"I have voted to kill <b>" + selected.toString() + "</b>");
		}
	}

	private void startVote() {
		String[] options = new String[game.getPlayers().size()-1];
		int counter = 0;
		for (Player p : game.getPlayers()) {
			if (!p.toString().equals(username) && p.getLivingStatus()) {
				options[counter] = p.toString();
				counter++;
			}
		}

		Object selected = JOptionPane.showInputDialog(null, "Who would you like to lynch?", "Selection", JOptionPane.DEFAULT_OPTION, null, options, "0");
		if (selected != null ){ //null if the user cancels. 
			game.assignVote(selected.toString());
			nm.sendMessage(NetworkDataObject.VOTE,new String[] {username, selected.toString()});
			inText.setText(getCurChat()+"<br><b>You</b>: "+"I have voted for <b>" + selected.toString() + "</b>");
		}else{
			game.assignCancelledVote();
			nm.sendMessage(NetworkDataObject.VOTE,new String[] {username, null});
			inText.setText(getCurChat()+"<br><b>You</b>: "+"<b>I chose not to vote</b>");
		}
	}

	private void endVote() {
		Player p = game.resetVotes();
		if (p != null) {
			p.kill(); // if not tied
			inText.setText( getCurChat()+"<br><b>" + p + "</b> was lynched.");
		}
		else {
			inText.setText(getCurChat()+"<br><b>No one was lynched.</b>");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (player.getLivingStatus() && (source == outText || source == sendButton) && !nightPhase) {
			if (!outText.getText().trim().equals("")) {
				String[] toGo = new String[] {username,outText.getText().trim()};
				if (nm != null)
					nm.sendMessage(NetworkDataObject.MESSAGE,toGo);
				inText.setText(getCurChat()+"<br><b>You:</b> "+toGo[1]);
				outText.setText("");
				
			}
		}

		if(source == startButton) {
			if (!(isHost && (game == null || game.getPlayers().size() != Player.players.size()))) {
				JOptionPane.showMessageDialog(this, "Unable to start game");
			}
			else {
				int docNum = 0;
				int detNum = 0;
				int mafNum = 0;
				while(true) {
					boolean running = false;
					try {
						docNum = Integer.parseInt(JOptionPane.showInputDialog("Number of doctors"));
						detNum = Integer.parseInt(JOptionPane.showInputDialog("Number of detectives"));
						mafNum = Integer.parseInt(JOptionPane.showInputDialog("Number of mafia"));
					}
					catch(NumberFormatException e1) {
						running = true;
						JOptionPane.showMessageDialog(this, "Please enter a number");
					}
					if(!running) break;
				}


				int n = JOptionPane.showConfirmDialog(null, "Start game with " + Player.players.size() + " players?", "Start game", JOptionPane.YES_NO_OPTION);
				if(n==JOptionPane.YES_OPTION) {

					game = new Game(new ArrayList<Player>(Player.players.values()));


					game.setNumberOfRole(new int[]{detNum,docNum,mafNum});
					game.setRoles();

					nm.sendMessage(NetworkDataObject.START_GAME, game.getPlayersInts());
					startGame();
				}
			}
		}
		if (source == roleButton) {
			try {
				if (started) {
					ArrayList<Player> playing = game.getPlayers();
					for (Player p : playing) {
						if (username.equals(p.toString())) {
							JOptionPane.showMessageDialog(this, p.getRole());
							break;
						}
					}  
				}
				else {
					JOptionPane.showMessageDialog(this, "Game not started yet");
				}
			} catch (HeadlessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void networkMessageReceived(NetworkDataObject ndo) {
		String n = "";

		if (ndo.messageType.equals(NetworkDataObject.MESSAGE)) {
			n = (String) ndo.message[0];
			inText.setText(getCurChat()+"<br><b>" + n + "</b>: " + ndo.message[1]);
		}
		else if (ndo.messageType.equals(NetworkDataObject.VOTE)) {
			n = (String) ndo.message[0];
			if (ndo.message[1] == null) {
				game.assignCancelledVote();
				inText.setText(getCurChat()+"<br><b>" + n + "</b>: <b>I chose not to vote</b>");
			}
			else {
				game.assignVote((String) ndo.message[1]);	
				inText.setText(getCurChat()+"<br><b>" + n + "</b>: I have voted for <b>" + ndo.message[1] + "</b>");
			}
		}
		else if (ndo.messageType.equals(NetworkDataObject.KILL)) {
			game.assignVote((String) ndo.message[0]);	
			if (player.getRole() instanceof Mafioso) {
				inText.setText(getCurChat()+"<br><b>" + ndo.message[1] + "</b>: I have voted to kill <b>" + ndo.message[0] + "</b>");
			}
		}
		else if (ndo.messageType.equals(NetworkDataObject.SAVE)) {
			game.saveVote((String) ndo.message[0]);	
		}
		else if(ndo.messageType.equals(NetworkDataObject.CREATE_PLAYER)) {
			Player p = new Player((String)ndo.message[0]);
		}

		else if (ndo.messageType.equals(NetworkDataObject.DISCONNECT)) {
			if (ndo.dataSource.equals(ndo.serverHost)) 
				inText.setText(getCurChat()+"<br>Disconnected from server " + ndo.serverHost);
			else
				inText.setText(getCurChat()+"<br>" + n + " disconnected. ");
		}
		else if(ndo.messageType.equals(NetworkDataObject.START_GAME)) {
			String players = "" + ndo.message[0];
			ArrayList<Player> p = new ArrayList<Player>();
			int startIndex = 0;
			for (int i = players.indexOf(';'); i != -1; i = players.indexOf(';',i+1)) { // parse each person
				String playerSummary = players.substring(startIndex, i); // "name role"
				Player player = new Player(playerSummary.substring(0, playerSummary.indexOf('>'))); // create player w/ role	

				// assign correct role
				char role = playerSummary.charAt(playerSummary.length()-1);
				if (role == '0') 
					player.assignRole(new Doctor());
				else if (role == '1') 
					player.assignRole(new Detective());
				else if (role == '2') 
					player.assignRole(new Townsperson());
				else 
					player.assignRole(new Mafioso());

				// add player to players list
				p.add(player);
				startIndex = i + 1;
			}
			game = new Game(p);
			startGame();

		}

		Queue<NetworkDataObject> queue = nm.getQueuedMessages();
		queue.remove(ndo);
	}

	private void startGame() {
		VoteThread vt = new VoteThread();
		vt.start();
		GameThread gt = new GameThread(this,vt);
		gt.start();
		playerList.setText(playerList.getText()+"</html>");
		started = true;
		int[] numPlayerRole = game.getNumberRoles();
		JOptionPane.showMessageDialog(this, "New Game with " + Player.players.size() + " players\n" + numPlayerRole[0] + " doctor(s)\n" + numPlayerRole[1] + " detective(s)\n" + numPlayerRole[2] + " mafioso(s)");
		topPanel.add(roleButton,BorderLayout.WEST);
		updatePlayerList();
		for (Player p : game.getPlayers()) {
			if (p.toString().equals(username)) {
				player = p;
			}
		}
		NetworkManagementPanel.window.dispose();
	}

	private void updatePlayerList() {
		playerList.setText("<html><center> &nbsp PLAYERS LIST &nbsp </center><br><br>");
		for (Player p : game.getPlayers()) {
			String alive = "Dead";
			if (p.getLivingStatus()) {
				alive = "Alive";
			}
			playerList.setText(playerList.getText() + "<center> &nbsp " + p + ": " + alive + " &nbsp </center><br>");
		}
	}

	@Override
	public void connectedToServer(NetworkMessenger nm) {
		this.nm = nm;
		while(true) {
			username = JOptionPane.showInputDialog("Please enter your name");
			if(Player.players.get(username)!=null) JOptionPane.showMessageDialog(this, "Username taken");
			else break;
		}
		player = new Player(username);

		nm.sendMessage(NetworkDataObject.CREATE_PLAYER, username);
	}
}
