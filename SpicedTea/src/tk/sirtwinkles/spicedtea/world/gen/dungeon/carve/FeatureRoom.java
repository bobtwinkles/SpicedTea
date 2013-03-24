package tk.sirtwinkles.spicedtea.world.gen.dungeon.carve;

import static tk.sirtwinkles.spicedtea.MathUtils.random;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.BACKGROUND;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.DOOR;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.FLOOR;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.WALL;
import tk.sirtwinkles.spicedtea.world.gen.dungeon.Direction;

public class FeatureRoom extends Feature {

	private static final int MIN_SIZE = 4;
	private static final int MAX_SIZE = 10;

	@Override
	public boolean generate(int x, int y, int[][] data, Direction dir) {
		int rw = random.nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE;
		int rh = random.nextInt(MAX_SIZE - MIN_SIZE) + MIN_SIZE;
		int rx, ry;
		rx = ry = 0;
		switch (dir) {
		case N:
			rx = x - rw / 2;
			ry = y - rh + 1;
			break;
		case E:
			rx = x;
			ry = y - rh / 2;
			break;
		case S:
			rx = x - rw / 2;
			ry = y;
			break;
		case W:
			rx = x - rw + 1;
			ry = y - rh / 2;
			break;
		}
		if (rx < 0 || rx >= data.length || ry < 0 || ry >= data[0].length ||
				rx+rw >= data.length || ry+rh >= data[0].length) {
			return false;
		}
		int notBlank = test(rx, ry, rw, rh, data, BACKGROUND);
		int notWall = test(rx, ry, rh, rh, data, WALL);
		int testres = (rw * rh) - notBlank - notWall;
		if (testres == 0) {
			for (int xx = rx; xx < rx + rw; ++xx) {
				for (int yy = ry; yy < ry + rh; ++yy) {
					if (xx == rx || xx == rx + rw - 1 || yy == ry
							|| yy == ry + rh - 1) {
						data[xx][yy] = WALL;
					} else {
						data[xx][yy] = FLOOR;
					}
				}
			}
			data[x][y] = DOOR;
			return true;
		}
		return false;
	}
}
