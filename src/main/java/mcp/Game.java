package mcp;

import processing.core.*;

public class Game {
	String name;
	PImage image;
	String path;
	PApplet parent;

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
}
