package tk.sirtwinkles.spicedtea.components;

import com.badlogic.gdx.utils.OrderedMap;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;

public class AttackComponent extends Component {
	
	public AttackComponent(OrderedMap<String, Object> config) {
		super(config);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		//We don't actually need to do anything here.
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		// TODO Auto-generated method stub
	}

}
