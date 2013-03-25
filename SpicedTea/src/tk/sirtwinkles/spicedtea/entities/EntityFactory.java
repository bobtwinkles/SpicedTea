package tk.sirtwinkles.spicedtea.entities;

import java.util.HashMap;

import tk.sirtwinkles.spicedtea.components.Component;
import tk.sirtwinkles.spicedtea.components.ImageComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.sys.render.RenderingSystem;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap.Entry;
import com.badlogic.gdx.utils.OrderedMap;

public final class EntityFactory {
	
	@SuppressWarnings("unchecked")
	public static Entity buildEntity(String jsonConfig, AssetManager manager, RenderingSystem ren) {
		JsonReader reader = new JsonReader();
		OrderedMap<String, Object> root = (OrderedMap<String, Object>) reader.parse(jsonConfig);
		Entity tr = new Entity((String) root.get("name"));
		Array<Object> comps = (Array<Object>) root.get("components");
		
		for (Object c : comps) {
			OrderedMap<String, Object> component = (OrderedMap<String, Object>) c;
			String s = ((String) component.get("type")).toLowerCase();
			if (s.equals("image")) {
				tr.addComponent(buildImageComponent(manager, ren, component));
			} else if (s.equals("position")) {
				tr.addComponent(new PositionComponent());
			}
		}
		
		return tr;
	}

	private static Component buildImageComponent(AssetManager manager,
			RenderingSystem ren, OrderedMap<String, Object> config) {
		int imx = ((Float) config.get("x")).intValue();
		int imy = ((Float) config.get("y")).intValue();
		imx *= 8;
		imy *= 8;
		Texture monst = manager.get("data/monsters.png");
		ImageComponent comp = new ImageComponent(ren, new TextureRegion(monst, imx, imy, 8, 8));
		return comp;
	}

	/**
	 * This class should never be instantiated.
	 */
	private EntityFactory() {}
}
