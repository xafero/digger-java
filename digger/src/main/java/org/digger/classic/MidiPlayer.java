package org.digger.classic;

import java.io.IOException;
import java.net.URL;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class MidiPlayer implements SoundPlayer {

	private final Sequencer player;
	private boolean nonZeroPosition;

	public MidiPlayer(String path) {
		try {
			player = loadMidi(getMidiSystem(), path);
		} catch (InvalidMidiDataException | IOException | MidiUnavailableException e) {
			throw new UnsupportedOperationException(e);
		}
		nonZeroPosition = false;
	}

	@Override
	public void playInLoop() {
		if (isPlaying())
			return;
		try {
			prepareToPlay();
			player.setLoopCount(-1);
			nonZeroPosition = true;
			player.start();
		} catch (Exception ex) {
		}
	}

	@Override
	public void playSingle() {
		if (isPlaying())
			return;
		try {
			prepareToPlay();
			player.setLoopCount(1);
			nonZeroPosition = true;
			player.start();
		} catch (Exception ex) {
		}
	}

	@Override
	public void stopPlaying() {
		try {
			if (isPlaying())
				player.stop();
			rewind();
		} catch (Exception e) {
		}
	}

	@Override
	public void pausePlaying() {
		try {
			if (!isPlaying())
				return;
			player.stop();
		} catch (Exception e) {
		}
	}

	@Override
	public void rewind() {
		try {
			if (nonZeroPosition)
				player.setTickPosition(0);
			nonZeroPosition = false;
		} catch (Exception ex) {
		}
	}

	public boolean isPlaying() {
		return player.isRunning();
	}

	@Override
	public void close() {
		player.close();
	}

	public boolean isClosed() {
		return !player.isOpen();
	}

	private void prepareToPlay() {
		// NO-OP
	}

	private static Sequencer loadMidi(Sequencer sequencer, String path) throws InvalidMidiDataException, IOException {
		URL midi = Resources.findResource(path);
		Sequence sequence = MidiSystem.getSequence(midi);
		sequencer.setSequence(sequence);
		return sequencer;
	}

	private static Sequencer getMidiSystem() throws MidiUnavailableException {
		Sequencer sequencer = MidiSystem.getSequencer();
		if (sequencer == null)
			throw new UnsupportedOperationException("Sequencer not supported!");
		sequencer.open();
		return sequencer;
	}
}