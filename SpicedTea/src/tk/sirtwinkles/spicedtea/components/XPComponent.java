package tk.sirtwinkles.spicedtea.components;

import com.badlogic.gdx.utils.OrderedMap;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.state.TextDisplayState;

public class XPComponent extends Component {
	public static final int MAX_LEVEL = 30;
	public int xp;
	public int nextLevelXP;
	public int level;
	private boolean firstLevel;
	
	public XPComponent(OrderedMap<String, Object> config) {
		super(config);
		level = 1;
		nextLevelXP = 4;
		firstLevel = true;
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		if (xp >= nextLevelXP && level < MAX_LEVEL) {
			++level;
			xp -= nextLevelXP;
			nextLevelXP = (level * level * level);
			
			if (owner.getComponent("health") != null) {
				HealthComponent hc = (HealthComponent) owner.getComponent("health");
				hc.maxHealth += level;
			}
			
			if (firstLevel && owner.getID().equalsIgnoreCase("player")) {
				play.requestStateChange(new TextDisplayState("first_level_up", play));
				firstLevel = false;
			}
		}
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		//Nothing to do here.
	}

}
