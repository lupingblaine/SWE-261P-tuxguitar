package app.tuxguitar.player.base;

// part5: test for newly testable strict-mode behavior in MidiSynthesizerProxy.
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TestMidiSynthesizerProxyTestability {

	@Test
	public void testStrictModeThrowsWhenSynthesizerIsMissing() {
		MidiSynthesizerProxy proxy = new MidiSynthesizerProxy();
		proxy.setStrictMode(true);

		assertThrows(MidiPlayerException.class, () -> proxy.openChannel(1));
		assertThrows(MidiPlayerException.class, () -> proxy.closeChannel(null));
		assertThrows(MidiPlayerException.class, () -> proxy.isChannelOpen(null));
		assertThrows(MidiPlayerException.class, () -> proxy.isBusy());
	}
}
