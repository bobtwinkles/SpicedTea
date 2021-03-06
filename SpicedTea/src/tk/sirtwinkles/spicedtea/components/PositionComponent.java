package tk.sirtwinkles.spicedtea.components;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.utils.OrderedMap;

public class PositionComponent extends Component {
	public PositionComponent(OrderedMap<String, Object> config) {
		super(config);
		if (config.containsKey("x")) {
			this.x = ((Float)config.get("x")).intValue();
		} else if (config.containsKey("y")) {
			this.y = ((Float)config.get("y")).intValue();
		}
	}

	public int x;
	public int y;
	
	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		//Nothing to do here, we are just data
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		//Nothing to do here, we are just data.
	}

}
