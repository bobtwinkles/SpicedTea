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

	public ImageComponent(RenderingSystem sys, TextureRegion tex) {
		super("image");
		this.tex = tex;
		float v2 = tex.getV();
		float v = tex.getV2();
		tex.setV(v);
		tex.setV2(v2);
		sys.addRenderer(new ImageComponentRenderer(this));
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
	}
	
	public TextureRegion getImage() {
		return tex;
	}
}
