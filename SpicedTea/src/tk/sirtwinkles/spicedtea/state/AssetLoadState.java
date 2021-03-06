package tk.sirtwinkles.spicedtea.state;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.entities.EntityFactory;
import tk.sirtwinkles.spicedtea.loaders.StringAssetLoader;
import tk.sirtwinkles.spicedtea.world.gen.dungeon.carve.room.RoomFeatureFactory;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;

public class AssetLoadState implements GameState {
	private boolean doneLoading;
	
	//TODO:Implement a progress bar renderer
	//private float progress;

	@Override
	public void onEnterState(GameSpicedTea game) {
		AssetManager manager = Globals.assets;
		// Load config strings.
		manager.setLoader(String.class, new StringAssetLoader());
		// Queue up assets.
		// TODO: Find a better way to request all this. Config file?
		System.out.println(Gdx.files.internal("data/config/tilesets/GreyBrick.json").file().getAbsolutePath());
		manager.load("data/terrain.png", Texture.class);
		manager.load("data/monsters.png", Texture.class);
		manager.load("data/gui.png", Texture.class);
		loadAllInDirectory("data/config/tilesets/", "json", String.class);
		loadAllInDirectory("data/config/component/", "json", String.class);
		loadAllInDirectory("data/config/characters/", "json", String.class);
		loadAllInDirectory("data/config/rooms/", "json", String.class);

		Gdx.gl10.glClearColor(0.5f, 0.1f, 0.1f, 1.0f);
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
		// Nothing to do here
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
		return doneLoading;
	}

	@Override
	public void render(GameSpicedTea game) {
		// TODO :Draw a progress bar.
		//GraphicsContext g = game.getContext();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		Gdx.graphics.requestRendering();
	}

	@Override
	public void tick(GameSpicedTea game) {
		doneLoading = Globals.assets.update();
		//TODO:Implement a progress bar
		//progress = Globals.assets.getProgress();
		if (doneLoading) {
			try {
				EntityFactory.parseCompoents();
				RoomFeatureFactory.buildTemplates();
			} catch (ClassNotFoundException e) {
				System.err.println("Unknown class:");
				e.printStackTrace();
				Gdx.app.exit();
			}
		}
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		return new TextDisplayState("intro", new PlayingState());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadAllInDirectory(String basePath, String extension,
			Class type) {
		String loadPath = basePath;
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			loadPath = "./bin/" + basePath;
		}
		for (FileHandle f : Gdx.files.internal(loadPath).list()) {
			if (f.extension().equalsIgnoreCase(extension)) {
				Globals.assets.load(basePath + f.name(), type);
			}
		}
	}
}
