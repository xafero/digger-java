package org.digger.classic;

import javax.swing.JFrame;

public class Launcher {

	public static void main(String[] args) {
		Digger game = new Digger();
		game.setFocusable(true);
		game.init();
		game.start();

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Digger Remastered");
		frame.setSize((int) (game.width * 4.03), (int) (game.height * 4.15));
		frame.setLocationRelativeTo(null);

		frame.getContentPane().add(game);
		frame.setVisible(true);
	}
}
