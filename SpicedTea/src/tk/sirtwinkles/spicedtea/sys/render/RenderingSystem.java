package tk.sirtwinkles.spicedtea.sys.render;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

import tk.sirtwinkles.spicedtea.GameSpicedTea;
import tk.sirtwinkles.spicedtea.GraphicsContext;
import tk.sirtwinkles.spicedtea.state.PlayingState;
import tk.sirtwinkles.spicedtea.sys.System;

public class RenderingSystem extends System {
	private static final float PXL_SCALE = 2f;
	private LinkedList<ImageComponentRenderer> componentRenderers;
	private Viewport view;
	private OrthographicCamera cam;
	private LevelRenderer levelRen;
	
	public RenderingSystem(Viewport view, PlayingState state) {
		super("tk.sirtwinkles.spicedtea.sys.render.RenderSystem");
		this.componentRenderers = new LinkedList<ImageComponentRenderer>();
		this.view = view;
		this.cam = new OrthographicCamera(1, 1);
		Gdx.graphics.getGL10().glClearColor(0.1f, 0.0f, 0.1f, 1.0f);
		this.levelRen = new LevelRenderer(state.getWorld().getCurrent(), state.getAssets());
	}

	@Override
	public void run(GameSpicedTea game) {
		//setup of some vars.
		GL10 gl10 = Gdx.graphics.getGL10();
		GraphicsContext context = game.getContext();
		
		view.resize(context.getWidth() / PXL_SCALE, context.getHeight() / PXL_SCALE);
		Rectangle viewRect = view.getSizeAndPosition();
		cam.setToOrtho(true, viewRect.width, viewRect.height);
		context.getBatch().setProjectionMatrix(cam.combined);

		gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
		context.begin(); // Begin rendering.
		for (ImageComponentRenderer ren : componentRenderers) {
			ren.render(context);
		}
		levelRen.render(context, view);
		context.end();
	}
	
	public void addRenderer(ImageComponentRenderer comp) {
		componentRenderers.add(comp);
	}
	
	public void removeRenderer(ImageComponentRenderer comp) {
		componentRenderers.remove(comp);
	}
}