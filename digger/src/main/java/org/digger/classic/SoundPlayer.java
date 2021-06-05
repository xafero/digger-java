package org.digger.classic;

public interface SoundPlayer {

	void playSingle();

	void playInLoop();

	void stopPlaying();

	void pausePlaying();
	
	boolean isPlaying();

	void rewind();
	
	void close();
}