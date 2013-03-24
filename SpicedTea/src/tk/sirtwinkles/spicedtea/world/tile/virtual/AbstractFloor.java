package tk.sirtwinkles.spicedtea.world.tile.virtual;

import static tk.sirtwinkles.spicedtea.MathUtils.random;
import tk.sirtwinkles.spicedtea.world.tile.Tile;

public abstract class AbstractFloor extends Tile {

	public AbstractFloor(String id, int baseImgX, int baseImgY, int x, int y) {
		super(id, baseImgX + random.nextInt(4), baseImgY + random.nextInt(4),
				x, y);
	}

}