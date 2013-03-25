package tk.sirtwinkles.spicedtea.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.loaders.StringAssetLoader;

public class AssetLoadState implements GameState {
	private AssetManager manager;
	private boolean doneLoading;
	private float progress;
	
	@Override
	public void onEnterState(GameSpicedTea game) {
		manager = new AssetManager();
		//Load config strings.
		manager.setLoader(String.class, new StringAssetLoader());
		//Queue up assets.
		//TODO: Find a better way to request all this. Config file?
		manager.load("data/terrain.png", Texture.class);
		manager.load("data/monsters.png", Texture.class);
		manager.load("data/config/tilesets/GreyBrick.json", String.class);
		manager.load("data/config/charecters/Player.json", String.class);
		
		Gdx.gl10.glClearColor(0.5f, 0.1f, 0.1f, 1.0f);
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
		//Nothing to do here
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
		return doneLoading;
	}

	@Override
	public void render(GameSpicedTea game) {
		GraphicsContext g = game.getContext();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		//TODO :Draw a progress bar.
	}

	@Override
	public void tick(GameSpicedTea game) {
		doneLoading = manager.update();
		progress = manager.getProgress();
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		return new PlayingState(manager);
	}
}
