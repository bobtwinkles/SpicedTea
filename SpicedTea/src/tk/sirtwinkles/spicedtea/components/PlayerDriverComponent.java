package tk.sirtwinkles.spicedtea.components;

import static tk.sirtwinkles.spicedtea.sys.render.LevelRenderer.TILE_SIZE;
import static tk.sirtwinkles.spicedtea.sys.render.RenderingSystem.PXL_SCALE;

import java.awt.geom.PathIterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.TimeUtils;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.input.InputQueue;
import tk.sirtwinkles.spicedtea.input.KeyEvent;
import tk.sirtwinkles.spicedtea.input.TouchEvent;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.render.Viewport;
import tk.sirtwinkles.spicedtea.world.Direction;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.Point;
import tk.sirtwinkles.spicedtea.world.util.Pather;

public class PlayerDriverComponent extends Component {
	private LinkedList<Point> p;
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
			//We have already upddated.
			return;
		}
		InputQueue iq = game.getInput();
		//Keyboard
		ListIterator<KeyEvent> keyitr = iq.getKeyEvents().listIterator();
		MoverComponent mc = (MoverComponent) owner.getComponent("mover");
		while (!mc.hasQueuedMoves() && keyitr.hasNext()) {
			KeyEvent evnt = keyitr.next();
			if (evnt.isPressEvent()) {
				switch (evnt.getC()) {
				case Input.Keys.UP:
				case Input.Keys.W:
					mc.add(Direction.N);
					keyitr.remove();
					break;
				case Input.Keys.DOWN:
				case Input.Keys.S:
					mc.add(Direction.S);
					keyitr.remove();
					break;
				case Input.Keys.RIGHT:
				case Input.Keys.D:
					mc.add(Direction.E);
					keyitr.remove();
					break;
				case Input.Keys.LEFT:
				case Input.Keys.A:
					mc.add(Direction.W);
					keyitr.remove();
					break;
				}
			}
		}
		//Touch
		ListIterator<TouchEvent> touchitr = iq.getTouchEvents().listIterator();
		while (touchitr.hasNext()) {
			TouchEvent evnt = touchitr.next();
			if (evnt.isPressEvent() && evnt.getPointer() == 0 && !handledPress && ! mc.hasQueuedMoves()) {
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
				goal.x = end.x; goal.y = end.y;
				mc.path(start, end, play.getWorld().getCurrent());
			}
			if (!evnt.isPressEvent() && handledPress) {
				handledPress = false;
			}
		}
		performedActionLastUpdate |= mc.hasQueuedMoves(); 
	}
	
	public Point getGoal() {
		return goal;
	}
	
	public boolean getPerformedActionLastUpdate() {
		return performedActionLastUpdate;
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		//Nothing to do here.
	}

}