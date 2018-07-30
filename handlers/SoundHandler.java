package handlers;

/**
 * handles sound
 * 
 * 
 */
public class SoundHandler {

	public static String laser = "sound/laser/laser_new_3.wav", explosion = "sound/explosion/explosion.wav",
			rail_gun = "sound/laser/railGun.wav", select = "sound/select/blip_select.wav",
			menu_music = "sound/music/menu_track.wav", campaign_music = "sound/music/campaign_track_1.wav",
			battle_music = "sound/music/battle_music.wav";
	private static float masterVolume = 1.0f;
	private static Sound sound;
	public static Sound music;
	private static int threadsOpen = 0, activeClips = 0;

	/**
	 * creates and plays a sound
	 * 
	 * @param path
	 *            the path to the sound file
	 */
	public static void playSound(String path) {
		if (threadsOpen < 50 && activeClips < 50) {
			sound = new Sound(path, masterVolume);
			activeClips++;
			sound.start();
			threadsOpen++;
			try {
				sound.join();
				threadsOpen--;
				activeClips--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * loops a sound
	 * 
	 * @param path
	 *            the path to the sound
	 */
	public static void loopSong(String path) {
		if (threadsOpen < 100 && activeClips < 100) {
			music = new Sound(path, masterVolume);
			activeClips++;
			music.loop();
			threadsOpen++;
			try {
				music.join();
				threadsOpen--;
				activeClips--;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * sets the master volume of all sounds
	 * 
	 * @param volume
	 *            the master volume
	 */
	public static void setMasterVolume(float volume) {
		masterVolume = volume;
		music.setVolume(volume);
	}

}
