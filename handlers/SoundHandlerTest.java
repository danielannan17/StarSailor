package handlers;

import org.junit.Test;

public class SoundHandlerTest {

	@Test
	public void testPlaySound() {
		SoundHandler.playSound(SoundHandler.laser);
	}

	@Test
	public void testLoopSong() {
		SoundHandler.loopSong(SoundHandler.battle_music);
		SoundHandler.music.stopClip();
	}

}
