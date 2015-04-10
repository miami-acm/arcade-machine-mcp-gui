package mcp;

import processing.core.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Game {
	String name;
	PImage image;
	String path;
	PApplet parent;
	static final String GAME_RULE = "'([-_a-zA-Z0-9. ]+)' '([-_a-zA-Z0-9. /]+)' '([-_a-zA-Z0-9. /]+)'";
	static final Pattern GAME_PATTERN = Pattern.compile(GAME_RULE);

	public Game(PApplet parent, String name, String path, String imagePath) {
		this.parent = parent;
		this.name = name;
		this.path = path;
		this.image = parent.loadImage(imagePath);
	}

	public void run() {
		parent.println("Running game " + name);
		parent.exit();
	}

	public static Game fromString(PApplet parent, String line) {
		Matcher lineMatcher = GAME_PATTERN.matcher(line);

		if (!lineMatcher.matches()) {
			throw new IllegalArgumentException("The inputted line was malformed.\n" + line);
		}

		String name = lineMatcher.group(1);
		String path = lineMatcher.group(2);
		String image = lineMatcher.group(3);

		Game g = new Game(parent, name, path, image);
		return g;
	}

	public String toString() {
		return String.format("Game [name=%s path=%s image=%s]", name, path, image);
	}
}
