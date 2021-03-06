package tk.sirtwinkles.spicedtea.components;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * A texture component.
 * @author bob_twinkles
 *
 */
public class ImageComponent extends Component {
	private TextureRegion tex;

	public ImageComponent(OrderedMap<String, Object> config) {
		super(config);
		int imx = ((Float) config.get("x")).intValue();
		int imy = ((Float) config.get("y")).intValue();
		imx *= 8;
		imy *= 8;
		Texture monst = Globals.assets.get("data/monsters.png");
		this.tex = new TextureRegion(monst, imx, imy, 8, 8);
		float v2 = tex.getV();
		float v = tex.getV2();
		tex.setV(v);
		tex.setV2(v2);
	}

	@Override
	public void update(GameSpicedTea game, PlayingState play) {
	}
	
	public TextureRegion getImage() {
		return tex;
	}

	@Override
	public void destroy(GameSpicedTea game, PlayingState play) {
		// Nothing to do here.
	}
}
