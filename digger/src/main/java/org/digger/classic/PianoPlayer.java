package org.digger.classic;

import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

public class PianoPlayer implements SoundPlayer {

	private final Synthesizer player;
	private final MidiChannel instr;
	private final byte[] tunes;
	private final int tempo;

	public PianoPlayer(byte[] tunes, int tempo) {
		try {
			player = getMidiSystem();
			instr = loadChannel(player);
			this.tunes = tunes;
			this.tempo = tempo;
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private static MidiChannel loadChannel(Synthesizer synthesizer) throws InvalidMidiDataException, IOException {
		MidiChannel channel = synthesizer.getChannels()[0];
		return channel;
	}

	private static Synthesizer getMidiSystem() throws MidiUnavailableException {
		Synthesizer synthesizer = MidiSystem.getSynthesizer();
		if (synthesizer == null)
			throw new UnsupportedOperationException("Synthesizer not supported!");
		synthesizer.open();
		return synthesizer;
	}

	@Override
	public void playSingle() {
		for (byte tune : tunes) {
			instr.noteOn(tune, tempo);
		}
	}

	@Override
	public void playInLoop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopPlaying() {

		// TODO Auto-generated method stub

	}

	@Override
	public void pausePlaying() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isPlaying() {
		return false;
	}

	@Override
	public void rewind() {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() {
		player.close();
	}

	public boolean isClosed() {
		return !player.isOpen();
	}
}