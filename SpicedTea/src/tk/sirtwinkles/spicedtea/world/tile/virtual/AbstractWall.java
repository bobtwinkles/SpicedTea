package tk.sirtwinkles.spicedtea.world.tile.virtual;

import tk.sirtwinkles.spicedtea.world.tile.Tile;
import tk.sirtwinkles.spicedtea.world.tile.WallSide;


public class AbstractWall extends Tile {

	private WallSide side;
	private int baseImgX, baseImgY;
	
	public AbstractWall(String id, int baseImgX, int baseImgY, int x, int y) {
		super(id, 0, 0, x, y);
		this.baseImgX = baseImgX; this.baseImgY = baseImgY;
		setImage();
	}
	public int getImageX() {
		return imgX;
	}
	
	private void setImage() {
		imgX = baseImgX;
		imgY = baseImgY;
	}
}