package tk.sirtwinkles.spicedtea.components;

import com.badlogic.gdx.utils.OrderedMap;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.render.Renderer;
import tk.sirtwinkles.spicedtea.sys.render.RenderingSystem;

public abstract class RenderComponent extends Component {
	protected Renderer ren;
	protected boolean registered;
	
	public RenderComponent(Renderer ren, OrderedMap<String, Object> config) {
		super(config);
		this.ren = ren;
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		if (!this.registered) {
			registerWithRenderer(play.getRenderingSystem());
		}
	}
	
	public void registerWithRenderer(RenderingSystem sys) {
		sys.addRenderer(ren);
		this.registered = true;
	}
	
	public void unregisterRenderer(RenderingSystem sys) {
		sys.removeRenderer(ren);
		this.registered = false;
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		this.unregisterRenderer(play.getRenderingSystem());
		this.registered = false;
	}
}
