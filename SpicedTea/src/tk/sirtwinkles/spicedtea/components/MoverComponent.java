package tk.sirtwinkles.spicedtea.components;

import java.util.LinkedList;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.world.Direction;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.Point;
import tk.sirtwinkles.spicedtea.world.util.Pather;

import com.badlogic.gdx.utils.OrderedMap;

/**
 * Provides a common interface for moving entites
 * 
 * @author bob_twinkles
 * 
 */
public class MoverComponent extends Component {
	private LinkedList<Direction> movements;
	private LinkedList<Point> p;
	private Direction lastDirection;

	public MoverComponent(OrderedMap<String, Object> config) {
		super(config);
		this.movements = new LinkedList<Direction>();
		this.lastDirection = Direction.E;
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		PositionComponent pc = (PositionComponent) this.owner.getComponent("position");
		// Movement processing.
		if (!movements.isEmpty()) {
			Direction d = movements.remove();
			lastDirection = d;
			switch (d) {
			case N:
				if (!tryMove(pc.x, pc.y - 1, play.getWorld().getCurrent())) {
					movements.clear();
				}
				break;
			case E:
				if (!tryMove(pc.x + 1, pc.y, play.getWorld().getCurrent())) {
					movements.clear();
				}
				break;
			case S:
				if (!tryMove(pc.x, pc.y + 1, play.getWorld().getCurrent())) {
					movements.clear();
				}
				break;
			case W:
				if (!tryMove(pc.x - 1, pc.y, play.getWorld().getCurrent())) {
					movements.clear();
				}
				break;
			}
		}
	}

	private boolean tryMove(int x, int y, Level in) {
		if (in.getTile(x, y).isPassable(owner, in)) {
			for (Entity ent : in.getEntities()) {
				PositionComponent pc = (PositionComponent) ent.getComponent("position");
				if (x == pc.x && y == pc.y) {
					return false;
				}
			}
			PositionComponent pc = (PositionComponent) owner
					.getComponent("position");
			pc.x = x;
			pc.y = y;
			in.onEntityMove(x, y, this.owner);
			return true;
		}

		return false;
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		// Nothing to do here.
	}
	
	public Direction getLastDirection() {
		return lastDirection;
	}

	public void add(Direction n) {
		movements.add(n);
	}
	
	/**
	 * Attempts to enqueue moves that will move to <code>end</code>.
	 * Movement is on a best effort basis, you are not guaranteed to 
	 * arrive at the destination.
	 * @param start Start point, freed.
	 * @param end End point, freed.
	 * @param in Level to path in.
	 */
	public void path(Point start, Point end, Level in) {
		if (hasQueuedMoves()) {
			throw new IllegalStateException("Can't path with queued moves.");
		}
		p = Pather.path(in, p, start, end, this.owner);
		if (p != null) {
			p.addFirst(start);
			while (!p.isEmpty()) {
				Point curr = p.remove();
				Point next = p.peek();
				if (next != null) {
					int xDelta = curr.x - next.x;
					int yDelta = curr.y - next.y;
					if (xDelta == -1) {
						add(Direction.E);
					} else if (xDelta == 1) {
						add(Direction.W);
					} else if (yDelta == -1) {
						add(Direction.S);
					} else if (yDelta == 1) {
						add(Direction.N);
					}
				}
				Point.free(curr);
			}
		} else {
			Point.free(start);
		}
		Point.free(end);
	}

	public void reset() {
		movements.clear();
	}

	public boolean hasQueuedMoves() {
		return !movements.isEmpty();
	}
}
