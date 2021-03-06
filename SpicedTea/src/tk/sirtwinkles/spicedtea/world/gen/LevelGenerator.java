package tk.sirtwinkles.spicedtea.world.gen;

import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.BACKGROUND;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.DOOR;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.FLOOR;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.STAIR_DOWN;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.STAIR_UP;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.WALL;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.gen.dungeon.DungeonGenerator;
import tk.sirtwinkles.spicedtea.world.tile.StairDirection;
import tk.sirtwinkles.spicedtea.world.tile.Tile;

public class LevelGenerator {
	/**
	 * Generates a new level.
	 * 
	 * @param width
	 *            The width of the level to create.
	 * @param height
	 *            The height of the level to create.
	 * @param depth
	 *            The "depth" of the level to create. Modifies difficulty of
	 *            level and loot teir.
	 * @param tsp
	 *            The tile set provider to load tile data from.
	 * @return
	 */
	public static Level create(int width, int height, int depth,
			TileSetProvider tsp, Entity player) {
		// TODO: implement depth.
		Level tr = new Level(width, height, depth);
		int[][] data = new int[width][height];

		DungeonGenerator.generate(width, height, depth, data, tr);

		for (int x = 0; x < width; ++x) {
			if (data[x][0] == FLOOR)
				data[x][0] = WALL;
			if (data[x][height - 1] == FLOOR)
				data[x][height - 1] = WALL;
			for (int y = 0; y < height; ++y) {
				if (data[0][y] == FLOOR)
					data[0][y] = WALL;
				if (data[width - 1][y] == FLOOR)
					data[width - 1][y] = WALL;
			}
		}

		Tile[][] tiles = tr.getTiles();
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				switch (data[x][y]) {
				case BACKGROUND:
					tiles[x][y] = tsp.getBackgroundTile(x, y);
					break;
				case FLOOR:
					tiles[x][y] = tsp.getFloorTile(x, y);
					break;
				case WALL:
					tiles[x][y] = tsp.getWallTile(x, y);
					break;
				case DOOR:
					tiles[x][y] = tsp.getDoorTile(x, y);
					break;
				case STAIR_UP:
					tiles[x][y] = tsp.getStair(x, y, StairDirection.UP);
					break;
				case STAIR_DOWN:
					tiles[x][y] = tsp.getStair(x, y, StairDirection.DOWN);
					break;
				default:
					throw new java.lang.IllegalStateException("Tile at " + x
							+ ", " + y + " has invalid int " + data[x][y]);
				}
			}
		}

		tr.addEntity(player, false);
		PositionComponent pc = (PositionComponent) player.getComponent("position");
		pc.x = tr.getWidth() / 2 - 1;
		pc.y = tr.getHeight() / 2;
		tr.floodFillVisiblity(tr.getWidth() / 2 - 1, tr.getHeight() / 2);

		return tr;
	}
/*
	private static char NORTH = 0x01 << 0;
	private static char SOUTH = 0x01 << 1;
	private static char EAST = 0x01 << 2;
	private static char WEST = 0x01 << 3;
	private static char NORTHEAST = 0x01 << 4;
	private static char NORTHWEST = 0x01 << 5;
	private static char SOUTHEAST = 0x01 << 6;
	private static char SOUTHWEST = 0x01 << 7;

	private static int NE_MASK = NORTH | EAST | NORTHEAST;
	private static int NW_MASK = NORTH | WEST | NORTHWEST;
	private static int SE_MASK = SOUTH | EAST | SOUTHEAST;
	private static int SW_MASK = SOUTH | WEST | SOUTHWEST;

	private static WallSide detectWallSide(int x, int y, int[][] data) {
		int sides = 0x00;
		if ((y + 1 >= data[x].length) || (data[x][y + 1] == BACKGROUND))
			sides |= SOUTH;
		if ((y - 1 < 0) || (data[x][y - 1] == BACKGROUND))
			sides |= NORTH;
		if ((x + 1 >= data.length) || (data[x + 1][y] == BACKGROUND))
			sides |= EAST;
		if ((x - 1 < 0) || (data[x - 1][y] == BACKGROUND))
			sides |= WEST;
		if ((y + 1 >= data[x].length || x + 1 >= data.length)
				|| (data[x + 1][y + 1] == BACKGROUND))
			sides |= SOUTHEAST;
		if ((y + 1 >= data[x].length || x - 1 < 0)
				|| (data[x - 1][y + 1] == BACKGROUND))
			sides |= SOUTHWEST;
		if ((y - 1 < 0 || x + 1 >= data.length)
				|| (data[x + 1][y - 1] == BACKGROUND))
			sides |= NORTHEAST;
		if ((y - 1 < 0 || x - 1 < 0) || (data[x - 1][y - 1] == BACKGROUND))
			sides |= NORTHWEST;

		// Indicies of positions
		// 5 0 4
		// 3 2
		// 7 1 6
		if ((sides & NE_MASK) == NE_MASK)
			return WallSide.NW;
		if ((sides & NW_MASK) == NW_MASK)
			return WallSide.NE;
		if ((sides & SE_MASK) == SE_MASK)
			return WallSide.SW;
		if ((sides & SW_MASK) == SW_MASK)
			return WallSide.SE;
		if ((sides & NORTH) == NORTH)
			return WallSide.N;
		if ((sides & EAST) == EAST)
			return WallSide.E;
		if ((sides & SOUTH) == SOUTH)
			return WallSide.S;
		if ((sides & WEST) == WEST)
			return WallSide.W;
		if (sides == NORTHEAST)
			return WallSide.NEO;
		if (sides == NORTHWEST)
			return WallSide.NWO;
		if (sides == SOUTHEAST)
			return WallSide.SEO;
		if (sides == SOUTHWEST)
			return WallSide.SWO;
		// throw new IllegalArgumentException("Unknown sides state: " +
		// (int)sides);
		return null;
	}*/
}
