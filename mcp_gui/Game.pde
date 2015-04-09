public class Game {
	String name;
	PImage image;
	String path;

	public Game(String name, String path, String imagePath) {
		this.name = name;
		this.path = path;
		this.image = loadImage(imagePath);
	}

	public void run() {
		println("Running game " + name);
		exit();
	}
}
