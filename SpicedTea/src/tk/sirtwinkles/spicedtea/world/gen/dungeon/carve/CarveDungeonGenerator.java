package tk.sirtwinkles.spicedtea.world.gen.dungeon.carve;

import static tk.sirtwinkles.spicedtea.MathUtils.random;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.*;
import java.util.ArrayList;

import tk.sirtwinkles.spicedtea.world.gen.dungeon.Direction;

public class CarveDungeonGenerator {
	private static ArrayList<Feature> FEATURES;

	static {
		// TODO: load this from JSON
		FEATURES = new ArrayList<Feature>();
		//FEATURES.add(new FeatureCorridor());
		FEATURES.add(new FeatureRoom());
	}

	public static void generate(int width, int height, int depth, int[][] data) {
		genCenterRoom(width, height, data);
		// Try to generate features.
		int rx, ry;
		for (int i = 0; i < 4 * width * height; ++i) {
			rx = 1 + random.nextInt(width - 2);
			ry = 1 + random.nextInt(height - 2);
			if (data[rx][ry] == WALL) {
				if (data[rx - 1][ry] == FLOOR) {
					genRandomFeatures(rx, ry, Direction.E, data);
				} else if (data[rx + 1][ry] == FLOOR) {
					genRandomFeatures(rx, ry, Direction.W, data);
				} else if (data[rx][ry + 1] == FLOOR) {
					genRandomFeatures(rx, ry, Direction.N, data);
				} else if (data[rx][ry - 1] == FLOOR) {
					genRandomFeatures(rx, ry, Direction.S, data);
				}
			}
		}
		//Cleanup.
		for (int x = 1; x < width - 1; ++x) {
			for (int y = 1; y < height - 1; ++y) {
				if (data[x][y] == FLOOR) {
					xxLoop: for (int xx = -1; xx <= 1; ++xx) {
						for (int yy = -1; yy <= 1; ++yy) {
							if (data[x + xx][y + yy] == BACKGROUND) {
								data[x][y] = WALL;
								break xxLoop;
							}
						}
					}
				}
			}
		}
	}

	private static void genRandomFeatures(int rx, int ry, Direction dir,
			int[][] data) {
		int featureIndex = random.nextInt(FEATURES.size());
		//if (dir == Direction.W)
		if (FEATURES.get(featureIndex).generate(rx, ry, data, dir)) {
		}
	}

	private static void genCenterRoom(int width, int height, int[][] data) {
		// Random constants 4 days
		int w = width / 5;
		int h = height / 5;
		int sx = width / 2 - w / 2;
		int sy = height / 2 - h / 2;
		for (int x = sx; x < sx + w; ++x) {
			for (int y = sy; y < sy + h; ++y) {
				if (x == sx || x == sx + w - 1 || y == sy || y == sy + h - 1) {
					data[x][y] = WALL;
				} else {
					data[x][y] = FLOOR;
				}
			}
		}
		
		data[width / 2][height / 2] = STAIR_UP;
	}
}
