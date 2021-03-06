package tk.sirtwinkles.spicedtea.sys.render;

import static tk.sirtwinkles.spicedtea.sys.render.LevelRenderer.TILE_SIZE;
import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.components.HealthComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class HealthBarRenderer implements Renderer {
	private HealthComponent comp;
	private Texture gui;
	
	public HealthBarRenderer() {
		gui = Globals.assets.get("data/gui.png");
	}
	
	public void setComponent(HealthComponent comp) {
		this.comp = comp;
	}
	
	public void render(GraphicsContext context, PlayingState state, Viewport view) {
		Rectangle viewRect = view.getSizeAndPosition();
		final int tx = (int) (viewRect.x / TILE_SIZE);
		final int ty = (int) (viewRect.y / TILE_SIZE);
		PositionComponent pos = (PositionComponent) comp.getOwner().getComponent("position");
		SpriteBatch sb = context.getBatch();
		if (state.getWorld().getCurrent().isVisible(pos.x, pos.y)) {
			final int baseX = (pos.x - tx) * TILE_SIZE;
			final int baseY = (pos.y - ty) * TILE_SIZE;
			final int healthPerTile = comp.maxHealth / 2;
			final int remainingHealth = comp.getHealth() % healthPerTile;
			final int remainingPixels = (int)(8.0 * remainingHealth / healthPerTile);
			//DRaw bar
			drawTile(sb, baseX - 4, baseY - 4, 2, 7);
			drawTile(sb, baseX + 4, baseY - 4, 3, 8);
			drawTile(sb, baseX + 0, baseY - 4, 3, 8);
			drawTile(sb, baseX + 8, baseY - 4, 4, 7);
			//Overdraw health
			if (comp.getHealth() >= comp.maxHealth / 2) {
				drawTile(sb, baseX + 0, baseY - 4, 3, 7);
				if (comp.getHealth() == comp.maxHealth) {
					drawTile(sb, baseX + 4, baseY - 4, 3, 7);
				} else {
					sb.draw(gui, baseX + 4, baseY - 4, remainingPixels / 2, 4, 24, 56, remainingPixels, 8, false, true);
				}
			} else {
				sb.draw(gui, baseX, baseY - 4, remainingPixels / 2, 4, 24, 56, remainingPixels, 8, false, true);
			}
		}
	}

	private void drawTile(SpriteBatch batch, int x, int y, int tx, int ty) {
		batch.draw(gui, x, y, Globals.GUI_TILE_SIZE / 2, Globals.GUI_TILE_SIZE / 2, tx
				* Globals.GUI_TILE_SIZE, ty * Globals.GUI_TILE_SIZE,
				Globals.GUI_TILE_SIZE, Globals.GUI_TILE_SIZE, false, true);
	}
}