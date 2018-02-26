package net.glowstone.util;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

import org.bukkit.Sound;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

public class SoundInfoTest {

	private final Sound testSound = Sound.AMBIENT_CAVE;

	/**
	 * Any SoundInfo object is currently expected to have a volume and pitch equal to 1. Should this behavior ever be
	 * changed, this characterization test will warn devs
	 */
	@Test
	public void testSoundInfoConstructor() {
		SoundInfo testSoundInfo = new SoundInfo(testSound);
		assertThat((float) Whitebox.getInternalState(testSoundInfo, "volume"), is(1.0F));
		assertThat((float) Whitebox.getInternalState(testSoundInfo, "pitch"), is(1.0F));
	}
}
