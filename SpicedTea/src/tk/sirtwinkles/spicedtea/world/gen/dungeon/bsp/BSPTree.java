package tk.sirtwinkles.spicedtea.world.gen.dungeon.bsp;

import static tk.sirtwinkles.spicedtea.MathUtils.random;

public class BSPTree {
	private static final double DRAW_CHANCE = 1.1;
	public BSPTree parent, left, right;
	public int x, y, width, height;
	public SplitDirection dir;
	public boolean draw;

	private BSPTree(int width, int height) {
		this.parent = null;
		this.width = width;
		this.height = height;
		this.x = 0;
		this.y = 0;
	}

	private BSPTree(int x, int y, int width, int height, BSPTree parent) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = parent;
		this.draw = random.nextFloat() < DRAW_CHANCE;
	}

	public static BSPTree buildTree(int width, int height, int minSize) {
		BSPTree root = new BSPTree(width, height);
		buildTree(root, minSize);
		return root;
	}

	private static void buildTree(BSPTree parent, int minSize) {
		if (random.nextFloat() < 0.5) {
			if (!horiSplit(parent, minSize)) {
				vertSplit(parent, minSize);
			}
		} else {
			if (!vertSplit(parent, minSize)) {
				horiSplit(parent, minSize);
			}
		}
	}
	
	private static final int MAX_TRIES = 20;

	private static boolean horiSplit(BSPTree parent, int minSize) {
		int off = 0;
		int tries = 0;
		do {
			off = random.nextInt(parent.height);
			++tries;
		} while ((off <= minSize || parent.height - off <= minSize) && tries < MAX_TRIES);
		if (tries == MAX_TRIES) {
			return false;
		}
		BSPTree right = new BSPTree(parent.x, parent.y, parent.width, off,
				parent);
		BSPTree left = new BSPTree(parent.x, parent.y + off, parent.width,
				parent.height - off, parent);
		parent.right = right;
		parent.left = left;
		parent.draw = right.draw || left.draw;
		buildTree(right, minSize);
		buildTree(left, minSize);
		parent.dir = SplitDirection.HORIZONTAL;
		
		return true;
	}

	private static boolean vertSplit(BSPTree parent, int minSize) {
		int tries = 0;
		int off = 0;
		do {
			off = random.nextInt(parent.width);
			++tries;
		} while  ((off <= minSize || parent.width - off <= minSize) && tries < MAX_TRIES);
		
		if (tries == MAX_TRIES) {
			return false;
		}
		BSPTree right = new BSPTree(parent.x, parent.y, off, parent.height,
				parent);
		BSPTree left = new BSPTree(parent.x + off, parent.y,
				parent.width - off, parent.height, parent);
		parent.right = right;
		parent.left = left;
		parent.draw = right.draw || left.draw;
		buildTree(right, minSize);
		buildTree(left, minSize);
		parent.dir = SplitDirection.VERTICAL;
		
		return false;
	}
}
