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
	// Should we regenerate the world?
	private boolean generateWorld;
	private int currentLevel;
	//Next state.
	private GameState nextState;
	//Should we switch?
	private boolean changeState;

	public PlayingState() {
		this.generateWorld = true;
	}

	@Override
	public void onEnterState(GameSpicedTea game) {
		Globals.ps = this;
		if (generateWorld) {
			Entity ent = EntityFactory.buildEntity("Player");
			player = (PlayerDriverComponent) ent.getComponent("player.driver");
			world = new World(ent);
			render = new RenderingSystem(new Viewport(new Rectangle()), this);
			// Build player
			PositionComponent pc = (PositionComponent) ent
					.getComponent("position");
			render.setCenter(pc);
			world.update(game, this);
			generateWorld = false;
		}
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
		changeState = false;
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
		return changeState;
	}

	@Override
	public void render(GameSpicedTea game) {
		render.run(game, this);
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
					TileSetProvider prov = getNextTileset();
					Entity player = world.getCurrent().getEntity("player");
					world.getCurrent().removeEntity(player);
					for (Entity ent : world.getCurrent().getEntities()) {
						ent.destroy(game, this);
					}

					world.setCurrent(LevelGenerator.create(80, 40, ++currentLevel, prov,
							player));
				}
			}
			// Remove any input events that were not processed.
			game.getInput().clearQueues();
		}
	}

	private TileSetProvider getNextTileset() {
		String tsName = "RedBrick.json";
		switch(currentLevel) {
		case 0: tsName = "GreyBrick.json"; break;
		default: System.out.println("Warning: no tileset specified for level: " + (currentLevel + 1));break;
		}
		
		
		return new JSONTileSetProvider((String) Globals.assets.get("data/config/tilesets/" + tsName));
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		GameState t = nextState;
		nextState = null;
		return t;
	}

	public World getWorld() {
		return world;
	}

	public RenderingSystem getRenderingSystem() {
		return this.render;
	}
	
	public void requestStateChange(GameState to) {
		this.nextState = to;
		this.changeState = true;
	}
	
	public Entity getPlayer() {
		return player.getOwner();
	}
}
