package mcp;

import processing.core.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Game {
	String name;
	PImage image;
	String path;
	PApplet parent;
	int id;
	static final String GAME_RULE = "'([-_a-zA-Z0-9. ]+)' '([-_a-zA-Z0-9. /]+)' '([-_a-zA-Z0-9. /]+)' '([0-9]+)'";
	static final Pattern GAME_PATTERN = Pattern.compile(GAME_RULE);

	public Game(PApplet parent, String name, String path, String imagePath, int id) {
		this.parent = parent;
		this.name = name;
		this.path = path;
		this.image = parent.loadImage(imagePath);
		this.id = id;
	}

	public static Game fromString(PApplet parent, String line) {
		Matcher lineMatcher = GAME_PATTERN.matcher(line);

		if (!lineMatcher.matches()) {
			throw new IllegalArgumentException("The inputted line was malformed.\n" + line);
		}

		String name = lineMatcher.group(1);
		String path = lineMatcher.group(2);
		String image = lineMatcher.group(3);
		String idStr = lineMatcher.group(4);
		int id = Integer.parseInt(idStr);

		Game g = new Game(parent, name, path, image, id);
		return g;
	}

	public String toString() {
		return String.format("Game [name=%s path=%s image=%s]", name, path, image);
	}
}
