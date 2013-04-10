package tk.sirtwinkles.spicedtea.state;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.components.PlayerDriverComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.components.RenderComponent;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.entities.EntityFactory;
import tk.sirtwinkles.spicedtea.sys.render.RenderingSystem;
import tk.sirtwinkles.spicedtea.sys.render.Viewport;
import tk.sirtwinkles.spicedtea.world.World;
import tk.sirtwinkles.spicedtea.world.gen.JSONTileSetProvider;
import tk.sirtwinkles.spicedtea.world.gen.LevelGenerator;
import tk.sirtwinkles.spicedtea.world.gen.TileSetProvider;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class PlayingState implements GameState {
	private RenderingSystem render;
	private World world;
	private long lastMoveTime;
	private PlayerDriverComponent player;

	public PlayingState() {
	}

	@Override
	public void onEnterState(GameSpicedTea game) {
		Entity ent = EntityFactory.buildEntity("Player");
		player = (PlayerDriverComponent) ent.getComponent("player.driver");
		world = new World(ent);
		render = new RenderingSystem(new Viewport(new Rectangle()), this);
		// Build player
		PositionComponent pc = (PositionComponent) ent.getComponent("position");
		render.setCenter(pc);
		world.update(game, this);
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
		long ctime = TimeUtils.nanoTime();
		if (ctime - lastMoveTime > 125000000l) {
			lastMoveTime = ctime;
			player.update(game, this);
			if (player.getPerformedActionLastUpdate()) {
				world.update(game, this);
				if (world.getCurrent().isComlete()) {
					TileSetProvider prov = new JSONTileSetProvider(
							(String) Globals.assets
									.get("data/config/tilesets/GreyBrick.json"));
					Entity player = world.getCurrent().getEntity("player");
					world.getCurrent().removeEntity(player);
					for (Entity ent : world.getCurrent().getEntities()) {
						ent.destroy(game, this);
					}

					world.setCurrent(LevelGenerator.create(80, 40, 0, prov,
							player));
					// player.update(game, this);
				}
			}
			// Remove any input events that were not processed.
			game.getInput().clearQueues();
		}
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		return null;
	}

	public World getWorld() {
		return world;
	}

	public RenderingSystem getRenderingSystem() {
		return this.render;
	}
}
