package mcp;

import java.util.HashSet;
import processing.core.*;

public class McpGui extends PApplet {
	final int GAMES_PER_PAGE = 5;

	// Color of the bar used to highlight the selected game
	final int HIGHLIGHT_COLOR = color(0, 0, 255);

	final int BAR_WIDTH = 600;
	final int BAR_HEIGHT = 45;

	final int IMAGE_WIDTH = 300;
	final int IMAGE_HEIGHT = 300;

	final int SPACE_BETWEEN_LINES = 50;

	PFont bitFont;

	Game[] games;
	HashSet<Character> anyKey;
	McpProxy masterControlProgram = new McpProxy();

	// This will be updated by the `loadGames` method after it executes. Since
	// `loadGames` runs asynchronously, this will indicate to the main thread that
	// it is safe to use the `games` ArrayList.
	boolean gamesLoaded = false;

	// This will be updated by the `loadAnyKey` method after it executes. Since
	// `loadAnyKey` runs asynchronously, this will indicate to the main thread that
	// it is safe to use the `anyKey` HashSet.
	boolean anyKeyLoaded = false;

	// The current page being displayed (1-indexed, not 0-indexed)
	int page = 1;

	// The highest page number. This will be updated by `loadGames` and should not
	// be changed manually. This, like the page number is 1-indexed, not 0-indexed.
	int maxPage = 1;

	// The index on the page of the currently selected game. This will be in the
	// range [0,GAMES_PER_PAGE)
	int selection = 0;

	MenuState state = MenuState.LOADING;

	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "mcp.McpGui" });
	}

	public void setup() {
		thread("loadGames");
		thread("loadAnyKey");

		noCursor();
		background(0, 0, 0);

		bitFont = createFont("/home/nate/git/arcade-machine-mcp-gui/src/main/java/data/PressStart2P.ttf", 32);
		textFont(bitFont);
		textAlign(CENTER, CENTER);
		rectMode(CENTER);
		imageMode(CENTER);
	}

	public void draw() {
		background(0, 0, 0);
		switch (state) {
			case LOADING:
				text("LOADING...", width/2, height/2);

				if (gamesLoaded && anyKeyLoaded) {
					state = MenuState.IN_MENU;
				}
				break;
			case IN_MENU:
				drawMenu();
				break;
		}
		// debug();
	}

	/*
	 * Required by processing to make the sketch fullscreen
	 */
	public int sketchWidth() {
		return displayWidth;
	}

	/*
	 * Required by processing to make the sketch fullscreen
	 */
	public int sketchHeight() {
		return displayHeight;
	}

	/*
	 * Draw the current page of games with the currently selected game highlighed in
	 * HIGHLIGHT_COLOR.
	 */
	public void drawMenu() {
		fill(HIGHLIGHT_COLOR);
		rect(width/2, 200, IMAGE_WIDTH+10, IMAGE_HEIGHT+10);

		Game selectedGame = games[selection * page];
		image(selectedGame.image, width/2, 200, IMAGE_WIDTH, IMAGE_HEIGHT);

		int hPos = width / 2;
		int vPos = 500 + selection * SPACE_BETWEEN_LINES;

		fill(HIGHLIGHT_COLOR);
		rect(hPos, vPos, BAR_WIDTH, BAR_HEIGHT);

		fill(255);
		for (int i = 0; i < GAMES_PER_PAGE; i++) {
			vPos = 500 + i * SPACE_BETWEEN_LINES;

			text(games[i * page].name, hPos, vPos);
		}
	}

	/*
	 * Draw crosshairs on the screen at the mouse position and place the numeric
	 * cordinates in the top left corner of the screen.
	 */
	public void debug() {
		stroke(255);
		line(0, mouseY, width, mouseY);
		line(mouseX, 0, mouseX, height);

		text(mouseX + ", " + mouseY, width/2, 20);
	}

	//public String sketchRenderer() {
		//return P3D;
	//}

	public void keyPressed() {
		if (key == 'w' || key == '8') {
			// up on the player 1 or player 2 joystick
			if (selection > 0) {
				selection--;
			}
		} else if (key == 'x' || key == '2') {
			// down on the player 1 or player 2 joystick
			if (selection < GAMES_PER_PAGE - 1) {
				selection++;
			}
		} else if (anyKey.contains(key)) {
			Game selectedGame = games[selection * page];
			selectedGame.run();
		}
	}

	/*
	 * Required by processing to make the sketch fullscreen
	 */
	public boolean sketchFullScreen() {
		return true;
	}

	/*
	 * Populate the global ArrayList `games` with the games from the configuration
	 * file. Currently this is just a placeholder that populates the list with the
	 * same 5 games. Sets the `gamesLoaded` variable to `true` and sets the
	 * `maxPage` variable to the index of the last page.
	 */
	public void loadGames() {
		games = masterControlProgram.getGames();

		maxPage = games.length / GAMES_PER_PAGE + 1;
		gamesLoaded = true;
	}

	public void loadAnyKey() {
		anyKey = new HashSet<Character>();

		// Player 1's buttons
		anyKey.add('R');
		anyKey.add('T');
		anyKey.add('Y');
		anyKey.add('F');
		anyKey.add('G');
		anyKey.add('H');

		// Player 2's buttons
		anyKey.add('U');
		anyKey.add('I');
		anyKey.add('O');
		anyKey.add('J');
		anyKey.add('K');
		anyKey.add('L');

		// Player 1 start button
		anyKey.add(' ');

		// Player 2 start button
		anyKey.add(ENTER);

		anyKeyLoaded = true;
	}
}
