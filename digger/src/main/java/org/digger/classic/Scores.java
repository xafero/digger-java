package org.digger.classic;

import java.net.*;
import java.io.*;
import java.util.*;

class Scores {

	Digger dig;
	ScoreTuple[] scores;
	String substr;

	char highbuf[] = new char[10];
	long scorehigh[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }; // [12]
	String scoreinit[] = new String[11];
	String scoreinit2[] = { "...", "...", "...", "...", "...", "...", "...", "...", "...", "...", "..." };
	long scoret = 0, score1 = 0, score2 = 0, nextbs1 = 0, nextbs2 = 0;
	String hsbuf;
	char scorebuf[] = new char[512];
	int bonusscore = 20000;
	boolean gotinitflag = false;

	Scores(Digger d) {
		dig = d;
	}

	public ScoreTuple[] _submit(String n, int s) {
		if (dig.subaddr != null) {
			int ms = 16 + (int) (System.currentTimeMillis() % (65536 - 16));
			substr = n + '+' + s + '+' + ms + '+' + ((ms + 32768) * s) % 65536;
			// new Thread(this).start();
		}
		return scores;
	}

	public void _updatescores(ScoreTuple[] o) {

		if (o == null)
			return;

		try {
			String[] in = new String[10];
			int[] sc = new int[10];
			for (int i = 0; i < 10; i++) {
				in[i] = o[i].getKey();
				sc[i] = o[i].getValue();
			}
			for (int i = 0; i < 10; i++) {
				scoreinit[i + 1] = in[i];
				scorehigh[i + 2] = sc[i];
			}
		} catch (Exception e) {
		}
		;

	}

	void addscore(int score) {
		if (dig.Main.getcplayer() == 0) {
			score1 += score;
			if (score1 > 999999l)
				score1 = 0;
			writenum(score1, 0, 0, 6, 1);
			if (score1 >= nextbs1) {
				if (dig.Main.getlives(1) < 5) {
					dig.Main.addlife(1);
					dig.Drawing.drawlives();
				}
				nextbs1 += bonusscore;
			}
		} else {
			score2 += score;
			if (score2 > 999999l)
				score2 = 0;
			if (score2 < 100000l)
				writenum(score2, 236, 0, 6, 1);
			else
				writenum(score2, 248, 0, 6, 1);
			if (score2 > nextbs2) { /* Player 2 doesn't get the life until >20,000 ! */
				if (dig.Main.getlives(2) < 5) {
					dig.Main.addlife(2);
					dig.Drawing.drawlives();
				}
				nextbs2 += bonusscore;
			}
		}
		dig.Main.incpenalty();
		dig.Main.incpenalty();
		dig.Main.incpenalty();
	}

	void drawscores() {
		writenum(score1, 0, 0, 6, 3);
		if (dig.Main.nplayers == 2)
			if (score2 < 100000l)
				writenum(score2, 236, 0, 6, 3);
			else
				writenum(score2, 248, 0, 6, 3);
	}

	void endofgame() {
		int i, j, z;
		addscore(0);
		if (dig.Main.getcplayer() == 0)
			scoret = score1;
		else
			scoret = score2;
		if (scoret > scorehigh[11]) {
			dig.Pc.gclear();
			drawscores();
			dig.Main.pldispbuf = "PLAYER ";
			if (dig.Main.getcplayer() == 0)
				dig.Main.pldispbuf += "1";
			else
				dig.Main.pldispbuf += "2";
			dig.Drawing.outtext(dig.Main.pldispbuf, 108, 0, 2, true);
			dig.Drawing.outtext(" NEW HIGH SCORE ", 64, 40, 2, true);
			getinitials();
			_updatescores(_submit(scoreinit[0], (int) scoret));
			shufflehigh();
			ScoreStorage.writeToStorage(this);
		} else {
			dig.Main.cleartopline();
			dig.Drawing.outtext("GAME OVER", 104, 0, 3, true);
			_updatescores(_submit("...", (int) scoret));
			dig.Sound.killsound();
			for (j = 0; j < 20; j++) /* Number of times screen flashes * 2 */
				for (i = 0; i < 2; i++) { // i<8;i++) {
					dig.Sprite.setretr(true);
//		dig.Pc.ginten(1);
					dig.Pc.gpal(1 - (j & 1));
					dig.Sprite.setretr(false);
					for (z = 0; z < 111; z++)
						; /* A delay loop */
					dig.Pc.gpal(0);
//		dig.Pc.ginten(0);
					dig.Pc.ginten(1 - i & 1);
					dig.newframe();
				}
			dig.Sound.setupsound();
			dig.Drawing.outtext("         ", 104, 0, 3, true);
			dig.Sprite.setretr(true);
		}
	}

	void flashywait(int n) {
		/*
		 * int i,gt,cx,p=0,k=1; int gap=19; dig.Sprite.setretr(false); for
		 * (i=0;i<(n<<1);i++) { for (cx=0;cx<dig.Sound.volume;cx++) {
		 * dig.Pc.gpal(p=1-p); for (gt=0;gt<gap;gt++); } }
		 */

		try {
			Thread.sleep(n * 2);
		} catch (Exception e) {
		}

	}

