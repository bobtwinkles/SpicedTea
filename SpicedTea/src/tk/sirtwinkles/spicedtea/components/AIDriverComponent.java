package tk.sirtwinkles.spicedtea.components;

import static tk.sirtwinkles.spicedtea.MathUtils.abs;
import static tk.sirtwinkles.spicedtea.MathUtils.random;
import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.combat.Attack;
import tk.sirtwinkles.spicedtea.world.Level;
import tk.sirtwinkles.spicedtea.world.Point;

import com.badlogic.gdx.utils.OrderedMap;

public class AIDriverComponent extends Component {
	private int level;
	
	public AIDriverComponent(OrderedMap<String, Object> config) {
		super(config);
		level = ((Float)config.get("level")).intValue();
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		((XPComponent)owner.getComponent("xp")).level = level;
		((XPComponent)owner.getComponent("xp")).nextLevelXP = Integer.MAX_VALUE; //We don't ever want to level enimies.
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
				play.getCombatSystem().queueAttack(new Attack(this.owner, play.getPlayer()));
			}
		}
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		// TODO Auto-generated method stub

	}

}
