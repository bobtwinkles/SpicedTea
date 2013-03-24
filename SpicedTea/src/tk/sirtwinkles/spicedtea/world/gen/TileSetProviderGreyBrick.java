package tk.sirtwinkles.spicedtea.world.gen;

import tk.sirtwinkles.spicedtea.world.tile.Tile;
import tk.sirtwinkles.spicedtea.world.tile.TileBlack;
import tk.sirtwinkles.spicedtea.world.tile.TileGreyBrickFloor;
import tk.sirtwinkles.spicedtea.world.tile.TileGreyBrickWall;
import tk.sirtwinkles.spicedtea.world.tile.WallSide;

public class TileSetProviderGreyBrick implements TileSetProvider {

	@Override
	public Tile getBackgroundTile(int x, int y) {
		return new TileBlack(x, y);
	}

	@Override
	public Tile getFloorTile(int x, int y) {
		return new TileGreyBrickFloor(x, y);
	}

	@Override
	public Tile getWallTile(int x, int y) {
		return new TileGreyBrickWall(x, y);
	}

	@Override
	public Tile getDoorTile(int x, int y) {
		return new TileGreyBrickFloor(x, y);
	}

}
