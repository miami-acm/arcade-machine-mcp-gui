package mcp;

import com.etsy.net.JUDS;
import com.etsy.net.UnixDomainSocketClient;
import java.io.*;
import processing.core.*;

public class McpProxy {
	UnixDomainSocketClient client;
	BufferedReader in;
	PrintWriter out;
	PApplet parent;

	static final String GET_GAMES = "GET GAMES";
	static final String NUM_GAMES = "NUM GAMES";
	static final String DONE_GAMES = "DONE GAMES";
	static final String DEFAULT_LOCATION = "/tmp/mcp";

	/**
	 * Construct a new McpProxy object that listens on the <code>fd</code> file
	 * descriptor.
	 *
	 * @param fd the file descriptor to listen on
	 * @param parent the parent PApplet
	 */
 	public McpProxy(PApplet parent, String fd) {
		try {
			client = new UnixDomainSocketClient(fd, JUDS.SOCK_STREAM);

			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			// TODO: Add some graceful exception handling here
			System.err.println("Could not connect to the socket.");
			System.exit(1);
		}

		this.parent = parent;
	}

	/**
	 * Construct a new McpProxy object that listens on the default file
	 * descriptor stored in <code>DEFAULT_LOCATION</code>
	 *
	 * @param parent the parent PApplet
	 */
	public McpProxy(PApplet parent) {
		this(parent, DEFAULT_LOCATION);
	}

	/**
	 * Return an array of all of the <code>Game</code> objects that are managed by the MCP.
	 *
	 * @return array of <code>Game</code> objects
	 */
	public Game[] getGames() {
		int num = numGames();
		Game[] games = new Game[num];

		out.println(GET_GAMES);

		String line = "";
		try {
			for (int i = 0; i < num; i++) {
				line = in.readLine();
				games[i] = Game.fromString(parent, line);
			}
		} catch (IOException e) {
			System.err.println("There was a problem communicating with the MCP.");
		}

		return games;
	}

	/**
	 * Return the number of games being managed by the MCP. If there was an
	 * error communicating with the MCP, or there was a problem with the message
	 * the MCP sent, -1 will be returned. Otherwise expect a positive value
	 * (including 0).
	 *
	 * @return number of games managed by MCP
	 */
	public int numGames() {
		out.println(NUM_GAMES);
		int num = -1;

		try {
			num = Integer.parseInt(in.readLine());
		} catch (IOException e) {
			System.err.println("There was a problem communicating with the MCP.");
		} catch (NumberFormatException e) {
			System.err.println("The value returned by the MCP was not a valid number.");
		}

		return num;
	}
}
