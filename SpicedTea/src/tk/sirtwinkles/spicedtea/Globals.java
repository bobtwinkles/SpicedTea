package tk.sirtwinkles.spicedtea;

import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.JsonReader;

/**
 * Evil but useful.
 * @author bob_twinkles
 *
 */
public final class Globals {
	public static AssetManager assets = new AssetManager(new InternalFileHandleResolver());
	public static JsonReader json = new JsonReader();
	//XXX: This is bad and I should feel bad...
	public static GameSpicedTea game;
	//XXX: This is bad and I should feel bad...
	public static PlayingState ps;

	public static final int GUI_DRAW_SIZE = 64;
	public static final int GUI_TILE_SIZE = 8;
	
	private Globals() {}
}
