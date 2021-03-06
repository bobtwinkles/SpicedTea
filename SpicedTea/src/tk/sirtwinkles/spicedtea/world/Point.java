package tk.sirtwinkles.spicedtea.world;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Point implements Poolable {

	public int x, y;

	private Point() {
		x = 0;
		y = 0;
	}

	private Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void reset() {
		// Nothing to do here.
	}
	
	@Override
	public String toString() {
		return "Point: " + x + " " + y;
	}

	/**
	 * Static pool stuff.
	 */
	private static Pool<Point> points;

	static {
		points = new Pool<Point>() {
			@Override
			protected Point newObject() {
				return new Point();
			}
		};
	}

	public static Point getPoint() {
		return points.obtain();
	}

	public static Point getPoint(int x, int y) {
		Point tr = points.obtain();
		tr.x = x;
		tr.y = y;
		return tr;
	}

	public static void free(Point p) {
		points.free(p);
	}
}
