PFont bitFont;
ArrayList<Game> games;
boolean gamesLoaded = false;

MenuState state = MenuState.LOADING;

void setup() {
	thread("loadGames");
	noCursor();
	background(0, 0, 0);

	bitFont = createFont("Press Start 2P", 32);
	textFont(bitFont);
	textAlign(CENTER, CENTER);
}

void draw() {
	switch (state) {
		case LOADING:
			text("LOADING...", width/2, height/2);

			if (gamesLoaded) {
				state = MenuState.IN_MENU;
			}
			break;
		case IN_MENU:
			background(0, 0, 0);
			break;
	}
}

public int sketchWidth() {
	return displayWidth;
}

public int sketchHeight() {
	return displayHeight;
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
	gamesLoaded = true;
}
