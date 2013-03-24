package tk.sirtwinkles.spicedtea.components;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.render.ImageComponentRenderer;
import tk.sirtwinkles.spicedtea.sys.render.RenderingSystem;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A texture component.
 * @author bob_twinkles
 *
 */
public class ImageComponent extends Component {
	private TextureRegion tex;

	public ImageComponent(String id, RenderingSystem sys, TextureRegion tex) {
		super(id);
		this.tex = tex;
		sys.addRenderer(new ImageComponentRenderer(this));
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
		if (this.owner != null) {
			
		}
	}
	
	public TextureRegion getImage() {
		return tex;
	}
}