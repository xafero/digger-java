package org.digger.classic;

import java.util.AbstractMap;

public class ScoreTuple extends AbstractMap.SimpleEntry<String, Integer> {

	private static final long serialVersionUID = 7101577917702053423L;

	public ScoreTuple(String key, int value) {
		super(key, value);
	}
}
