package tk.sirtwinkles.spicedtea.state;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.sys.render.RenderingSystem;
import tk.sirtwinkles.spicedtea.sys.render.Viewport;
import tk.sirtwinkles.spicedtea.world.World;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;

public class PlayingState implements GameState {
	private RenderingSystem render;
	private World world;
	private AssetManager manager;
	
	public PlayingState(AssetManager manager) {
		this.manager = manager;
	}

	@Override
	public void onEnterState(GameSpicedTea game) {
		world = new World();
		render = new RenderingSystem(new Viewport(new Rectangle()), this);
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
		//TODO Auto-generated method stub.
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(GameSpicedTea game) {
		render.run(game);
	}

	@Override
	public void tick(GameSpicedTea game) {
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public AssetManager getAssets() {
		return manager;
	}
	
	public World getWorld() {
		return world;
	}
}