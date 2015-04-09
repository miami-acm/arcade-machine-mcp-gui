PFont bitFont;
ArrayList<Game> games;
boolean gamesLoaded = false;
int page = 1;
int maxPage = 1;
final int GAMES_PER_PAGE = 5;
final color HIGHLIGHT_COLOR = color(0, 0, 255);
int selection = 0;

MenuState state = MenuState.LOADING;

void setup() {
	thread("loadGames");
	noCursor();
	background(0, 0, 0);

	bitFont = createFont("PressStart2P.ttf", 32);
	textFont(bitFont);
	textAlign(CENTER, CENTER);
	rectMode(CENTER);
}

void draw() {
	background(0, 0, 0);
	switch (state) {
		case LOADING:
			text("LOADING...", width/2, height/2);

			if (gamesLoaded) {
				state = MenuState.IN_MENU;
			}
			break;
		case IN_MENU:
			drawMenu();
			break;
	}
	// debug();
}

public int sketchWidth() {
	return displayWidth;
}

public int sketchHeight() {
	return displayHeight;
}

void drawMenu() {
	int hPos = width / 2;
	int vPos = 500 + selection * 100;

	fill(HIGHLIGHT_COLOR);
	rect(hPos, vPos, 500, 45);

	fill(255);
	for (int i = 0; i < GAMES_PER_PAGE; i++) {
		vPos = 500 + i * 100;

		text(games.get(i * page).name, hPos, vPos);
	}
}

void debug() {
	stroke(255);
	line(0, mouseY, width, mouseY);
	line(mouseX, 0, mouseX, height);

	text(mouseX + ", " + mouseY, width/2, 20);
}

//public String sketchRenderer() {
	//return P3D;
//}

boolean sketchFullScreen() {
	return true;
}

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
