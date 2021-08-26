package gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

/**
 * Class representing a graphical rectangular button
 * @author azhou849
 * @version 1.0
 *
 */
public class RectButton {
	
	private Graphics g;

	private int x, y, width, height;
	private int stringX, stringY;
	private String text;
	private Font customFont;
	private Color customColor;
	
	/**
	 * Constructs a rectangular button using the given parameters
	 * @param g The Graphics component used to draw the button
	 * @param x The x-coordinate of the button
	 * @param y The y-coordinate of the button
	 * @param width The width of the button
	 * @param height The height of the button
	 * @param text The text displayed on the button
	 */
	public RectButton(Graphics g, int x, int y, int width, int height, String text) {
		this.g = g;
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		
		this.customFont = new Font("Times New Roman", Font.BOLD, 38);
		this.stringX = 10;
		this.stringY = height/2;
		this.customColor = Color.WHITE;
	}
	
	/**
	 * Checks to see if the specified location is on the button
	 * @param mousex The x-coordinate of the point
	 * @param mousey The y-coordinate of the point
	 * @return True if the location is on the button, false otherwise
	 */
	public boolean isOnButton(int mousex, int mousey) {
		return ((mousex >= this.x&&mousex <= this.x + width)&&(mousey >= this.y&&mousey <= this.y + height));
	}
	
	/**
	 * Draws the button
	 */
	public void draw() {
		g.setColor(customColor);
		g.drawRect(x, y, width, height);
		g.setFont(customFont);
		g.drawString(text, x+stringX, y+stringY+10);
		g.setColor(Color.WHITE);
	}
	
}
