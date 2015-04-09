import java.util.HashSet;

final int GAMES_PER_PAGE = 5;

// Color of the bar used to highlight the selected game
final color HIGHLIGHT_COLOR = color(0, 0, 255);

final int BAR_WIDTH = 600;
final int BAR_HEIGHT = 45;

final int IMAGE_WIDTH = 300;
final int IMAGE_HEIGHT = 300;

PFont bitFont;

ArrayList<Game> games;
HashSet<Character> anyKey;

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

void setup() {
	thread("loadGames");
	thread("loadAnyKey");

	noCursor();
	background(0, 0, 0);

	bitFont = createFont("PressStart2P.ttf", 32);
	textFont(bitFont);
	textAlign(CENTER, CENTER);
	rectMode(CENTER);
	imageMode(CENTER);
}

void draw() {
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
void drawMenu() {
	fill(HIGHLIGHT_COLOR);
	rect(width/2, 200, IMAGE_WIDTH+10, IMAGE_HEIGHT+10);

	Game selectedGame = games.get(selection * page);
	image(selectedGame.image, width/2, 200, IMAGE_WIDTH, IMAGE_HEIGHT);

	int hPos = width / 2;
	int vPos = 500 + selection * 100;

	fill(HIGHLIGHT_COLOR);
	rect(hPos, vPos, BAR_WIDTH, BAR_HEIGHT);

	fill(255);
	for (int i = 0; i < GAMES_PER_PAGE; i++) {
		vPos = 500 + i * 100;

		text(games.get(i * page).name, hPos, vPos);
	}
}

/*
 * Draw crosshairs on the screen at the mouse position and place the numeric
 * cordinates in the top left corner of the screen.
 */
void debug() {
	stroke(255);
	line(0, mouseY, width, mouseY);
	line(mouseX, 0, mouseX, height);

	text(mouseX + ", " + mouseY, width/2, 20);
}

//public String sketchRenderer() {
	//return P3D;
//}

void keyPressed() {
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
		Game selectedGame = games.get(selection * page);
		selectedGame.run();
	}
}

/*
 * Required by processing to make the sketch fullscreen
 */
boolean sketchFullScreen() {
	return true;
}

/*
 * Populate the global ArrayList `games` with the games from the configuration
 * file. Currently this is just a placeholder that populates the list with the
 * same 5 games. Sets the `gamesLoaded` variable to `true` and sets the
 * `maxPage` variable to the index of the last page.
 */
void loadGames() {
	games = new ArrayList<Game>();

	games.add(new Game("Space Invaders", "space_invaders.pde", "space_invaders.png"));
	games.add(new Game("Space Invaders", "space_invaders.pde", "space_invaders.png"));
	games.add(new Game("Space Invaders", "space_invaders.pde", "space_invaders.png"));
	games.add(new Game("Space Invaders", "space_invaders.pde", "space_invaders.png"));
	games.add(new Game("Space Invaders", "space_invaders.pde", "space_invaders.png"));

	maxPage = games.size() / GAMES_PER_PAGE + 1;

	gamesLoaded = true;
}

void loadAnyKey() {
	anyKey = new HashSet<Character>();

	anyKey.add('R');
	anyKey.add('T');
	anyKey.add('Y');
	anyKey.add('F');
	anyKey.add('G');
	anyKey.add('H');

	anyKey.add('U');
	anyKey.add('I');
	anyKey.add('O');
	anyKey.add('J');
	anyKey.add('K');
	anyKey.add('L');

	anyKeyLoaded = true;
}
