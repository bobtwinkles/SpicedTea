package tk.sirtwinkles.spicedtea.components;

import static tk.sirtwinkles.spicedtea.MathUtils.abs;
import static tk.sirtwinkles.spicedtea.sys.render.LevelRenderer.TILE_SIZE;
import static tk.sirtwinkles.spicedtea.sys.render.RenderingSystem.PXL_SCALE;

import java.util.ListIterator;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.input.InputQueue;
import tk.sirtwinkles.spicedtea.input.KeyEvent;
import tk.sirtwinkles.spicedtea.input.TouchEvent;
import tk.sirtwinkles.spicedtea.state.InventoryViewState;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.combat.Attack;
import tk.sirtwinkles.spicedtea.sys.render.Viewport;
import tk.sirtwinkles.spicedtea.world.Direction;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.Point;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.OrderedMap;

public class PlayerDriverComponent extends Component {
	private boolean handledPress;
	private Point goal;
	private boolean performedActionLastUpdate;

	public PlayerDriverComponent(OrderedMap<String, Object> config) {
		super(config);
		this.goal = Point.getPoint();
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		if (performedActionLastUpdate) {
			performedActionLastUpdate = false;
			// We have already upddated.
			return;
		}
		InputQueue iq = game.getInput();
		// Keyboard
		ListIterator<KeyEvent> keyitr = iq.getKeyEvents().listIterator();
		MoverComponent mc = (MoverComponent) owner.getComponent("mover");
		while (!mc.hasQueuedMoves() && keyitr.hasNext()) {
			KeyEvent evnt = keyitr.next();
			if (evnt.isPressEvent()) {
				switch (evnt.getC()) {
				case Input.Keys.UP:
				case Input.Keys.W:
					attackOrMove(Direction.N, play);
					keyitr.remove();
					break;
				case Input.Keys.DOWN:
				case Input.Keys.S:
					attackOrMove(Direction.S, play);
					keyitr.remove();
					break;
				case Input.Keys.RIGHT:
				case Input.Keys.D:
					attackOrMove(Direction.E, play);
					keyitr.remove();
					break;
				case Input.Keys.LEFT:
				case Input.Keys.A:
					attackOrMove(Direction.W, play);
					keyitr.remove();
					break;
				case Input.Keys.SPACE:
					// Do nothing, but pretend we have.
					performedActionLastUpdate = true;
					break;
				// TODO: Make the inventory system work.
				// case Input.Keys.E:
				// displayInventory(play);
				// break;
				}
			}
		}
		// Touch
		ListIterator<TouchEvent> touchitr = iq.getTouchEvents().listIterator();
		while (touchitr.hasNext()) {
			TouchEvent evnt = touchitr.next();
			if (evnt.getType() == TouchEvent.EventType.PRESSED
					&& evnt.getPointer() == 0 && !handledPress
					&& !mc.hasQueuedMoves()) {
				PositionComponent pc = (PositionComponent) this.owner
						.getComponent("position");
				handledPress = true;
				Viewport view = play.getRenderingSystem().getView();
				Rectangle viewRect = view.getSizeAndPosition();
				final int tbx = (int) (viewRect.x / TILE_SIZE);
				final int tby = (int) (viewRect.y / TILE_SIZE);
				final int tx = (int) (evnt.getX() / TILE_SIZE / PXL_SCALE);
				final int ty = (int) (evnt.getY() / TILE_SIZE / PXL_SCALE);
				Point start = Point.getPoint(pc.x, pc.y);
				Point end = Point.getPoint(tbx + tx, tby + ty);
				goal.x = end.x;
				goal.y = end.y;
				if (abs(goal.x - start.x) + abs(goal.y - start.y) <= 1 && (start.x != goal.x || start.y != goal.y)) {
					Direction dir = null;
					if (goal.x - start.x == 1) {
						dir = Direction.E;
					} else if (goal.x - start.x == -1) {
						dir = Direction.W;
					} else if (goal.y - start.y == 1) {
						dir = Direction.S;
					} else if (goal.y - start.y == -1) {
						dir = Direction.N;
					}
					attackOrMove(dir, play);
				} else {
					mc.path(start, end, play.getWorld().getCurrent());
				}
			}
			if (evnt.getType() == TouchEvent.EventType.RELEASED && handledPress) {
				handledPress = false;
			}
		}
		performedActionLastUpdate |= mc.hasQueuedMoves();
	}

	private void attackOrMove(Direction w, PlayingState play) {
		PositionComponent pc = (PositionComponent) owner
				.getComponent("position");
		int x = pc.x;
		int y = pc.y;
		switch (w) {
		case N:
			--y;
			break;
		case S:
			++y;
			break;
		case E:
			++x;
			break;
		case W:
			--x;
			break;
		}
		Level l = play.getWorld().getCurrent();

		for (Entity ent : l.getEntities()) {
			PositionComponent epos = (PositionComponent) ent
					.getComponent("position");
			if (epos != null) {
				if (epos.x == x && epos.y == y) {
					play.getCombatSystem().queueAttack(
							new Attack(this.owner, ent));
				}
			}
		}

		MoverComponent mc = (MoverComponent) owner.getComponent("mover");
		mc.add(w);
	}

	public Point getGoal() {
		return goal;
	}

	public boolean getPerformedActionLastUpdate() {
		return performedActionLastUpdate;
	}

	public void displayInventory(PlayingState p) {
		p.requestStateChange(new InventoryViewState(p));
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		// Nothing to do here.
	}
}
