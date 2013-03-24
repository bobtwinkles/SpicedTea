package tk.sirtwinkles.spicedtea.world;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.world.gen.LevelGenerator;
import tk.sirtwinkles.spicedtea.world.gen.TileSetProviderRedBrick;

public class World {
	private Level current;
	
	public World() {
		this.current = LevelGenerator.create(80, 40, 0, new TileSetProviderRedBrick());
	}
	
	public void update(GameSpicedTea game, PlayingState play) {
		current.update(game, play);
		if (current.isComlete()) {
			current = current.getNextLevel();
		}
	}
	
	public Level getCurrent() {
		return current;
	}
}