package handlers;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineEvent.Type;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;

/**
 * a sound
 * 
 * 
 *
 */
public class Sound extends Thread {

	private Clip clip;
	private FloatControl gainControl;

	/**
	 * creates a new sound to be played
	 * 
	 * @param path
	 *            the path to the sound
	 * @param volume
	 *            the volume to play it at
	 */
	public Sound(String path, float volume) {
		try {
			AudioInputStream sound = ResourceHandler.getSound(path);
			DataLine.Info info = new DataLine.Info(Clip.class, sound.getFormat());
			clip = (Clip) AudioSystem.getLine(info);
			clip.open(sound);
			clip.addLineListener(new LineListener() {

				@Override
				public void update(LineEvent event) {
					if (event.getType() == Type.STOP) {
						clip.close();
					}
				}

			});
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			setVolume(volume);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * plays the clip
	 */
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * stops the clip
	 */
	public void stopClip() {
		clip.stop();
		clip.close();
	}

	/**
	 * loops the clip
	 */
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	/**
	 * sets the volume of this sound
	 * 
	 * @param volume
	 *            the new volume
	 */
	public void setVolume(float volume) {
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}

	@Override
	public void run() {
		play();
	}

}
