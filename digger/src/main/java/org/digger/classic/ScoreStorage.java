package org.digger.classic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ScoreStorage {

	public static void createInStorage(Scores mem) {
		try {
			writeToStorage(mem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeToStorage(Scores mem) {
		try {
			File scoFile = getScoreFile();
			FileOutputStream fileOut = new FileOutputStream(scoFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOut));
			String[] scoreinit = mem.scoreinit;
			long[] scorehigh = mem.scorehigh;
			for (int i = 0; i < 10; i++) {
				bw.append(scoreinit[i]);
				bw.newLine();
				bw.append(Long.toString(scorehigh[i]));
				bw.newLine();
			}
			bw.flush();
			bw.close();
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static File getScoreFile() {
		String fileName = "digger.sco";
		File filePath = new File(fileName);
		return filePath.getAbsoluteFile();
	}

	public static boolean readFromStorage(Scores mem) {
		try {
			File scoFile = getScoreFile();
			if (!scoFile.exists() || !scoFile.canRead())
				return false;
			FileInputStream fileIn = new FileInputStream(scoFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fileIn));
			ScoreTuple[] sc = new ScoreTuple[10];
			for (int i = 0; i < 10; i++) {
				String name = br.readLine();
				int score = Integer.parseInt(br.readLine());
				sc[i] = new ScoreTuple(name, score);
			}
			br.close();
			fileIn.close();
			mem.scores = sc;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}