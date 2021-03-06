package tk.sirtwinkles.spicedtea.sys.render;

import tk.sirtwinkles.spicedtea.Globals;
import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.components.InventoryComponent;
import tk.sirtwinkles.spicedtea.entities.Entity;
import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class InventoryRenderer implements Renderer {
	private static final int GUI_TILE_SIZE = 8;

	private Texture gui;
	private InventoryComponent inv;
	
	public InventoryRenderer() {
		this.gui = Globals.assets.get("data/gui.png");
	}
	
	@Override
	public void render(GraphicsContext context, PlayingState state,
			Viewport view) {
		SpriteBatch batch = context.getBatch();

		final Rectangle size = view.getSizeAndPosition();
		final int width = 80;
		final int height = 32;
		final int xbase = (int) ((size.width / 2) - (width / 2));
		final int ybase = (int) ((size.height / 2) - (height / 2));

		//Draw corners.
		drawTile(batch, xbase - GUI_TILE_SIZE, ybase - GUI_TILE_SIZE, 0, 0);
		drawTile(batch, xbase - GUI_TILE_SIZE, ybase + height, 0, 2);
		drawTile(batch, xbase + width, ybase - GUI_TILE_SIZE, 2, 0);
		drawTile(batch, xbase + width, ybase + height, 2, 2);
		//Draw vertical thingies
		for (int y = 0; y < height; y += GUI_TILE_SIZE) {
			drawTile(batch, xbase - GUI_TILE_SIZE, ybase + y, 0, 1);
			drawTile(batch, xbase + width, ybase + y, 2, 1);
		}
		//Draw horizontal thingies
		for (int x = 0; x < width; x += GUI_TILE_SIZE) {
			drawTile(batch, xbase + x, ybase - GUI_TILE_SIZE, 1, 0);
			drawTile(batch, xbase + x, ybase + height, 1, 2);
		}
		//Draw center
		for (int x = 0; x < width; x += GUI_TILE_SIZE) {
			for (int y = 0; y < height; y += GUI_TILE_SIZE) {
				drawTile(batch, xbase + x, ybase + y, 1, 1);
			}
		}
		//Draw inventory.
		Entity[][] contents = inv.getContents();
		for (int x = 0; x < inv.getWidth(); ++x) {
			for (int y = 0; y < inv.getHeight(); ++y) {
				if (contents[x][y] != null) {
					//XXX: Remove this.
					if (y % 2 == 0) {
						drawTile(batch, xbase + x * GUI_TILE_SIZE, ybase + y * GUI_TILE_SIZE, 3, 0);
					} else {
						drawTile(batch, xbase + x * GUI_TILE_SIZE, ybase + y * GUI_TILE_SIZE, 3, 1);
					}
				}
			}
		}
	}
	
	public void setInventory(InventoryComponent inv) {
		this.inv = inv;
	}

	private void drawTile(SpriteBatch batch, int x, int y, int tx, int ty) {
		batch.draw(gui, x, y, GUI_TILE_SIZE, GUI_TILE_SIZE, tx * GUI_TILE_SIZE,
				ty * GUI_TILE_SIZE, GUI_TILE_SIZE, GUI_TILE_SIZE, false, true);
	}
}