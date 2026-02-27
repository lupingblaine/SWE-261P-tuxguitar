package app.tuxguitar.player.base;

// part5 added
public class MidiSynthesizerProxy implements MidiSynthesizer{

	private MidiSynthesizer midiSynthesizer;
	private boolean strictMode; // part5 added

	public MidiSynthesizerProxy(){
		super();
		this.strictMode = false; // part5 added
	}

	public MidiChannel openChannel(int channelId) throws MidiPlayerException {
		if( this.midiSynthesizer != null ){
			return this.midiSynthesizer.openChannel(channelId);
		}
		if (this.strictMode) { // part5 added
			throw new MidiPlayerException("MidiSynthesizer is not set"); // part5 added
		}// part5 added
		return null;// part5 added
	}

	public void closeChannel(MidiChannel midiChannel) throws MidiPlayerException {
		if( this.midiSynthesizer != null ){
			this.midiSynthesizer.closeChannel(midiChannel);
			return; // part5 added
		}
		if (this.strictMode) { // part5 added
			throw new MidiPlayerException("MidiSynthesizer is not set"); // part5 added
		}
	}

	public boolean isChannelOpen(MidiChannel midiChannel) throws MidiPlayerException {
		if( this.midiSynthesizer != null ){
			return this.midiSynthesizer.isChannelOpen(midiChannel);
		}
		if (this.strictMode) { // part5 added
			throw new MidiPlayerException("MidiSynthesizer is not set"); // part5 added
		} // part5 added
		return false;
	}

	public boolean isBusy() throws MidiPlayerException {
		if( this.midiSynthesizer != null ){
			return this.midiSynthesizer.isBusy();
		}
		if (this.strictMode) { // part5 added
			throw new MidiPlayerException("MidiSynthesizer is not set"); // part5 added
		} // part5 added
		return false;
	}

	public MidiSynthesizer getMidiSynthesizer() {
		return this.midiSynthesizer;
	}

	public void setMidiSynthesizer(MidiSynthesizer midiSynthesizer) {
		this.midiSynthesizer = midiSynthesizer;
	}

	public boolean isStrictMode() { // part5 added
		return strictMode; // part5 added
	} // part5 added

	public void setStrictMode(boolean strictMode) { // part5 added
		this.strictMode = strictMode; // part5 added
	} // part5 added
}
