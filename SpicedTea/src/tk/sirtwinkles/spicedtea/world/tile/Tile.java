package tk.sirtwinkles.spicedtea.world.tile;

public abstract class Tile {
	private String id;
	/**
	 * Image coordinates in terrain.png
	 */
	protected int imgX;
	protected int imgY;
	/**
	 * Position in the world;
	 */
	private int x, y;

	/**
	 * Constructs a new tile object.
	 * 
	 * @param id
	 *            Name of this tile.
	 * @param imgX
	 *            Image coordinates in terrain.png, in blocks of 16
	 * @param imgY
	 *            Image coordinates in terrain.png, in blocks of 16
	 * @param x
	 *            X position in the level.
	 * @param y
	 *            Y position in the level.
	 */
	public Tile(String id, int imgX, int imgY, int x, int y) {
		this.id = id;
		this.imgX = imgX;
		this.imgY = imgY;
		this.x = x;
		this.y = y;
	}

	public int getImageX() {
		return imgX;
	}

	public int getImageY() {
		return imgY;
	}
}