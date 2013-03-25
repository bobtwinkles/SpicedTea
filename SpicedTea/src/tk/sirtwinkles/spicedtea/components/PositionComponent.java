package tk.sirtwinkles.spicedtea.components;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;

public class PositionComponent extends Component {
	public PositionComponent() {
		super("position");
	}

	public int x;
	public int y;
	
	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		//Nothing to do here, we are just data
	}

}