	int getinitial(int x, int y) {
		int i, j;
		dig.Input.keypressed = 0;
		dig.Pc.gwrite(x, y, '_', 3, true);
		for (j = 0; j < 5; j++) {
			for (i = 0; i < 40; i++) {
				if ((dig.Input.keypressed & 0x80) == 0 && dig.Input.keypressed != 0)
					return dig.Input.keypressed;
				flashywait(15);
			}
			for (i = 0; i < 40; i++) {
				if ((dig.Input.keypressed & 0x80) == 0 && dig.Input.keypressed != 0) {
					dig.Pc.gwrite(x, y, '_', 3, true);
					return dig.Input.keypressed;
				}
				flashywait(15);
			}
		}
		gotinitflag = true;
		return 0;
	}

	void getinitials() {
		int k, i;
		dig.Drawing.outtext("ENTER YOUR", 100, 70, 3, true);
		dig.Drawing.outtext(" INITIALS", 100, 90, 3, true);
		dig.Drawing.outtext("_ _ _", 128, 130, 3, true);
		scoreinit[0] = "...";
		dig.Sound.killsound();
		gotinitflag = false;
		for (i = 0; i < 3; i++) {
			k = 0;
			while (k == 0 && !gotinitflag) {
				k = getinitial(i * 24 + 128, 130);
				if (i != 0 && k == 8)
					i--;
				k = dig.Input.getasciikey(k);
			}
			if (k != 0) {
				dig.Pc.gwrite(i * 24 + 128, 130, k, 3, true);
				StringBuffer sb = new StringBuffer(scoreinit[0]);
				sb.setCharAt(i, (char) k);
				scoreinit[0] = sb.toString();
			}
		}
		dig.Input.keypressed = 0;
		for (i = 0; i < 20; i++)
			flashywait(15);
		dig.Sound.setupsound();
		dig.Pc.gclear();
		dig.Pc.gpal(0);
		dig.Pc.ginten(0);
		dig.newframe(); // needed by Java version!!
		dig.Sprite.setretr(true);
	}

	void initscores() {
		addscore(0);
	}

	void loadscores() {
		int p = 1, i, x;
		// readscores();
		for (i = 1; i < 11; i++) {
			for (x = 0; x < 3; x++)
				scoreinit[i] = "..."; // scorebuf[p++]; --- zmienic
			p += 2;
			for (x = 0; x < 6; x++)
				highbuf[x] = scorebuf[p++];
			scorehigh[i + 1] = 0; // atol(highbuf);
		}
		if (scorebuf[0] != 's')
			for (i = 0; i < 11; i++) {
				scorehigh[i + 1] = 0;
				scoreinit[i] = "...";
			}
	}

	String numtostring(long n) {
		int x;
		String p = "";
		for (x = 0; x < 6; x++) {
			p = String.valueOf(n % 10) + p;
			n /= 10;
			if (n == 0) {
				x++;
				break;
			}
		}
		for (; x < 6; x++)
			p = ' ' + p;
		return p;
	}

	public void init() {
		if (!ScoreStorage.readFromStorage(this))
			ScoreStorage.createInStorage(this);
	}

	void scorebonus() {
		addscore(1000);
	}

	void scoreeatm() {
		addscore(dig.eatmsc * 200);
		dig.eatmsc <<= 1;
	}

	void scoreemerald() {
		addscore(25);
	}

	void scoregold() {
		addscore(500);
	}

	void scorekill() {
		addscore(250);
	}

	void scoreoctave() {
		addscore(250);
	}

	void showtable() {
		int i, col;
		dig.Drawing.outtext("HIGH SCORES", 16, 25, 3);
		col = 2;
		for (i = 1; i < 11; i++) {
			hsbuf = scoreinit[i] + "  " + numtostring(scorehigh[i + 1]);
			dig.Drawing.outtext(hsbuf, 16, 31 + 13 * i, col);
			col = 1;
		}
	}

	void shufflehigh() {
		int i, j;
		for (j = 10; j > 1; j--)
			if (scoret < scorehigh[j])
				break;
		for (i = 10; i > j; i--) {
			scorehigh[i + 1] = scorehigh[i];
			scoreinit[i] = scoreinit[i - 1];
		}
		scorehigh[j + 1] = scoret;
		scoreinit[j] = scoreinit[0];
	}

	void writecurscore(int bp6) {
		if (dig.Main.getcplayer() == 0)
			writenum(score1, 0, 0, 6, bp6);
		else if (score2 < 100000l)
			writenum(score2, 236, 0, 6, bp6);
		else
			writenum(score2, 248, 0, 6, bp6);
	}

	void writenum(long n, int x, int y, int w, int c) {
		int d, xp = (w - 1) * 12 + x;
		while (w > 0) {
			d = (int) (n % 10);
			if (w > 1 || d > 0)
				dig.Pc.gwrite(xp, y, d + '0', c, false); // true
			n /= 10;
			w--;
			xp -= 12;
		}
	}

	void zeroscores() {
		score2 = 0;
		score1 = 0;
		scoret = 0;
		nextbs1 = bonusscore;
		nextbs2 = bonusscore;
	}
}
