package tk.sirtwinkles.spicedtea.state;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.components.InventoryComponent;
import tk.sirtwinkles.spicedtea.sys.render.InventoryRenderer;

public class InventoryViewState implements GameState {
	private PlayingState parent;
	private InventoryRenderer renderer;

	public InventoryViewState(PlayingState parent) {
		this.parent = parent;
		// Gdx.graphics.setContinuousRendering(false);
	}

	@Override
	public void onEnterState(GameSpicedTea game) {
		this.renderer = new InventoryRenderer();
		this.renderer.setInventory((InventoryComponent) parent.getPlayer()
				.getComponent("inventory"));
		parent.getRenderingSystem().addRenderer(renderer);
	}

	@Override
	public void onLeaveState(GameSpicedTea game) {
		parent.getRenderingSystem().removeRenderer(renderer);
	}

	@Override
	public boolean switchState(GameSpicedTea game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void render(GameSpicedTea game) {
		parent.render(game);
	}

	@Override
	public void tick(GameSpicedTea game) {
	}

	@Override
	public GameState getNextState(GameSpicedTea game) {
		// TODO Auto-generated method stub
		return null;
	}

}
