package menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

import handlers.InputHandler;
import main.State;

/**
 * the menu for entering a battle with a planet
 * 
 * 
 *
 */
public class BattleStart extends Menu {

	private static final long serialVersionUID = 1L;
	private Button attack, retreat, showUnits, bribe, trade, purchase;
	private Label planetName, unitCount, tradeMessage, bribeMessage;
	private boolean showing = false, bribed = false;
	private String pName;

	/**
	 * creates a new battle menu
	 * 
	 * @param bg
	 *            the background
	 * @param pName
	 *            the planet name
	 */
	public BattleStart(String bg, String pName, boolean finished) {
		super(bg);
		this.pName = pName;
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 4, height = InputHandler.screenSize.height / 12;
		int x = (int) InputHandler.midPoint.x - width / 2, y = (int) (InputHandler.midPoint.y / 4 - height / 2);
		planetName = new Label(pName, "Verdana", Font.BOLD, InputHandler.screenSize.width / 40, Color.white, x, y,
				width, height, JLabel.CENTER);
		y = y + (int) (height * 1.5);
		attack = new Button("Attack", x, y, width, height);
		y = y + (int) (height * 1.5);
		retreat = new Button("Retreat", x, y, width, height);
		y = y + (int) (height * 1.5);
		showUnits = new Button("Show Units? (crystal - 1)", x, y, width, height);
		x = x + width;
		width = width * 2;
		y = y + height / 9;
		unitCount = new Label("Unit Count : ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		width = width / 2;
		x = x - width;
		y = y + (int) (height * 1.5);
		bribe = new Button("Bribe Units? (money - 500)", x, y, width, height);
		x = x + width;
		width = width * 2;
		bribeMessage = new Label("" + pName, "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		width = width / 2;
		x = x - width;
		y = y + (int) (height * 1.5);
		trade = new Button("Trade With Planet?(money - 1000)", x, y, width, height);
		x = x + width;
		width = width * 2;
		tradeMessage = new Label("Trade Route Started With The Planet " + pName, "Verdana", Font.BOLD, 20, Color.white,
				x, y, width, height, JLabel.LEFT);
		width = width / 2;
		x = x - width;
		y = y + (int) (height * 1.5);
		purchase = new Button("Purchase Planet? (money - 50000", x, y, width, height);
		this.add(planetName);
		this.add(attack);
		this.add(retreat);
		this.add(showUnits);
		this.add(bribe);
		this.add(trade);
		if (finished) {
			this.add(purchase);
		}
	}

	/**
	 * shows the unit count at this planet
	 * 
	 * @param count
	 */
	public void showUnitCount(int count) {
		if (!showing) {
			unitCount.setText(unitCount.getText() + count);
			this.add(unitCount);
			setShowing(true);
			repaint();
		}
	}

	/**
	 * shows the no crystal message when trying to show units
	 */
	public void noCrystal() {
		if (!showing) {
			unitCount.setText("Not Enough Crystal");
			this.add(unitCount);
			setShowing(true);
			repaint();
		}
	}

	/**
	 * checks if the unit count is showing
	 * 
	 * @return whether the unit count is showing
	 */
	public boolean getShowing() {
		return showing;
	}

	/**
	 * sets if the unit count is showing
	 * 
	 * @param showing
	 *            the new value of showing
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}

	/**
	 * shows a message when the trade button is clicked
	 * 
	 * @param show
	 *            an int deciding which message to show
	 */
	public void showTrading(int show) {
		if (show == 0) {
			tradeMessage.setText("Trade Route Started With Planet " + pName);
			this.add(tradeMessage);
			this.remove(trade);
			this.repaint();
		} else if (show == 1) {
			tradeMessage.setText("Not Enough Money");
			this.add(tradeMessage);
			this.repaint();
		} else {
			tradeMessage.setText("Trade Not Possible With " + pName);
			this.add(tradeMessage);
			this.repaint();
		}
	}

	/**
	 * shows a message when the bribe button is pushed
	 * 
	 * @param bribed
	 *            whether its bribed
	 * @param numBribed
	 *            the number of ships bribed
	 */
	public void showBribe(boolean bribed, int numBribed) {
		if (bribed) {
			this.bribed = true;
			bribeMessage.setText("successfully bribed " + numBribed + " units");
		} else {
			bribeMessage.setText("Not Enough Money Or Already Bribed");
		}
		this.add(bribeMessage);
		this.setVisible(true);
		repaint();
	}

	/**
	 * gets whether the planet's been bribed
	 * @return
	 */
	public boolean getBribed() {
		return bribed;
	}

	@Override
	public int update(float time) {
		if (attack.buttonPressed()) {
			return State.PLAY;
		} else if (retreat.buttonPressed()) {
			return State.EXIT;
		} else if (showUnits.buttonPressed()) {
			showUnits.releaseButton();
			return State.SPEND_CRYSTAL;
		} else if (bribe.buttonPressed()) {
			bribe.releaseButton();
			return State.BRIBE;
		} else if (trade.buttonPressed()) {
			trade.releaseButton();
			return State.TRADE;
		} else if (purchase.buttonPressed()) {
			purchase.releaseButton();
			return State.PURCHASE;
		}
		return 0;
	}

}
