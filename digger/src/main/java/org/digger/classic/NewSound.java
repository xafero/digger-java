package org.digger.classic;

public class NewSound {

	static private class DIGGER_SOUND_DATA extends MusicNotes {
		static final String NORMAL_MIDI_FILE = "/audio/popcorn.mid";
		static final String BONUS_MIDI_FILE = "/audio/williamtell.mid";
		static final String DEATH_MIDI_FILE = "/audio/funeral.mid";
		static final String NEW_LEVEL_MIDI_FILE = "/audio/level.mid";
		static final String BONUS_PULSE_MIDI_FILE = "/audio/bonus_pulse.mid";

		static final int MONEY_MIDI_LOW = D4;
		static final int MONEY_MIDI_HIGH = MONEY_MIDI_LOW + 30;
		static final int MONEY_JUMP = 3;
		static final byte MONEY_NOTE_LENGTH = MusicNotes.NS;
		static final int MONEY_TEMPO = 60;

		static final byte[] EAT_MONSTER_TUNE = { D5, NS, D6, NS, D7, NS, D5, NS, D6, NS, D7, NS, D5, NS, D6, NS, D7,
				NS };
		static final int EAT_MONSTER_TEMPO = 60;
	}

	private boolean muteFlag = false;
	public static final int NO_MUSIC_MODE = 0;
	public static final int MIDI_MUSIC_MODE = 1;
	private int musicMode = NO_MUSIC_MODE;

	private SoundPlayer normalBackgroundPlayer = null;
	private SoundPlayer bonusBackgroundPlayer = null;
	private SoundPlayer deathPlayer = null;
	private SoundPlayer newLevelPlayer = null;
	private SoundPlayer bonusPulseMidiPlayer = null;

	private final SoundPlayer eatMonsterPlayer = new PianoPlayer(DIGGER_SOUND_DATA.EAT_MONSTER_TUNE,
			DIGGER_SOUND_DATA.EAT_MONSTER_TEMPO);
	private final SoundPlayer moneyEatPlayer;

	public NewSound() {
		setMusicMode(MIDI_MUSIC_MODE);

		// Create money player
		byte[] moneySeq = new byte[(DIGGER_SOUND_DATA.MONEY_MIDI_HIGH - DIGGER_SOUND_DATA.MONEY_MIDI_LOW) * 2
				/ DIGGER_SOUND_DATA.MONEY_JUMP];
		byte low = DIGGER_SOUND_DATA.MONEY_MIDI_LOW;
		byte high = DIGGER_SOUND_DATA.MONEY_MIDI_HIGH;
		int pos = 0;
		boolean toggle = true;
		while (low < high) {
			moneySeq[pos] = toggle ? low : high;
			moneySeq[pos + 1] = DIGGER_SOUND_DATA.MONEY_NOTE_LENGTH;
			pos += 2;
			if (toggle) {
				low += DIGGER_SOUND_DATA.MONEY_JUMP;
			} else {
				high -= DIGGER_SOUND_DATA.MONEY_JUMP;
			}
			toggle = !toggle;
		}
		moneyEatPlayer = new PianoPlayer(moneySeq, DIGGER_SOUND_DATA.MONEY_TEMPO);
	}

	public void setMusicMode(int newMusicMode) {
		if (musicMode == newMusicMode) {
			return;
		}

		if (musicMode != NO_MUSIC_MODE) {
			killAll();
			normalBackgroundPlayer.close();
			bonusBackgroundPlayer.close();
			deathPlayer.close();
			newLevelPlayer.close();
			bonusPulseMidiPlayer.close();
		}

		switch (newMusicMode) {
		case MIDI_MUSIC_MODE:
			normalBackgroundPlayer = new MidiPlayer(DIGGER_SOUND_DATA.NORMAL_MIDI_FILE);
			bonusBackgroundPlayer = new MidiPlayer(DIGGER_SOUND_DATA.BONUS_MIDI_FILE);
			deathPlayer = new MidiPlayer(DIGGER_SOUND_DATA.DEATH_MIDI_FILE);
			newLevelPlayer = new MidiPlayer(DIGGER_SOUND_DATA.NEW_LEVEL_MIDI_FILE);
			bonusPulseMidiPlayer = new MidiPlayer(DIGGER_SOUND_DATA.BONUS_PULSE_MIDI_FILE);
			break;
		}

		musicMode = newMusicMode;
	}

	public int getMusicMode() {
		return musicMode;
	}

	public void killAll() {
		normalBackgroundPlayer.stopPlaying();
		bonusBackgroundPlayer.stopPlaying();
		deathPlayer.stopPlaying();
		fallEnd();
		fireEnd();
		newLevelPlayer.stopPlaying();
		endBonusPulse();
	}

	public void fallEnd() {
		// TODO Auto-generated method stub

	}

	public void playBagBreak() {
		// TODO Auto-generated method stub

	}

	public void playMoneyEat() {
		if (!muteFlag) {
			moneyEatPlayer.playSingle();
		}
	}

	private boolean normalMusicMode = false;

	public void startNormalBackgroundMusic() {
		normalMusicMode = true;
		if (!muteFlag) {
			normalBackgroundPlayer.playInLoop();
		}
	}

	public void stopNormalBackgroundMusic() {
		normalMusicMode = false;
		if (!muteFlag) {
			normalBackgroundPlayer.stopPlaying();
		}
	}

	private boolean bonusMusicMode = false;

	public void startBonusBackgroundMusic() {
		bonusMusicMode = true;
		if (!muteFlag) {
			bonusBackgroundPlayer.playInLoop();
		}
	}

	public void stopBonusBackgroundMusic() {
		bonusMusicMode = false;
		if (!muteFlag) {
			bonusBackgroundPlayer.stopPlaying();
		}
	}

	public void playDeath() {
		if (!muteFlag) {
			deathPlayer.playSingle();
		}
	}

	public void startBonusPulse() {
		// TODO Auto-generated method stub

	}

	public void endBonusPulse() {
		// TODO Auto-generated method stub

	}

	public void fallStart() {
		// TODO Auto-generated method stub

	}

	public void playMonsterEat() {
		if (!muteFlag) {
			eatMonsterPlayer.playSingle();
		}
	}

	public void playExplode() {
		// TODO Auto-generated method stub

	}

	public void playLooseLevel(int wt) {
		// TODO Auto-generated method stub

	}

	public void fireEnd() {
		// TODO Auto-generated method stub

	}

	public void playNewLevel() {
		if (muteFlag) {
			return;
		}
		try {
			newLevelPlayer.playSingle();
			while (newLevelPlayer.isPlaying()) {
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
		}
	}

	public void playEatEmerald(boolean b) {
		// TODO Auto-generated method stub

	}

	public void fireStart() {
		// TODO Auto-generated method stub

	}
}