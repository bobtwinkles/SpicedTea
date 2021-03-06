package tk.sirtwinkles.spicedtea.world.gen.dungeon;

import static tk.sirtwinkles.spicedtea.MathUtils.abs;
import static tk.sirtwinkles.spicedtea.MathUtils.sign;
import static tk.sirtwinkles.spicedtea.MathUtils.random;
import static tk.sirtwinkles.spicedtea.world.gen.TileSetProvider.FLOOR;

import java.util.LinkedList;

import tk.sirtwinkles.spicedtea.world.Point;
import tk.sirtwinkles.spicedtea.world.gen.dungeon.bsp.SplitDirection;

public class LineUtils {

	private static final int MAX_COUNT = 1024;

	/**
	 * Draws a zigzag line.
	 * @param tr The list to which the path will be appended.
	 * @param sx Start point
	 * @param ex End point
	 */
	public static void drawZigZag(LinkedList<Point> tr, Point sp, Point ep, float zigChance) {
		Point cp = Point.getPoint(sp.x, sp.y);
		Point remain = Point.getPoint();
		Point delta = Point.getPoint();
		int count = 1;

		tr.add(Point.getPoint(cp.x, cp.y));

		while (count < MAX_COUNT && (cp.x != ep.x || cp.y != ep.y)) {
			remain.x = abs(ep.x - cp.x);
			remain.y = abs(ep.y - cp.y);

			if ((count == 1 || random.nextFloat() < zigChance)
					|| (abs(ep.x - (cp.y + delta.x)) > remain.x)
					|| (abs(ep.y - (cp.y + delta.x)) > remain.y)) {
				delta.x = sign(ep.x - cp.x);
				delta.y = sign(ep.y - cp.y);
				
				if (random.nextInt(remain.x + remain.y) < remain.x) {
					if (delta.x != 0) {
						delta.y = 0;
					}
				} else {
					if (delta.y != 0) {
						delta.x = 0;
					}
				}
			}

			cp.x += delta.x;
			cp.y += delta.y;

			tr.addLast(Point.getPoint(cp.x, cp.y));

			++count;
		}

		Point.free(delta);
		Point.free(remain);
		Point.free(cp);
	}

	public static boolean isValidLine(Point sp, Point ep, SplitDirection dir,
			int[][] data) {
		return isValidLinePoint(sp, dir, data)
				&& isValidLinePoint(ep, dir, data);
	}

	private static boolean isValidLinePoint(Point p, SplitDirection dir,
			int[][] data) {
		switch (dir) {
		case VERTICAL:
			return data[p.x][p.y] == FLOOR 
					&& p.x + 1 < data.length && data[p.x][p.y] == FLOOR
					&& p.x - 1 >= 0 && data[p.x][p.y] == FLOOR;
		case HORIZONTAL:
			return data[p.x][p.y] == FLOOR
					&& p.y + 1 < data[p.x].length && data[p.x][p.y + 1] == FLOOR
					&& p.y - 1 < data[p.x].length && data[p.x][p.y - 1] == FLOOR;
		}
		return false;
	}
}
