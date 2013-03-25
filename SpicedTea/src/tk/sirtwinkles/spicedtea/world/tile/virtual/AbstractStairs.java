package tk.sirtwinkles.spicedtea.world.tile.virtual;

import tk.sirtwinkles.spicedtea.world.tile.StairDirection;
import tk.sirtwinkles.spicedtea.world.tile.Tile;

public class AbstractStairs extends Tile {
	private StairDirection direction;
	
	public AbstractStairs(String id, int imgX, int imgY, int x, int y, StairDirection dir) {
		super(id, imgX, imgY, x, y);
		this.direction = dir;
	}
}
