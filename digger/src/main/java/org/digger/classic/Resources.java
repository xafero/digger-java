package org.digger.classic;

import java.net.URL;

public final class Resources {

	public static URL findResource(String name) {
		 URL url = Resources.class.getResource(name);
		 return url;
	}
}