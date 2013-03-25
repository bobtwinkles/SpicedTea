package tk.sirtwinkles.spicedtea.state;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.entities.EntityFactory;
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
		world = new World(manager);
		render = new RenderingSystem(new Viewport(new Rectangle()), this);
		// Build player
		Entity ent = EntityFactory.buildEntity(
				(String) manager.get("data/config/charecters/Player.json"),
				manager, render);
		world.getCurrent().addEntity(ent);
		PositionComponent pc = (PositionComponent) ent.getComponent("position");
		pc.x = world.getCurrent().getWidth() / 2;
		pc.y = world.getCurrent().getHeight() / 2;
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
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
		return null;
	}

	public AssetManager getAssets() {
		return manager;
	}

	public World getWorld() {
		return world;
	}
}
