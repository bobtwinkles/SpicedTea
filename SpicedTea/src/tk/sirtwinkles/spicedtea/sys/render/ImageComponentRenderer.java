package tk.sirtwinkles.spicedtea.sys.render;

import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.components.ImageComponent;
import tk.sirtwinkles.spicedtea.components.PositionComponent;

import com.badlogic.gdx.math.Rectangle;

public class ImageComponentRenderer {
	private ImageComponent comp;
	
	public ImageComponentRenderer(ImageComponent comp) {
		this.comp = comp;
	}
	
	public void render(GraphicsContext context, Viewport view) {
		Rectangle rect = view.getSizeAndPosition();
		PositionComponent pos = (PositionComponent) comp.getOwner().getComponent("position");
		context.getBatch().draw(comp.getImage(), pos.x * LevelRenderer.TILE_SIZE, pos.y * LevelRenderer.TILE_SIZE);
	}
}