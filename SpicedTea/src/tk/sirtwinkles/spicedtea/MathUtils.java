package tk.sirtwinkles.spicedtea;

import java.util.Random;

public final class MathUtils {
	public static Random random = new Random();

	public static int sign(int x) {
		if (x < 0) {
			return -1;
		} else if (x == 0) {
			return 0;
		} else {
			return 1;
		}
	}

	public static int abs(int x) {
		if (x < 0) {
			return -x;
		}
		return x;
	}
	
	private MathUtils() {
		/*
		 * We don't want to do anything, this class should never be
		 * instantiated.
		 */
	}
}
