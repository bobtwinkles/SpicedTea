package tk.sirtwinkles.spicedtea.sys.render;

import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.components.HealthComponent;
import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HealthRenderer implements Renderer {
	private static final int HEIGHT = 4;
	private HealthComponent comp;
	private Texture gui;

	public HealthRenderer() {
		gui = Globals.assets.get("data/gui.png");
	}

	public void setComponent(HealthComponent comp) {
		this.comp = comp;
	}

	public void render(GraphicsContext context, PlayingState state,
			Viewport view) {
		SpriteBatch sb = context.getBatch();
		final int xbase = 0;
		final int ybase = 4;
		final int height = Globals.GUI_TILE_SIZE * HEIGHT;
		drawTile(sb, xbase, ybase - Globals.GUI_TILE_SIZE, 2, 4);
		int missingPixels = (int)((comp.getHealth() * height / (float)(comp.maxHealth)));
		final int pixels = missingPixels;
		for (int y = 0; y < height; y += Globals.GUI_TILE_SIZE) {
			missingPixels -= 8;
			if (missingPixels >= 0) {
				drawTile(sb, xbase, ybase + y, 2, 5);
			} else {
				drawTile(sb, xbase, ybase + y, 4, 5);
			}
		}
		if (comp.getHealth() > 0) {
			sb.draw(gui, xbase, ybase + (pixels / 8 * 8), Globals.GUI_TILE_SIZE,
				pixels % 8, 16, 40, 8, pixels % 8,
				false, true);
		}
		if (comp.getHealth() == comp.maxHealth) {
			drawTile(sb, xbase, ybase + height, 2, 6);
		} else {
			drawTile(sb, xbase, ybase + height, 4, 6);
		}
	}

	private void drawTile(SpriteBatch batch, int x, int y, int tx, int ty) {
		batch.draw(gui, x, y, Globals.GUI_TILE_SIZE, Globals.GUI_TILE_SIZE, tx
				* Globals.GUI_TILE_SIZE, ty * Globals.GUI_TILE_SIZE,
				Globals.GUI_TILE_SIZE, Globals.GUI_TILE_SIZE, false, true);
	}
}