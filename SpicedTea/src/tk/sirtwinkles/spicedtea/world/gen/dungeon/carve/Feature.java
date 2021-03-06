package tk.sirtwinkles.spicedtea.world.gen.dungeon.carve;

import tk.sirtwinkles.spicedtea.world.Direction;

public abstract class Feature {
	public abstract boolean generate(int x, int y, int[][] data, Direction dir);

	public int test(int x, int y, int w, int h, int[][] data, int expected) {
		int numbad = 0;
		for (int xx = x; xx < x + w; ++xx) {
			for (int yy = y; yy < y + h; ++yy) {
				if (xx < 0 || xx >= data.length || yy < 0
						|| yy >= data[xx].length || data[xx][yy] != expected) {
					++numbad;
				}
			}
		}
		return numbad;
	}

	public void fill(int x, int y, int w, int h, int[][] data, int to) {
		for (int xx = x; xx < x + w; ++xx) {
			for (int yy = y; yy < y + h; ++yy) {
				if (!(xx < 0 || xx >= data.length || yy < 0 || yy >= data[xx].length)) {
					data[xx][yy] = to;
				}
			}
		}
	}
}
