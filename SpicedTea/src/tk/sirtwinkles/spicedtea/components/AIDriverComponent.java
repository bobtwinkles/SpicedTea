package tk.sirtwinkles.spicedtea.components;

import static tk.sirtwinkles.spicedtea.MathUtils.random;
import static tk.sirtwinkles.spicedtea.MathUtils.abs;
import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.Point;

import com.badlogic.gdx.utils.OrderedMap;

public class AIDriverComponent extends Component {

	public AIDriverComponent(OrderedMap<String, Object> config) {
		super(config);
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		// Random movement
		PositionComponent pc = (PositionComponent) owner
				.getComponent("position");
		MoverComponent mc = (MoverComponent) owner.getComponent("mover");
		Level l = play.getWorld().getCurrent();
		if (!l.isVisible(pc.x, pc.y)) {
			if (!mc.hasQueuedMoves()) {
				int rx = random.nextInt(5) - 2 + pc.x;
				int ry = random.nextInt(5) - 2 + pc.y;

				if (rx < 0 || rx >= l.getWidth() || ry < 0
						|| ry >= l.getHeight())
					return;

				// System.out.println(rx + " " + ry);
				if (l.getTile(rx, ry).isPassable(this.owner, l)) {
					Point start = Point.getPoint(pc.x, pc.y);
					Point end = Point.getPoint(rx, ry);
					mc.path(start, end, l);
				}
			}
		} else {
			PositionComponent playerPos = (PositionComponent) play.getPlayer()
					.getComponent("position");
			mc.reset();
			Point start = Point.getPoint(pc.x, pc.y);
			Point end = Point.getPoint(playerPos.x, playerPos.y);
			mc.path(start, end, l);
			if (abs(playerPos.x - pc.x) <= 1 && abs(playerPos.y - pc.y) <= 1) {
				HealthComponent health = (HealthComponent) play.getPlayer()
						.getComponent("health");
				health.health--;
			}
		}
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		// TODO Auto-generated method stub

	}

}
