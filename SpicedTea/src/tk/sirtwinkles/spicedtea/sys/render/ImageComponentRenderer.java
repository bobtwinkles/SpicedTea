package tk.sirtwinkles.spicedtea.sys.render;

import static tk.sirtwinkles.spicedtea.sys.render.LevelRenderer.TILE_SIZE;
import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.components.ImageComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;
import tk.sirtwinkles.spicedtea.state.PlayingState;

import com.badlogic.gdx.math.Rectangle;

public class ImageComponentRenderer implements Renderer {
	private ImageComponent comp;
	
	public ImageComponentRenderer() {
	}
	
	public void setComponent(ImageComponent comp) {
		this.comp = comp;
	}
	
	public void render(GraphicsContext context, PlayingState state, Viewport view) {
		Rectangle viewRect = view.getSizeAndPosition();
		final int tx = (int) (viewRect.x / TILE_SIZE);
		final int ty = (int) (viewRect.y / TILE_SIZE);
		PositionComponent pos = (PositionComponent) comp.getOwner().getComponent("position");
		if (state.getWorld().getCurrent().isVisible(pos.x, pos.y)) {
			context.getBatch().draw(comp.getImage(), (pos.x - tx) * TILE_SIZE, (pos.y - ty) * TILE_SIZE);
		}
	}
}