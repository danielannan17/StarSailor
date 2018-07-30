package menu;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import handlers.InputHandler;
import handlers.SoundHandler;
import main.State;

/**
 * the options menu for changing the sound level
 * 
 * 
 */
public class OptionsMenu extends Menu {

	private static final long serialVersionUID = 1L;
	private Button back;
	private Label volumeLabel;
	private Slider volume;

	/**
	 * creates a new options menu
	 * 
	 * @param bg
	 *            the background to display
	 * @param startPoint
	 *            the start point for this menu's jslider
	 */
	public OptionsMenu(String bg, int startPoint) {
		super(bg);
		this.setLayout(null);
		int width = InputHandler.screenSize.width / 10;
		int height = InputHandler.screenSize.height / 14;
		int x = (int) (InputHandler.midPoint.x / 2 - (width));
		int y = (int) (InputHandler.midPoint.y / 2 - (height / 2));
		volumeLabel = new Label("Volume: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		width = InputHandler.screenSize.width / 2;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2);
		volume = new Slider(x, y, width, height, Color.black, Color.white, 0, 100, 25, 0, true, true, 100);
		volume.setValue(startPoint);
		SoundHandler.setMasterVolume((float) ((volume.getValue() / 2 + 50) * 0.01f));
		volume.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				SoundHandler.setMasterVolume((float) ((volume.getValue() / 2 + 50) * 0.01f));
			}

		});
		width = InputHandler.screenSize.width / 10;
		height = InputHandler.screenSize.height / 14;
		x = (int) (InputHandler.midPoint.x / 2 - (width));
		volumeLabel = new Label("Volume: ", "Verdana", Font.BOLD, 20, Color.white, x, y, width, height, JLabel.LEFT);
		x = (int) (InputHandler.midPoint.x / 2 - (width / 2));
		y = (int) ((InputHandler.midPoint.y / 2) * 3 - (height / 2));
		back = new Button("Back", x, y, width, height);
		this.add(volume);
		this.add(volumeLabel);
		this.add(back);
	}

	/**
	 * gets the volume of the volume slider
	 * 
	 * @return the volume
	 */
	public int getVolume() {
		return volume.getValue();
	}

	@Override
	public int update(float time) {
		if (back.buttonPressed()) {
			return State.MAIN;
		}
		return 0;
	}

}
